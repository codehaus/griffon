/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
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
import org.codehaus.groovy.transform.GroovyASTTransformation;

import griffon.plugins.ldap.GriffonLdapSchema;
import griffon.plugins.ldap.GriffonLdapSchemaClass;
import org.codehaus.griffon.runtime.ldap.AbstractGriffonLdapSchema;

/**
 * Handles generation of code for Quartz jobs.<p/>
 *
 * @author Andres Almiray 
 */
@GroovyASTTransformation(phase=CompilePhase.CANONICALIZATION)
public class GriffonLdapSchemaASTTransformation extends GriffonArtifactASTTransformation {
    private static final String ARTIFACT_PATH = "jobs";
    private static final ClassNode GRIFFON_LDAP_SCHEMA_CLASS = ClassHelper.makeWithoutCaching(GriffonLdapSchema.class);
    private static final ClassNode ABSTRACT_GRIFFON_LDAP_SCHEMA_CLASS = ClassHelper.makeWithoutCaching(AbstractGriffonLdapSchema.class);
    
    protected void transform(ClassNode classNode, SourceUnit source, String artifactPath) {
        if(!ARTIFACT_PATH.equals(artifactPath) || !classNode.getName().endsWith(GriffonLdapSchemaClass.TRAILING)) return;

        if(ClassHelper.OBJECT_TYPE.equals(classNode.getSuperClass())) {
            classNode.setSuperClass(ABSTRACT_GRIFFON_LDAP_SCHEMA_CLASS);
        } else if(!classNode.implementsInterface(GRIFFON_LDAP_SCHEMA_CLASS)){
            // 1. add interface
            classNode.addInterface(GRIFFON_LDAP_SCHEMA_CLASS);
            // 2. add methods
            ASTInjector injector = new GriffonArtifactASTInjector();
            injector.inject(classNode, GriffonLdapSchemaClass.TYPE);
        }
    }
}
