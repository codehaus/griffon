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

package lombok.javac.handlers;

import com.sun.tools.javac.code.Flags;
import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.JCTree.JCAnnotation;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import griffon.transform.Threading;
import griffon.util.GriffonClassUtils.MethodDescriptor;
import lombok.core.AST.Kind;
import lombok.core.AnnotationValues;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static griffon.util.GriffonClassUtils.isEventHandler;
import static griffon.util.GriffonClassUtils.isPlainMethod;
import static lombok.javac.handlers.AstBuilder.defClass;
import static lombok.javac.handlers.AstBuilder.defMethod;
import static lombok.javac.handlers.HandlerUtils.*;
import static lombok.javac.handlers.JavacHandlerUtil.deleteAnnotationIfNeccessary;
import static org.codehaus.griffon.ast.ThreadingASTTransformation.getThreadingMethod;
import static org.codehaus.griffon.ast.ThreadingASTTransformation.skipInjection;

/**
 * @author Andres Almiray
 */
public class HandleThreading extends JavacAnnotationHandler<Threading> {
    private static final Logger LOG = LoggerFactory.getLogger(HandleThreading.class);

    public void handle(AnnotationValues<Threading> annotation, JCAnnotation ast, JavacNode annotationNode) {
        deleteAnnotationIfNeccessary(annotationNode, Threading.class);
        JavacNode methodNode = annotationNode.up();

        if (methodNode == null || methodNode.getKind() != Kind.METHOD || !(methodNode.get() instanceof JCTree.JCMethodDecl)) {
            annotationNode.addError("@Threading is legal only on methods.");
            return;
        }

        JCTree.JCMethodDecl method = (JCTree.JCMethodDecl) methodNode.get();
        TokenBuilder b = new TokenBuilder(methodNode.up());

        if ((method.mods.flags & Flags.ABSTRACT) != 0) {
            annotationNode.addError("@Threading is legal only on concrete methods.");
            return;
        }

        Threading.Policy threadingPolicy = annotation.getInstance().value();
        if (threadingPolicy == Threading.Policy.SKIP) return;

        String threadingMethod = getThreadingMethod(threadingPolicy);
        handleMethodForInjection(b, methodNode, method, threadingMethod);
    }

    private void handleMethodForInjection(TokenBuilder b, JavacNode methodNode, JCTree.JCMethodDecl method, String threadingMethod) {
        MethodDescriptor md = methodDescriptorFor(method);
        if (isPlainMethod(md) &&
                !isEventHandler(md) &&
                hasVoidOrDefAsReturnType(method) &&
                !skipInjection(b.context.getName() + "." + method.getName())) {
            wrapStatements(b, methodNode, method, threadingMethod);
        }
    }

    private boolean hasVoidOrDefAsReturnType(JCTree.JCMethodDecl method) {
        String type = method.getReturnType().type.toString();
        return "void".equals(type) ||
                "Object".equals(type) ||
                "java.lang.Object".equals(type);
    }

    private void wrapStatements(TokenBuilder b, JavacNode methodNode, JCTree.JCMethodDecl method, String threadingMethod) {
        // 0. abort if method is empty
        if (method.getBody().getStatements().isEmpty()) return;

        // 1. make method parameters final
        for (JCTree.JCVariableDecl parameter : method.getParameters()) {
            makeFinal(parameter);
        }

        // 2. create Runnable anonymous inner class wrapping method body
        TreeMaker m = methodNode.getTreeMaker();

        JCTree.JCClassDecl runnableClass = defClass("ThreadingWrapper_" + methodNode.getName().toString())
                .modifiers(0)
                .implementing(Runnable.class)
                .withMembers(
                        defMethod("run").withBody(method.getBody().getStatements()).$(methodNode)
                ).$(methodNode);
        JCTree.JCExpression runnable = m.NewClass(null, NIL_EXPRESSION, b.type(Runnable.class), NIL_EXPRESSION, runnableClass);

        // 3. create call for UIThreadManager.getInstance().<threadingMethod>(runnable)
        JCTree.JCExpression uiThreadManagerInstance = b.dotExpr("griffon.core.UIThreadManager.getInstance");
        JCTree.JCExpression uiThreadManagerInstanceCall = m.Apply(NIL_EXPRESSION, uiThreadManagerInstance, NIL_EXPRESSION);
        JCTree.JCExpression call = b.call(m.Select(uiThreadManagerInstanceCall, b.name(threadingMethod)), List.<JCTree.JCExpression>of(runnable));

        // 4. substitute method body
        method.body = m.Block(0, List.<JCTree.JCStatement>of(m.Exec(call)));

        if (LOG.isDebugEnabled()) {
            LOG.debug("Modified " + b.context.getName() + "." + method.getName() + "() - code wrapped with " + threadingMethod + "{}");
        }
    }

    private void makeFinal(JCTree.JCVariableDecl parameter) {
        if ((parameter.mods.flags & Flags.FINAL) == 0) {
            parameter.mods.flags |= Flags.FINAL;
        }
    }
}
