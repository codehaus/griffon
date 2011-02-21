/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codehaus.griffon.compiler.support;

import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.*;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.control.messages.SimpleMessage;
import org.codehaus.groovy.control.messages.SyntaxErrorMessage;
import org.codehaus.groovy.runtime.MetaClassHelper;
import org.codehaus.groovy.syntax.SyntaxException;
import org.codehaus.groovy.syntax.Token;
import org.codehaus.groovy.syntax.Types;
import org.codehaus.groovy.transform.ASTTransformation;
import org.codehaus.groovy.transform.GroovyASTTransformation;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;
import org.codehaus.groovy.GroovyBugError;
import org.objectweb.asm.Opcodes;

import java.util.*;
import java.lang.reflect.Modifier;
import griffon.plugins.scaffolding.*;
import griffon.util.GriffonClassUtils;
import griffon.util.GriffonNameUtils;
import static org.codehaus.griffon.ast.GriffonASTUtils.*;
import org.codehaus.griffon.runtime.scaffolding.*;

/**
 * Handles generation of code for the {@code @Scaffold} annotation.
 * <p/>
 *
 * @author Andres Almiray
 */
@GroovyASTTransformation(phase = CompilePhase.INSTRUCTION_SELECTION)
public class ScaffoldASTTransformation implements ASTTransformation, Opcodes {
    private static final Class MY_CLASS = Scaffold.class;
    private static final ClassNode MY_TYPE = new ClassNode(MY_CLASS);
    private static final String MY_TYPE_NAME = "@" + MY_TYPE.getNameWithoutPackage();
    private static final ClassNode MODEL_CLASS = ClassHelper.makeWithoutCaching(Model.class);
    private static final ClassNode BEANMODEL_CLASS = ClassHelper.makeWithoutCaching(BeanModel.class);
    private static final ClassNode ABSTRACTBEANMODEL_CLASS = ClassHelper.makeWithoutCaching(AbstractBeanModel.class);
    private static final ClassNode MODELUTILS_CLASS = ClassHelper.makeWithoutCaching(ModelUtils.class);
    private static final ClassNode COLLECTIONS_CLASS = ClassHelper.makeWithoutCaching(Collections.class);
    private static final ClassNode LINKED_HASH_MAP_CLASS = ClassHelper.makeWithoutCaching(LinkedHashMap.class);

    private static final String TRAILING = "BeanModel";
    private static final String ATTRIBUTES_FIELD_NAME = "this$attributes";

