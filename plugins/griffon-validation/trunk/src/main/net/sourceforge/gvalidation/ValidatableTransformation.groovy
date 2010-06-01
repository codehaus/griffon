/*
 * Copyright 2010 the original author or authors.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.sourceforge.gvalidation

import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.transform.GroovyASTTransformation

/**
 * Created by nick.zhu
 */
@GroovyASTTransformation(phase=CompilePhase.SEMANTIC_ANALYSIS)
class ValidatableTransformation implements ASTTransformation {

    void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
        def classNode = sourceUnit.getAST()?.classNode()

        println classNode
    }


}
