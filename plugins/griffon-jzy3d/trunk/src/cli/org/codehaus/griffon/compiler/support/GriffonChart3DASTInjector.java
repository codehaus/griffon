package org.codehaus.griffon.compiler.support;

import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.*;
import static org.codehaus.griffon.ast.GriffonASTUtils.*;

import org.jzy3d.chart.Chart;

public class GriffonChart3DASTInjector extends GriffonMvcArtifactASTInjector {
    private static final ClassNode CHART_CLASS = ClassHelper.makeWithoutCaching(Chart.class);
    
    public void inject(ClassNode classNode, String artifactType) {
        super.inject(classNode, artifactType);
    
        FieldNode chartField = classNode.addField(
            "this$chart",
            ACC_FINAL | ACC_PRIVATE | ACC_SYNTHETIC,
            CHART_CLASS,
            new ConstructorCallExpression(CHART_CLASS, args(constx("swing")))
        );
        
        // Chart getChart()
        classNode.addMethod(new MethodNode(
            "getChart",
            ACC_PUBLIC,
            CHART_CLASS,
            Parameter.EMPTY_ARRAY,
            ClassNode.EMPTY_ARRAY,
            returns(field(chartField))
        ));
    }
}
