/*
 * Copyright 2010-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codehaus.griffon.compiler.support;

import griffon.domain.GriffonDomain;
import griffon.domain.GriffonDomainHandler;
import griffon.domain.methods.DefaultPersistentMethods;
import griffon.domain.methods.MethodSignature;
import org.codehaus.griffon.runtime.domain.MethodMissingInterceptor;
import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.Statement;
import org.codehaus.groovy.runtime.DefaultGroovyMethods;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.codehaus.griffon.ast.GriffonASTUtils.*;

/**
 * Base contract for a domain class injector that adds persistence
 * specific properties and other boilerplate code.
 *
 * @author Andres Almiray
 */
public abstract class GriffonDomainClassInjector {
    private static final int ACC_SYNTHETIC = 4096;
    private static final String GRIFFON_DOMAIN_CLASSNAME = GriffonDomain.class.getName();
    private final ClassNode GRIFFON_DOMAIN_HANDLER_CLASS = new ClassNode(GriffonDomainHandler.class);
    private final ClassNode METHOD_MISSING_INTERCEPTOR_CLASS = new ClassNode(MethodMissingInterceptor.class);
    protected static final String DOMAIN_HANDLER_METHOD_NAME = "domainHandler";
    protected static final String MAPPING = "mapping";
    protected static final String DATASOURCE = "datasource";

    public void performInjectionOn(ClassNode classNode, String implementation, String datasource) {
        injectDomainHandler(classNode, implementation, datasource);
        injectMethodMissing(classNode);
        injectMethods(classNode);
        performInjection(classNode);
    }

    protected abstract MethodSignature[] getProvidedMethods();

    protected abstract ClassNode getDomainHandlerClass();

    protected abstract void performInjection(ClassNode classNode);

    protected void injectMethods(ClassNode classNode) {
        for (MethodSignature methodSignature : getProvidedMethods()) {
            injectMethod(classNode, methodSignature);
        }
    }

    protected void injectMethod(ClassNode classNode, MethodSignature methodSignature) {
        Parameter[] parameters = makeParameters(methodSignature.getParameterTypes());

        int modifiers = Modifier.PUBLIC;
        if (methodSignature.isStatic()) modifiers |= Modifier.STATIC;
        String returnTypeClassName = methodSignature.getReturnType().getName();
        ClassNode returnType = GRIFFON_DOMAIN_CLASSNAME.equals(returnTypeClassName) ? classNode : ClassHelper.makeWithoutCaching(methodSignature.getReturnType());
        classNode.addMethod(new MethodNode(
                methodSignature.getMethodName(),
                modifiers,
                returnType,
                parameters,
                ClassNode.EMPTY_ARRAY,
                makeMethodBody(classNode, methodSignature, parameters)
        ));
    }

    protected Parameter[] makeParameters(Class[] parameterTypes) {
        if (parameterTypes.length == 0) return Parameter.EMPTY_ARRAY;
        Parameter[] parameters = new Parameter[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            parameters[i] = new Parameter(ClassHelper.makeWithoutCaching(parameterTypes[i]), "arg" + i);
        }
        return parameters;
    }

    private Statement makeMethodBody(ClassNode classNode, MethodSignature methodSignature, Parameter[] parameters) {
        String invokeMethod = methodSignature.isStatic() ? "invokeStatic" : "invokeInstance";
        Expression[] args = new Expression[parameters.length + 2];
        args[0] = methodSignature.isStatic() ? new ClassExpression(classNode) : VariableExpression.THIS_EXPRESSION;
        args[1] = new ConstantExpression(methodSignature.getMethodName());
        int i = 2;
        for (Parameter parameter : parameters) {
            args[i++] = new VariableExpression(parameter.getName());
        }

        return returns(call(
                getDomainHandlerMethod(classNode),
                invokeMethod,
                args(args)));
    }

    protected void injectDomainHandler(ClassNode classNode, String implementation, String datasource) {
        classNode.addMethod(new MethodNode(
                DOMAIN_HANDLER_METHOD_NAME,
                Modifier.PUBLIC | Modifier.STATIC,
                GRIFFON_DOMAIN_HANDLER_CLASS,
                Parameter.EMPTY_ARRAY,
                ClassNode.EMPTY_ARRAY,
                returns(domainHandlerInstance())
        ));

        classNode.addMethod(new MethodNode(
                MAPPING,
                Modifier.PUBLIC | Modifier.STATIC,
                ClassHelper.STRING_TYPE,
                Parameter.EMPTY_ARRAY,
                ClassNode.EMPTY_ARRAY,
                returns(constx(implementation))
        ));

        classNode.addMethod(new MethodNode(
                DATASOURCE,
                Modifier.PUBLIC | Modifier.STATIC,
                ClassHelper.STRING_TYPE,
                Parameter.EMPTY_ARRAY,
                ClassNode.EMPTY_ARRAY,
                returns(constx(datasource))
        ));
    }

    protected void injectMethodMissing(ClassNode classNode) {
        FieldNode methodMissingInterceptor = classNode.addField(
                "this$methodMissingInterceptor",
                Modifier.FINAL | Modifier.STATIC | Modifier.PRIVATE | ACC_SYNTHETIC,
                METHOD_MISSING_INTERCEPTOR_CLASS,
                ctor(METHOD_MISSING_INTERCEPTOR_CLASS,
                        args(classx(classNode), domainHandlerInstance())));

        classNode.addMethod(new MethodNode(
                "$static_methodMissing",
                Modifier.PUBLIC | Modifier.STATIC,
                ClassHelper.OBJECT_TYPE,
                params(
                        param(ClassHelper.STRING_TYPE, "methodName"),
                        param(ClassHelper.makeWithoutCaching(Object[].class), "arguments")
                ),
                ClassNode.EMPTY_ARRAY,
                returns(
                        call(field(methodMissingInterceptor),
                                "handleMethodMissing",
                                args(var("methodName"), var("arguments")))
                )
        ));
    }

    protected MethodCallExpression getDomainHandlerMethod(ClassNode classNode) {
        return new MethodCallExpression(
                VariableExpression.THIS_EXPRESSION,
                DOMAIN_HANDLER_METHOD_NAME,
                NO_ARGS
        );
    }

    public static MethodSignature[] allDefaultPersistentMethodSignatures() {
        Collection<MethodSignature> signatures = new ArrayList<MethodSignature>();
        for (DefaultPersistentMethods method : DefaultPersistentMethods.values()) {
            signatures = DefaultGroovyMethods.plus(
                    signatures,
                    Arrays.asList(method.getMethodSignatures())
            );
        }
        return signatures.toArray(new MethodSignature[signatures.size()]);
    }

    protected Expression domainHandlerInstance() {
        return call(getDomainHandlerClass(), "getInstance", NO_ARGS);
    }
}
