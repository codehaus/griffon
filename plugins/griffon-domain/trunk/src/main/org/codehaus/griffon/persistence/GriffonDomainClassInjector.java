/*
 * Copyright 2004-2010 the original author or authors.
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
package org.codehaus.griffon.persistence;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collection;

import java.io.File;
import java.lang.reflect.Modifier;

import griffon.domain.GriffonDomainClassProperty;
import griffon.domain.metaclass.MethodSignature;
import griffon.domain.metaclass.DefaultPersistentDynamicMethod;
import org.codehaus.griffon.runtime.domain.DomainHandler;

import org.codehaus.groovy.ast.*;
import org.codehaus.groovy.ast.expr.*;
import org.codehaus.groovy.ast.stmt.*;
import org.codehaus.groovy.classgen.GeneratorContext;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.griffon.ast.GriffonASTUtils;

import org.codehaus.groovy.runtime.DefaultGroovyMethods;

/**
 * Base contract for a domain class injector that adds persistence
 * specific properties and other boilerplate code.
 *
 * @author Graeme Rocher (Grails 0.2)
 */
public abstract class GriffonDomainClassInjector {
    private static final int ACC_SYNTHETIC = 4096;
    private static final String DOMAIN_DIR = "domain";
    private static final String GRIFFON_APP_DIR = "griffon-app";
    protected ClassNode dwClassNode = new ClassNode(DomainHandler.class);
    protected static final String DOMAIN_HANDLER_METHOD_NAME = "getDomainHandler";

/*
    public void performInjection(SourceUnit source, ClassNode classNode) {
        performInjection(source, null, classNode);
    }

    public void performInjection(SourceUnit source, GeneratorContext context, ClassNode classNode) {
        if (isDomainClass(classNode, source) && shouldInjectClass(classNode)) {
            performInjectionOnAnnotatedEntity(classNode);
        }
    }
*/

    public void performInjectionOnAnnotatedEntity(ClassNode classNode) {
        GriffonASTUtils.injectConstant(classNode, GriffonDomainClassProperty.GRIFFON_DOMAIN_MAPPING, String.class, getMappingValue());
        injectDomainHandler(classNode);
        injectMethods(classNode);
        performInjection(classNode);
    }

    protected abstract MethodSignature[] getProvidedMethods();
    protected abstract String getMappingValue(); 
    protected abstract Class getDomainHandlerClass();
    protected abstract Class getDomainHandlerHolderClass();
    protected abstract void performInjection(ClassNode classNode);

//    public boolean isDomainClass(@SuppressWarnings("unused") ClassNode classNode, SourceUnit sourceNode) {
//        String sourcePath = sourceNode.getName();
//        File sourceFile = new File(sourcePath);
//        File parent = sourceFile.getParentFile();
//        while (parent != null) {
//            File parentParent = parent.getParentFile();
//            if (parent.getName().equals(DOMAIN_DIR) && parentParent != null &&
//                    parentParent.getName().equals(GRIFFON_APP_DIR)) {
//                return true;
//            }
//            parent = parentParent;
//        }
//
//        return false;
//    }

    protected boolean shouldInjectClass(ClassNode classNode) {
        return !GriffonASTUtils.isEnum(classNode);
    }

    protected void injectMethods(ClassNode classNode) {
        for(MethodSignature methodSignature: getProvidedMethods()) {
            injectMethod(classNode, methodSignature);
        }
    }

    protected void injectMethod(ClassNode classNode, MethodSignature methodSignature) {
        Parameter[] parameters = makeParameters(methodSignature.getParameterTypes());

        int modifiers = Modifier.PUBLIC;
        if(methodSignature.isStatic()) modifiers |= Modifier.STATIC;
        GriffonASTUtils.addMethod(classNode, new MethodNode(
            methodSignature.getMethodName(),
            modifiers,
            ClassHelper.makeWithoutCaching(methodSignature.getReturnType()),
            parameters,
            ClassNode.EMPTY_ARRAY,
            makeMethodBody(classNode, methodSignature, parameters)
        ));
    }

    protected Parameter[] makeParameters(Class[] parameterTypes) {
        if(parameterTypes.length == 0) return Parameter.EMPTY_ARRAY;
        Parameter[] parameters = new Parameter[parameterTypes.length];
        for(int i = 0; i < parameterTypes.length; i++) {
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
        for(Parameter parameter: parameters) {
            args[i++] = new VariableExpression(parameter.getName());
        }
       
        return new ReturnStatement(
                   new ExpressionStatement(
                       new MethodCallExpression(
                           getDomainHandlerMethod(classNode),
                           invokeMethod,
                           new ArgumentListExpression(args))));
    }

    protected void injectDomainHandler(ClassNode classNode) {
        GriffonASTUtils.addMethod(classNode, new MethodNode(
            DOMAIN_HANDLER_METHOD_NAME,
            Modifier.PRIVATE | Modifier.STATIC | ACC_SYNTHETIC,
            ClassHelper.makeWithoutCaching(getDomainHandlerClass()),
            Parameter.EMPTY_ARRAY,
            ClassNode.EMPTY_ARRAY,
            new ReturnStatement(
                new ExpressionStatement(
                    new StaticMethodCallExpression(
                        ClassHelper.makeWithoutCaching(getDomainHandlerHolderClass()),
                        "getDomainHandler",
                        ArgumentListExpression.EMPTY_ARGUMENTS))))
        );
    }

    protected MethodCallExpression getDomainHandlerMethod(ClassNode classNode) {
        return new MethodCallExpression(
            VariableExpression.THIS_EXPRESSION,
            DOMAIN_HANDLER_METHOD_NAME,
            ArgumentListExpression.EMPTY_ARGUMENTS
        );
    }

    public static MethodSignature[] allDefaultPersistentMethodSignatures() {
        Collection<MethodSignature> signatures = new ArrayList<MethodSignature>();
        for(DefaultPersistentDynamicMethod dynaMethod: DefaultPersistentDynamicMethod.allMethods()) {
            signatures = DefaultGroovyMethods.plus(
                signatures,
                Arrays.asList(dynaMethod.getMethodSignatures())
            );
        }
        return (MethodSignature[]) signatures.toArray(new MethodSignature[signatures.size()]);
    }
}