/* 
 * Copyright 2004-2010 Graeme Rocher
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.griffon.compiler.support;

import griffon.core.GriffonClass;
import griffon.transform.Sortable;
import griffon.util.GriffonNameUtils;
import org.codehaus.griffon.ast.AbstractASTTransformation;
import org.codehaus.griffon.runtime.util.AbstractComparator;
import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.BlockStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.GroovyASTTransformation;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.codehaus.griffon.ast.GriffonASTUtils.*;

/**
 * Injects a set of Comparators and sort methods.
 *
 * @author Andres Almiray
 */
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class SortableASTTransformation extends AbstractASTTransformation {
    private static final ClassNode MY_TYPE = new ClassNode(Sortable.class);
    private static final String MY_TYPE_NAME = "@" + MY_TYPE.getNameWithoutPackage();
    private static final ClassNode COMPARABLE_TYPE = ClassHelper.makeWithoutCaching(Comparable.class);
    private static final ClassNode COMPARATOR_TYPE = ClassHelper.makeWithoutCaching(Comparator.class);
    private static final ClassNode ABSTRACT_COMPARATOR_TYPE = ClassHelper.makeWithoutCaching(AbstractComparator.class);
    private static final Expression NIL = ConstantExpression.NULL;

    public void visit(ASTNode[] nodes, SourceUnit source) {
        checkNodesForAnnotationAndType(nodes[0], nodes[1]);
        AnnotationNode annotation = (AnnotationNode) nodes[0];
        AnnotatedNode parent = (AnnotatedNode) nodes[1];

        if (parent instanceof ClassNode) {
            createSortable(source, annotation, (ClassNode) parent);
        }
    }

    public static void createSortable(SourceUnit sourceUnit, AnnotationNode annotation, ClassNode classNode) {
        List<PropertyNode> properties = findProperties(annotation, classNode);
        if (!classNode.implementsInterface(COMPARABLE_TYPE)) {
            classNode.addInterface(COMPARABLE_TYPE);
        }

        classNode.addMethod(new MethodNode(
                "compareTo",
                ACC_PUBLIC,
                ClassHelper.int_TYPE,
                params(param(ClassHelper.OBJECT_TYPE, "obj")),
                ClassNode.EMPTY_ARRAY,
                createCompareToMethodBody(classNode, properties)
        ));

        for (PropertyNode property : properties) {
            createComparatorFor(classNode, property);
        }
    }

    private static Statement createCompareToMethodBody(ClassNode classNode, List<PropertyNode> properties) {
        List<Statement> statements = new ArrayList<Statement>();

        // if(this.is(obj)) return 0;
        statements.add(ifs(new MethodCallExpression(THIS, "is", vars("obj")), constx(0)));
        // if(!(obj instanceof <type>)) return -1;
        statements.add(ifs(not(iof(var("obj"), classx(classNode))), constx(-1)));
        // int value = 0;
        statements.add(decls(var("value", ClassHelper.int_TYPE), constx(0)));
        for (PropertyNode property : properties) {
            String name = property.getName();
            // value = this.prop <=> obj.prop;
            statements.add(
                    assigns(var("value"), cmp(prop(THIS, name), prop(var("obj"), name)))
            );
            // if(value != 0) return value;
            statements.add(
                    ifs(ne(var("value"), constx(0)), var("value"))
            );
        }

        if (properties.isEmpty()) {
            // let this object be less than obj
            statements.add(returns(constx(-1)));
        } else {
            // objects are equal
            statements.add(returns(constx(0)));
        }

        final BlockStatement body = new BlockStatement();
        body.addStatements(statements);
        return body;
    }

    private static Statement createCompareToMethodBody(ClassNode classNode, PropertyNode property) {
        String propertyName = property.getName();
        return block(
                // if(a == b) return 0;
                ifs(eq(var("a"), var("b")), constx(0)),
                // if(a != null && b == null) return -1;
                ifs(and(ne(var("a"), NIL), eq(var("b"), NIL)), constx(-1)),
                // if(a == null && b != null) return 1;
                ifs(and(eq(var("a"), NIL), ne(var("b"), NIL)), constx(1)),
                // return a.prop <=> b.prop;
                returns(cmp(prop(var("a"), propertyName), prop(var("b"), propertyName)))
        );
    }

    private static void createComparatorFor(ClassNode classNode, PropertyNode property) {
        String propertyName = property.getName();
        String className = classNode.getName() + "$" + GriffonNameUtils.capitalize(propertyName) + "Comparator";
        InnerClassNode cmpClass = new InnerClassNode(classNode, className, ACC_PRIVATE | ACC_STATIC, ABSTRACT_COMPARATOR_TYPE);
        classNode.getModule().addClass(cmpClass);

        cmpClass.addMethod(new MethodNode(
                "compare",
                ACC_PUBLIC,
                ClassHelper.int_TYPE,
                params(
                        param(ClassHelper.OBJECT_TYPE, "a"),
                        param(ClassHelper.OBJECT_TYPE, "b")),
                ClassNode.EMPTY_ARRAY,
                createCompareToMethodBody(classNode, property)
        ));

        String fieldName = "this$" + GriffonNameUtils.capitalize(propertyName) + "Comparator";
        // private final Comparator this$<property>Comparator = new <type>$<property>Comparator();
        FieldNode cmpField = classNode.addField(
                fieldName,
                ACC_STATIC | ACC_FINAL | ACC_PRIVATE | ACC_SYNTHETIC,
                COMPARATOR_TYPE,
                new ConstructorCallExpression(cmpClass, NO_ARGS));

        classNode.addMethod(new MethodNode(
                "comparatorBy" + GriffonNameUtils.capitalize(propertyName),
                ACC_PUBLIC | ACC_STATIC,
                COMPARATOR_TYPE,
                Parameter.EMPTY_ARRAY,
                ClassNode.EMPTY_ARRAY,
                returns(field(cmpField))
        ));
    }

    private static List<PropertyNode> findProperties(AnnotationNode annotation, ClassNode classNode) {
        List<PropertyNode> properties = new ArrayList<PropertyNode>();
        List<String> includes = new ArrayList<String>();
        List<String> excludes = new ArrayList<String>();

        Expression expr = annotation.getMember("includes");
        if (expr instanceof ListExpression) {
            for (Expression x : ((ListExpression) expr).getExpressions()) {
                if (x instanceof ConstantExpression) {
                    includes.add(String.valueOf(((ConstantExpression) x).getValue()));
                }
            }
        }
        expr = annotation.getMember("excludes");
        if (expr instanceof ListExpression) {
            for (Expression x : ((ListExpression) expr).getExpressions()) {
                if (x instanceof ConstantExpression) {
                    excludes.add(String.valueOf(((ConstantExpression) x).getValue()));
                }
            }
        }

        for (PropertyNode property : classNode.getProperties()) {
            String propertyName = property.getName();
            if (property.isStatic() ||
                    excludes.contains(propertyName) ||
                    !includes.isEmpty() && !includes.contains(propertyName) ||
                    GriffonClass.STANDARD_PROPERTIES.contains(propertyName)) continue;
            properties.add(property);
        }

        return properties;
    }
}