/*
 * Copyright 2009-2011 the original author or authors.
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
import org.codehaus.groovy.transform.GroovyASTTransformation;

import griffon.plugins.slideware.GriffonSlide;
import griffon.plugins.slideware.GriffonSlideClass;
import org.codehaus.griffon.runtime.slideware.AbstractGriffonSlideScript;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles generation of code for Griffon slides.
 * <p/>
 *
 * @author Andres Almiray 
 */
@GroovyASTTransformation(phase=CompilePhase.CANONICALIZATION)
public class GriffonSlideASTTransformation extends GriffonArtifactASTTransformation {
    private static final Logger LOG = LoggerFactory.getLogger(GriffonSlideASTTransformation.class);
    private static final String ARTIFACT_PATH = "slides";
    private static final ClassNode GRIFFON_SLIDE_CLASS = ClassHelper.makeWithoutCaching(GriffonSlide.class);
    private static final ClassNode ABSTRACT_GRIFFON_SLIDE_SCRIPT_CLASS = ClassHelper.makeWithoutCaching(AbstractGriffonSlideScript.class);

    protected boolean allowsScriptAsArtifact() {
        return true;
    }
    
    protected void transform(ClassNode classNode, SourceUnit source, String artifactPath) {
        if(!ARTIFACT_PATH.equals(artifactPath) || !classNode.getName().endsWith(GriffonSlideClass.TRAILING)) return;

        if(classNode.isDerivedFrom(ClassHelper.SCRIPT_TYPE)) {
            if(LOG.isDebugEnabled()) LOG.debug("Setting "+ABSTRACT_GRIFFON_SLIDE_SCRIPT_CLASS.getName()+" as the superclass of "+classNode.getName());
            classNode.setSuperClass(ABSTRACT_GRIFFON_SLIDE_SCRIPT_CLASS);
        } else if(!classNode.implementsInterface(GRIFFON_SLIDE_CLASS)){
            inject(classNode);
        }
    }

    private void inject(ClassNode classNode) {
        if(LOG.isDebugEnabled()) LOG.debug("Injecting "+GRIFFON_SLIDE_CLASS.getName()+" behavior to "+ classNode.getName());
        // 1. add interface
        classNode.addInterface(GRIFFON_SLIDE_CLASS);
        // 2. add methods
        ASTInjector injector = new GriffonSlideASTInjector();
        injector.inject(classNode, GriffonSlideClass.TYPE);
    }
}
