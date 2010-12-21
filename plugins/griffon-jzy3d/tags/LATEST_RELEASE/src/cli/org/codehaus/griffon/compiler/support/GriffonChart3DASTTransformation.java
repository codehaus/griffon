/*
 * Copyright (c) 2010 Griffon Jzy3d - Andres Almiray. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 *  o Neither the name of Griffon Jzy3d - Andres Almiray nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.codehaus.griffon.compiler.support;

import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.*;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.GroovyASTTransformation;

import griffon.plugins.jzy3d.GriffonChart3D;
import griffon.plugins.jzy3d.GriffonChart3DClass;
import org.codehaus.griffon.runtime.jzy3d.AbstractGriffonChart3D;

/**
 * Handles generation of code for 3D charts.<p/>
 *
 * @author Andres Almiray 
 */
@GroovyASTTransformation(phase=CompilePhase.CANONICALIZATION)
public class GriffonChart3DASTTransformation extends GriffonArtifactASTTransformation {
    private static final String ARTIFACT_PATH = "charts";
    private static final ClassNode GRIFFON_CHART3D_CLASS = ClassHelper.makeWithoutCaching(GriffonChart3D.class);
    private static final ClassNode ABSTRACT_GRIFFON_CHART3D_CLASS = ClassHelper.makeWithoutCaching(AbstractGriffonChart3D.class);    
    
    protected void transform(ClassNode classNode, SourceUnit source, String artifactPath) {
        if(!ARTIFACT_PATH.equals(artifactPath) || !classNode.getName().endsWith(GriffonChart3DClass.TRAILING)) return;

        if(ClassHelper.OBJECT_TYPE.equals(classNode.getSuperClass())) {
            classNode.setSuperClass(ABSTRACT_GRIFFON_CHART3D_CLASS);
        } else if(!classNode.implementsInterface(GRIFFON_CHART3D_CLASS)){
            // 1. add interface
            classNode.addInterface(GRIFFON_CHART3D_CLASS);
            // 2. add methods
            ASTInjector injector = new GriffonChart3DASTInjector();
            injector.inject(classNode, GriffonChart3DClass.TYPE);
        }
    }
}
