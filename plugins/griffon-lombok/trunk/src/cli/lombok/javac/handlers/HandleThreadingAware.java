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

import com.sun.tools.javac.tree.JCTree;
import com.sun.tools.javac.tree.TreeMaker;
import com.sun.tools.javac.util.List;
import griffon.transform.ThreadingAware;
import groovy.lang.Closure;
import lombok.core.AnnotationValues;
import lombok.javac.JavacAnnotationHandler;
import lombok.javac.JavacNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static com.sun.tools.javac.code.Flags.FINAL;
import static lombok.javac.handlers.AstBuilder.defMethod;
import static lombok.javac.handlers.AstBuilder.defVar;
import static lombok.javac.handlers.HandlerUtils.NIL_EXPRESSION;
import static lombok.javac.handlers.HandlerUtils.extractArgNames;
import static lombok.javac.handlers.JavacHandlerUtil.*;

/**
 * @author Andres Almiray
 */
public class HandleThreadingAware extends JavacAnnotationHandler<ThreadingAware> {
    private static final Logger LOG = LoggerFactory.getLogger(HandleThreadingAware.class);
    private static final String RUNNABLE_PARAM = "runnable";
    private static final String CLOSURE_PARAM = "closure";
    private static final String CALLABLE_PARAM = "callable";
    private static final String EXECUTOR_SERVICE_PARAM = "executorService";

    private static final String EXEC_FUTURE = "execFuture";
    private static final String EXECUTE_FUTURE = "executeFuture";

    public void handle(AnnotationValues<ThreadingAware> annotation, JCTree.JCAnnotation ast, JavacNode annotationNode) {
        deleteAnnotationIfNeccessary(annotationNode, ThreadingAware.class);

        JavacNode typeNode = annotationNode.up();
        switch (typeNode.getKind()) {
            case TYPE:
                addThreadingHandlerSupport(typeNode);
                return;
            default:
                annotationNode.addError("@ThreadingHandler is legal only on types.");
                return;
        }
    }

    private void addThreadingHandlerSupport(JavacNode typeNode) {
        HandlerUtils.TokenBuilder b = new HandlerUtils.TokenBuilder(typeNode);
        b.addInterface("griffon.core.ThreadingHandler", typeNode);

        TreeMaker m = typeNode.getTreeMaker();

        injectMethod(typeNode, defMethod("isUIThread")
                .returning(Boolean.TYPE)
                .withBody(bodyWithReturn("isUIThread", NIL_EXPRESSION, b))
                .$(typeNode));

        List<JCTree.JCVariableDecl> params = List.of(
                defVar(RUNNABLE_PARAM)
                        .modifiers(FINAL)
                        .type(Runnable.class)
                        .$(typeNode));
        List<JCTree.JCExpression> args = extractArgNames(params, m);
        injectMethod(typeNode, defMethod("execAsync")
                .withParams(params)
                .withBody(body("executeAsync", args, b))
                .$(typeNode));

        params = List.of(
                defVar(RUNNABLE_PARAM)
                        .modifiers(FINAL)
                        .type(Runnable.class)
                        .$(typeNode));
        args = extractArgNames(params, m);
        injectMethod(typeNode, defMethod("execSync")
                .withParams(params)
                .withBody(body("executeSync", args, b))
                .$(typeNode));

        params = List.of(
                defVar(RUNNABLE_PARAM)
                        .modifiers(FINAL)
                        .type(Runnable.class)
                        .$(typeNode));
        args = extractArgNames(params, m);
        injectMethod(typeNode, defMethod("execOutside")
                .withParams(params)
                .withBody(body("executeOutside", args, b))
                .$(typeNode));

        params = List.of(
                defVar(CLOSURE_PARAM)
                        .modifiers(FINAL)
                        .type(Closure.class)
                        .$(typeNode));
        args = extractArgNames(params, m);
        injectMethod(typeNode, defMethod(EXEC_FUTURE)
                .returning(Future.class)
                .withParams(params)
                .withBody(bodyWithReturn(EXECUTE_FUTURE, args, b))
                .$(typeNode));

        params = List.of(
                defVar(EXECUTOR_SERVICE_PARAM)
                        .modifiers(FINAL)
                        .type(ExecutorService.class)
                        .$(typeNode),
                defVar(CLOSURE_PARAM)
                        .modifiers(FINAL)
                        .type(Closure.class)
                        .$(typeNode));
        args = extractArgNames(params, m);
        injectMethod(typeNode, defMethod(EXEC_FUTURE)
                .returning(Future.class)
                .withParams(params)
                .withBody(bodyWithReturn(EXECUTE_FUTURE, args, b))
                .$(typeNode));

        params = List.of(
                defVar(CALLABLE_PARAM)
                        .modifiers(FINAL)
                        .type(Callable.class)
                        .$(typeNode));
        args = extractArgNames(params, m);
        injectMethod(typeNode, defMethod(EXEC_FUTURE)
                .returning(Future.class)
                .withParams(params)
                .withBody(bodyWithReturn(EXECUTE_FUTURE, args, b))
                .$(typeNode));

        params = List.of(
                defVar(EXECUTOR_SERVICE_PARAM)
                        .modifiers(FINAL)
                        .type(ExecutorService.class)
                        .$(typeNode),
                defVar(CALLABLE_PARAM)
                        .modifiers(FINAL)
                        .type(Callable.class)
                        .$(typeNode));
        args = extractArgNames(params, m);
        injectMethod(typeNode, defMethod(EXEC_FUTURE)
                .returning(Future.class)
                .withParams(params)
                .withBody(bodyWithReturn(EXECUTE_FUTURE, args, b))
                .$(typeNode));

        if (LOG.isDebugEnabled()) LOG.debug("Modified " + typeNode.getName() + " as a ThreadingHandler.");
    }

    private List<JCTree.JCStatement> body(String methodName, List<JCTree.JCExpression> args, HandlerUtils.TokenBuilder b) {
        return List.<JCTree.JCStatement>of(b.getTreeMaker().Exec(invokeThreadManager(methodName, args, b)));
    }

    private List<JCTree.JCStatement> bodyWithReturn(String methodName, List<JCTree.JCExpression> args, HandlerUtils.TokenBuilder b) {
        return List.<JCTree.JCStatement>of(b.getTreeMaker().Return(invokeThreadManager(methodName, args, b)));
    }

    private JCTree.JCExpression invokeThreadManager(String methodName, List<JCTree.JCExpression> args, HandlerUtils.TokenBuilder b) {
        return b.invoke(b.staticCallExpr("griffon.core.UIThreadManager", "getInstance"), methodName, args);
    }
}
