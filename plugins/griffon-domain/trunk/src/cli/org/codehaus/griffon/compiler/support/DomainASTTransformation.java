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

import griffon.domain.GriffonDomain;
import griffon.transform.Domain;
import groovy.util.ConfigObject;
import org.codehaus.griffon.ast.AbstractASTTransformation;
import org.codehaus.griffon.compiler.GriffonCompilerContext;
import org.codehaus.groovy.ast.ASTNode;
import org.codehaus.groovy.ast.AnnotatedNode;
import org.codehaus.groovy.ast.AnnotationNode;
import org.codehaus.groovy.ast.ClassNode;
import org.codehaus.groovy.ast.expr.ConstantExpression;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.GroovyASTTransformation;

/**
 * Injects the necessary fields and behaviors into a domain class in order to make it a property domain entity.
 *
 * @author Graeme Rocher (Grails 1.1)
 */
@GroovyASTTransformation(phase = CompilePhase.CANONICALIZATION)
public class DomainASTTransformation extends AbstractASTTransformation {
    private static final ClassNode MY_TYPE = new ClassNode(Domain.class);
    private static final String MY_TYPE_NAME = "@" + MY_TYPE.getNameWithoutPackage();

    public void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
        if (!(astNodes[0] instanceof AnnotationNode) || !(astNodes[1] instanceof AnnotatedNode)) {
            throw new RuntimeException("Internal error: wrong types: $node.class / $parent.class");
        }

        AnnotatedNode parent = (AnnotatedNode) astNodes[1];
        AnnotationNode node = (AnnotationNode) astNodes[0];
        if (!MY_TYPE.equals(node.getClassNode()) || !(parent instanceof ClassNode)) {
            return;
        }

        ClassNode classNode = (ClassNode) parent;
        String cName = classNode.getName();
        if (classNode.isInterface()) {
            throw new RuntimeException("Error processing interface '" + cName + "'. " +
                    MY_TYPE_NAME + " not allowed for interfaces.");
        }

        String implementation = null;
        Expression member = node.getMember("value");
        if (member instanceof ConstantExpression) {
            implementation = (String) ((ConstantExpression) member).getValue();
        }
        if (implementation == null || implementation.trim().length() == 0) {
            /*
            throw new RuntimeException("Error processing " + MY_TYPE_NAME + " in " + cName +
                    ". Implementation value '" + implementation + "' is invalid.");
            */
            Object defaultMapping = GriffonCompilerContext.getFlattenedBuildSettings().get(GriffonDomainASTTransformation.GRIFFON_DOMAIN_DEFAULT_MAPPING);
            if (defaultMapping != null && !(defaultMapping instanceof ConfigObject)) {
                implementation = String.valueOf(defaultMapping);
            } else {
                implementation = "memory";
            }
        }

        String datasource = null;
        member = node.getMember("datasource");
        if (member instanceof ConstantExpression) {
            datasource = (String) ((ConstantExpression) member).getValue();
        }
        if (datasource == null || datasource.trim().length() == 0) {
            Object defaultDatasource = GriffonCompilerContext.getFlattenedBuildSettings().get(GriffonDomainASTTransformation.GRIFFON_DOMAIN_DEFAULT_DATASOURCE);
            if (defaultDatasource != null && !(defaultDatasource instanceof ConfigObject)) {
                datasource = String.valueOf(defaultDatasource);
            } else {
                datasource = "default";
            }
        }

        GriffonDomainASTTransformation.inject(classNode, implementation, datasource);
    }
}