package org.codehaus.griffon.compiler.support;

import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.*;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.GroovyASTTransformation;

import griffon.plugins.jzy3d.artifact.GriffonChart3D;
import griffon.plugins.jzy3d.artifact.GriffonChart3DClass;
import griffon.plugins.jzy3d.artifact.AbstractGriffonChart3D;

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
