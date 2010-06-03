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

import org.codehaus.groovy.ast.ASTNode
import org.codehaus.groovy.ast.ClassNode
import org.codehaus.groovy.ast.MethodNode
import org.codehaus.groovy.ast.Parameter
import org.codehaus.groovy.ast.builder.AstBuilder
import org.codehaus.groovy.ast.expr.ConstantExpression
import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.control.SourceUnit
import org.codehaus.groovy.transform.ASTTransformation
import org.codehaus.groovy.transform.GroovyASTTransformation
import org.objectweb.asm.Opcodes
import org.codehaus.groovy.ast.ClassHelper
import org.codehaus.groovy.ast.stmt.BlockStatement
import org.codehaus.groovy.ast.stmt.ReturnStatement
import org.codehaus.groovy.ast.VariableScope

/**
 * Groovy AST transformation class that enhances any class
 * annotated as @Validatable during compile time automatically 
 *
 * Created by nick.zhu
 */
@GroovyASTTransformation(phase = CompilePhase.SEMANTIC_ANALYSIS)
class ValidatableTransformation implements ASTTransformation {

    void visit(ASTNode[] astNodes, SourceUnit sourceUnit) {
        ClassNode classNode = sourceUnit.getAST()?.classes.first()

        if (annotatedWithValidatable(classNode)) {
            if (hasNotInjectedMethod(classNode)) {
                MethodNode validateMethod = buildValidateMethod()
                classNode.addMethod(validateMethod)
            }
        }
    }

    private boolean annotatedWithValidatable(ClassNode classNode) {
        def validatableAnnotation = classNode.getAnnotations().find {
            it.classNode.typeClass == Validatable
        }

        return validatableAnnotation != null
    }

    private boolean hasNotInjectedMethod(ClassNode classNode) {
        return !classNode.hasMethod('validate',
                [new Parameter(new ClassNode(Object), 'fields', new ConstantExpression(null))] as Parameter[]
        )
    }

    private MethodNode buildValidateMethod() {
        def methodNode = new MethodNode(
                "validate",
                Opcodes.ACC_PUBLIC,
                ClassHelper.make(Boolean, false),
                [new Parameter(ClassHelper.make(Object, false), "fields", new ConstantExpression(null))] as Parameter[],
                [] as ClassNode[],
                new BlockStatement(
                        new AstBuilder().buildFromCode {
                            def enhancer = net.sourceforge.gvalidation.ValidationEnhancer.enhance(this)
                            return enhancer.doValidate(fields)
                        },
                        new VariableScope()
                ))

         return methodNode
    }

}