    /**
     * Convenience method to see if an annotated node is {@code @Scaffold}.
     *
     * @param node the node to check
     * @return true if the node is an event publisher
     */
    public static boolean hasScaffoldAnnotation(AnnotatedNode node) {
        for (AnnotationNode annotation : (Collection<AnnotationNode>) node.getAnnotations()) {
            if (MY_TYPE.equals(annotation.getClassNode())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Handles the bulk of the processing, mostly delegating to other methods.
     *
     * @param nodes   the ast nodes
     * @param source  the source unit for the nodes
     */
    public void visit(ASTNode[] nodes, SourceUnit source) {
        init(nodes, source);
        AnnotationNode annotation = (AnnotationNode) nodes[0];
        AnnotatedNode parent = (AnnotatedNode) nodes[1];

        if(parent instanceof ClassNode) {
            addScaffoldToClass(source, annotation, (ClassNode) parent);
        }
    }

    private void addScaffoldToClass(SourceUnit source, AnnotationNode annotation, ClassNode classNode) {
        ClassNode sourceClass = findSourceClass(source, annotation, classNode);
        if(sourceClass == null) return;
        ClassNode targetClass = findTargetClass(source, annotation, sourceClass, classNode);

        FieldNode attributesField = targetClass.addField(
                ATTRIBUTES_FIELD_NAME,
                ACC_FINAL | ACC_PRIVATE | ACC_SYNTHETIC,
                ClassHelper.MAP_TYPE,
                new ConstructorCallExpression(LINKED_HASH_MAP_CLASS, NO_ARGS));

        List<PropertyNode> properties = findProperties(annotation, sourceClass);
        addAttributes(targetClass, sourceClass, properties);
        addConstructor(targetClass, sourceClass, properties);
    }

    private ClassNode findSourceClass(SourceUnit source, AnnotationNode annotation, ClassNode classNode) {
        ClassNode sourceClass = null;
        String className = classNode.getName();

        ClassExpression expr = (ClassExpression) annotation.getMember("value");
        if(expr == null) {
            if(!className.endsWith(TRAILING)) {
                // @Scaffold class Book {}
                sourceClass = classNode;
            } else {
                // @Scaffold class BookBeanModel {}
                // className = className.substring(0, className.length() - TRAILING.length());
                // sourceClass = ClassHelper.makeWithoutCaching(className);
                throw new GroovyBugError(MY_TYPE_NAME+" must annotate a class whose name does not end with '"+TRAILING+
                      "' when no Class has been defined as the annotation's value. Annotated class is '"+className+"'");
            }
        } else {
            ClassNode type = expr.getType();
 
            if(className.endsWith(TRAILING)) {
                // @Scaffold(Book) class BookBeanModel {}
                sourceClass = type;
            } else if(className.equals(type.getName())) {
                // @Scaffold(Book) class Book {}
                sourceClass = classNode;
            } else {
                // @Scaffold(X) class Book {}
                throw new GroovyBugError("Found "+MY_TYPE_NAME+" annotation on class '"+className+"' with value '"+type.getName()+
                      "'. Please remove the annotation value and recompile.");
            }
        }
        
        return sourceClass;
    }

    private ClassNode findTargetClass(SourceUnit source, AnnotationNode annotation, ClassNode sourceClass, ClassNode classNode) {
        if(sourceClass != classNode) {
            // make sure BeanModel is in the hierarchy
            if(classNode.isDerivedFrom(ABSTRACTBEANMODEL_CLASS)) return classNode;
            ClassNode cNode = classNode;
            while(cNode != null) {
                for(ClassNode interfaceNode : cNode.getAllInterfaces()) {
                    if(interfaceNode.isDerivedFrom(BEANMODEL_CLASS)) return classNode;
                }
                cNode = cNode.getSuperClass();
            }

            classNode.setSuperClass(ABSTRACTBEANMODEL_CLASS);
            return classNode;
        }

        ClassNode targetClass = new ClassNode(sourceClass.getName() + "BeanModel", ACC_PUBLIC, ABSTRACTBEANMODEL_CLASS);
        classNode.getModule().addClass(targetClass);
        return targetClass;
    }
 
    private List<PropertyNode> findProperties(AnnotationNode annotation, ClassNode sourceClass) {
        List<PropertyNode> properties = new ArrayList<PropertyNode>();
        List<String> excludes = new ArrayList<String>();
  
        Expression expr = (Expression) annotation.getMember("excludes");
        if(expr instanceof ListExpression) {
            for(Expression x : ((ListExpression) expr).getExpressions()) {
                if(x instanceof ConstantExpression) {
                    excludes.add(String.valueOf(((ConstantExpression)x).getValue()));
                }
            }
        }

        for(PropertyNode property : sourceClass.getProperties()) {
            if(!property.isStatic() && !excludes.contains(property.getName())) properties.add(property);
        }

        return properties;
    }

    private void addAttributes(ClassNode classNode, ClassNode sourceClass, List<PropertyNode> properties) {
        // List<Expression> fields = new ArrayList<Expression>();

        for(PropertyNode property : properties) {
            addReadOnlyProperty(classNode, property.getName(), MODEL_CLASS, null);
            // fields.add(field(classNode, property.getName()));
        }
       
        classNode.addMethod(new MethodNode(
            "getModelAttributes",
            ACC_PUBLIC,
            ClassHelper.MAP_TYPE,
            Parameter.EMPTY_ARRAY,
            ClassNode.EMPTY_ARRAY,
            returns(
                call(
                    COLLECTIONS_CLASS,
                    "unmodifiableMap",
                    args(field(classNode, ATTRIBUTES_FIELD_NAME)))
            )
        ));     
    }

    private void addConstructor(ClassNode classNode, ClassNode sourceClass, List<PropertyNode> properties) {
        if (!validateConstructors(classNode)) return;

        final BlockStatement body = new BlockStatement();

        body.addStatement(new ExpressionStatement(
            new ConstructorCallExpression(ClassNode.SUPER, args(classx(sourceClass)))
        ));

        for(PropertyNode property : properties) {
            body.addStatement(assignStatement(
                field(classNode, property.getName()),
                createMakeModelCall(classNode, sourceClass, property.getName())
            ));
        }

        Expression attributesField = field(classNode, ATTRIBUTES_FIELD_NAME);
        for(PropertyNode property : properties) {
            body.addStatement(stmnt(call(
                    attributesField,
                    "put",
                    args(constx(property.getName()), field(classNode, property.getName()))
                )
            ));
        }       
      
        body.addStatement(stmnt(call(
                VariableExpression.THIS_EXPRESSION,
                "attachToAttributes",
                NO_ARGS
            ))
        );

        classNode.addConstructor(new ConstructorNode(ACC_PUBLIC, Parameter.EMPTY_ARRAY, ClassNode.EMPTY_ARRAY, body));
    }

    private boolean validateConstructors(ClassNode classNode) {
        if(classNode.getDeclaredConstructors().size() != 0) {
            // TODO: allow constructors which only call provided constructor?
            addError("Explicit constructors not allowed for " + MY_TYPE_NAME + " class: " + classNode.getNameWithoutPackage(), classNode.getDeclaredConstructors().get(0));
        }
        return true;
    }

    private Expression createMakeModelCall(ClassNode classNode, ClassNode sourceClass, String propertyName) {
        return call(
            classx(MODELUTILS_CLASS),
            "makeModelFor",
            args(
                classx(sourceClass),
                VariableExpression.THIS_EXPRESSION,
                constx(propertyName) 
            )
        );
    }

    // ---------------------------

    private static final Token ASSIGN = Token.newSymbol(Types.ASSIGN, -1, -1);
    private SourceUnit sourceUnit;

    protected void init(ASTNode[] nodes, SourceUnit sourceUnit) {
        if (nodes.length != 2 || !(nodes[0] instanceof AnnotationNode) || !(nodes[1] instanceof AnnotatedNode)) {
            throw new GroovyBugError("Internal error: expecting [AnnotationNode, AnnotatedNode] but got: " + Arrays.asList(nodes));
        }
        this.sourceUnit = sourceUnit;
    }

    public static Statement assignStatement(Expression fieldExpr, Expression value) {
        return new ExpressionStatement(assignExpr(fieldExpr, value));
    }

    private static Expression assignExpr(Expression expression, Expression value) {
        return new BinaryExpression(expression, ASSIGN, value);
    }

    protected void addError(String msg, ASTNode expr) {
        int line = expr.getLineNumber();
        int col = expr.getColumnNumber();
        sourceUnit.getErrorCollector().addErrorAndContinue(
                new SyntaxErrorMessage(new SyntaxException(msg + '\n', line, col), sourceUnit)
        );
    }
}
