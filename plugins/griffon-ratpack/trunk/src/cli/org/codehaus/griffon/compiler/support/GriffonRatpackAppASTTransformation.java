package org.codehaus.griffon.compiler.support;

import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.*;
import org.codehaus.groovy.control.CompilePhase;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.transform.GroovyASTTransformation;

import griffon.plugins.ratpack.artifact.GriffonRatpackApp;
import griffon.plugins.ratpack.artifact.GriffonRatpackAppClass;
import griffon.plugins.ratpack.artifact.AbstractGriffonRatpackApp;

/**
 * Handles generation of code for Ratpack apps.<p/>
 *
 * @author Andres Almiray 
 */
@GroovyASTTransformation(phase=CompilePhase.CANONICALIZATION)
public class GriffonRatpackAppASTTransformation extends GriffonArtifactASTTransformation {
    private static final String ARTIFACT_PATH = "ratpack";
    private static final ClassNode GRIFFON_RATPACK_APP_CLASS = ClassHelper.makeWithoutCaching(GriffonRatpackApp.class);
    private static final ClassNode ABSTRACT_GRIFFON_RATPACK_APP_CLASS = ClassHelper.makeWithoutCaching(AbstractGriffonRatpackApp.class);    
    
    protected void transform(ClassNode classNode, SourceUnit source, String artifactPath) {
        if(!ARTIFACT_PATH.equals(artifactPath) || !classNode.getName().endsWith(GriffonRatpackAppClass.TRAILING)) return;

        if(ClassHelper.OBJECT_TYPE.equals(classNode.getSuperClass())) {
            classNode.setSuperClass(ABSTRACT_GRIFFON_RATPACK_APP_CLASS);
        } else if(!classNode.implementsInterface(GRIFFON_RATPACK_APP_CLASS)){
            // 1. add interface
            classNode.addInterface(GRIFFON_RATPACK_APP_CLASS);
            // 2. add methods
            ASTInjector injector = new GriffonArtifactASTInjector();
            injector.inject(classNode, GriffonRatpackAppClass.TYPE);
        }
    }
}