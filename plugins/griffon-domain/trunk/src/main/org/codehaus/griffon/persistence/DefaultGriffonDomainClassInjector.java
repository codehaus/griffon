/*
 * Copyright 2004-2010 the original author or authors.
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
package org.codehaus.griffon.persistence;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.codehaus.groovy.ast.ClassHelper;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.MethodNode;
import org.codehaus.groovy.ast.Parameter;
import org.codehaus.groovy.ast.PropertyNode;
import org.codehaus.groovy.ast.expr.ClassExpression;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.ast.expr.GStringExpression;
import org.codehaus.groovy.ast.expr.MapEntryExpression;
import org.codehaus.groovy.ast.expr.MapExpression;
import org.codehaus.groovy.ast.expr.VariableExpression;
import org.codehaus.groovy.ast.stmt.ReturnStatement;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.griffon.ast.GriffonASTUtils;
import griffon.domain.artifacts.GriffonDomainClassProperty;

/**
 * Default implementation of domain class injector interface that adds the 'id'
 * and 'version' properties and other previously boilerplate code.
 *
 * @author Graeme Rocher (Grails 0.2)
 */
public abstract class DefaultGriffonDomainClassInjector extends GriffonDomainClassInjector {
    private List<ClassNode> classesWithInjectedToString = new ArrayList<ClassNode>();

    protected void performInjection(ClassNode classNode) {
        injectIdProperty(classNode);
        injectVersionProperty(classNode);
        injectToStringMethod(classNode, GriffonDomainClassProperty.IDENTITY);
        injectAssociations(classNode);
    }

    protected void injectAssociations(ClassNode classNode) {
        List<PropertyNode> propertiesToAdd = new ArrayList<PropertyNode>();
        for (PropertyNode pn : classNode.getProperties()) {
            final String name = pn.getName();
            final boolean isHasManyProperty = name.equals(GriffonDomainClassProperty.RELATES_TO_MANY) ||
                    name.equals(GriffonDomainClassProperty.HAS_MANY);
            if (isHasManyProperty) {
                Expression e = pn.getInitialExpression();
                propertiesToAdd.addAll(createPropertiesForHasManyExpression(e, classNode));
            }
            final boolean isBelongsTo = name.equals(GriffonDomainClassProperty.BELONGS_TO) || name.equals(GriffonDomainClassProperty.HAS_ONE);
            if (isBelongsTo) {
                Expression e = pn.getInitialExpression();
                propertiesToAdd.addAll(createPropertiesForBelongsToExpression(e, classNode));
            }
        }
        injectAssociationProperties(classNode, propertiesToAdd);
    }

    protected Collection<PropertyNode> createPropertiesForBelongsToExpression(Expression e, ClassNode classNode) {
        List<PropertyNode> properties = new ArrayList<PropertyNode>();
        if (e instanceof MapExpression) {
            MapExpression me = (MapExpression) e;
            for (MapEntryExpression mme : me.getMapEntryExpressions()) {
                String key = mme.getKeyExpression().getText();
                final Expression expression = mme.getValueExpression();
                ClassNode type;
                if (expression instanceof ClassExpression) {
                    type = expression.getType();
                }
                else {
                    type = ClassHelper.make(expression.getText());
                }

                properties.add(new PropertyNode(key, Modifier.PUBLIC, type, classNode, null, null, null));
            }
        }

        return properties;
    }

    protected void injectAssociationProperties(ClassNode classNode, List<PropertyNode> propertiesToAdd) {
        for (PropertyNode pn : propertiesToAdd) {
            if (!GriffonASTUtils.hasProperty(classNode, pn.getName())) {
                classNode.addProperty(pn);
            }
        }
    }

    protected List<PropertyNode> createPropertiesForHasManyExpression(Expression e, ClassNode classNode) {
        List<PropertyNode> properties = new ArrayList<PropertyNode>();
        if (e instanceof MapExpression) {
            MapExpression me = (MapExpression) e;
            for (MapEntryExpression mee : me.getMapEntryExpressions()) {
                String key = mee.getKeyExpression().getText();
                addAssociationForKey(key, properties, classNode);
            }
        }
        return properties;
    }

    protected void addAssociationForKey(String key, List<PropertyNode> properties, ClassNode classNode) {
        properties.add(new PropertyNode(key, Modifier.PUBLIC, new ClassNode(Set.class), classNode, null, null, null));
    }

    protected void injectToStringMethod(ClassNode classNode, String propertyName) {
        final boolean hasToString = GriffonASTUtils.implementsOrInheritsZeroArgMethod(
                classNode, "toString", classesWithInjectedToString);

        if (!hasToString && !GriffonASTUtils.isEnum(classNode)) {
            GStringExpression ge = new GStringExpression(classNode.getName() + " : ${"+ propertyName +"}");
            ge.addString(new ConstantExpression(classNode.getName() + " : "));
            ge.addValue(new VariableExpression(propertyName));
            Statement s = new ReturnStatement(ge);
            MethodNode mn = new MethodNode("toString", Modifier.PUBLIC, new ClassNode(String.class), new Parameter[0], new ClassNode[0], s);
            classNode.addMethod(mn);
            classesWithInjectedToString.add(classNode);
        }
    }

    protected void injectVersionProperty(ClassNode classNode) {
        GriffonASTUtils.injectProperty(classNode, GriffonDomainClassProperty.VERSION, Long.class);
    }

    protected void injectIdProperty(ClassNode classNode) {
        GriffonASTUtils.injectProperty(classNode, GriffonDomainClassProperty.IDENTITY, Long.class);
    }
}
