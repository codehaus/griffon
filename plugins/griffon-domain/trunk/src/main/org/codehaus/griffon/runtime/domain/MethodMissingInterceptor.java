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

package org.codehaus.griffon.runtime.domain;

import griffon.core.GriffonClass;
import griffon.domain.GriffonDomainHandler;
import griffon.domain.methods.CountByMethod;
import griffon.domain.methods.FindAllByMethod;
import griffon.domain.methods.FindByMethod;
import griffon.domain.methods.MethodSignature;
import griffon.util.ApplicationHolder;
import groovy.lang.Closure;
import groovy.lang.ExpandoMetaClass;
import groovy.lang.MetaMethod;
import groovy.lang.MissingMethodException;
import org.codehaus.groovy.runtime.metaclass.ClosureStaticMetaMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;

/**
 * @author Andres Almiray
 */
public class MethodMissingInterceptor {
    private static final Logger LOG = LoggerFactory.getLogger(MethodMissingInterceptor.class);
    private final Class clazz;
    private final GriffonDomainHandler domainHandler;
    private final List<String> supportedDynamicMethods = new ArrayList<String>();

    public MethodMissingInterceptor(Class clazz, GriffonDomainHandler domainHandler) {
        this.clazz = clazz;
        this.domainHandler = domainHandler;

        for (String methodName : asList(FindByMethod.METHOD_NAME, FindAllByMethod.METHOD_NAME, CountByMethod.METHOD_NAME)) {
            inner:
            for (MethodSignature methodSignature : domainHandler.getMethodSignatures()) {
                if (methodName.equals(methodSignature.getMethodName())) {
                    supportedDynamicMethods.add(methodName);
                    break inner;
                }
            }
        }
    }

    public Object handleMethodMissing(String methodName, Object[] arguments) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Handling missing method " + clazz.getName() + "." + methodName);
        }
        for (String dynaMethodName : supportedDynamicMethods) {
            if (methodName.startsWith(dynaMethodName)) {
                Object[] adjustedArguments = new Object[arguments.length + 1];
                adjustedArguments[0] = methodName.substring(dynaMethodName.length());
                System.arraycopy(arguments, 0, adjustedArguments, 1, arguments.length);

                MetaMethod method = methodFor(methodName, dynaMethodName);
                Object returnValue = null;
                try {
                    returnValue = method.invoke(clazz, adjustedArguments);
                } catch (MissingMethodException mme) {
                    if (mme.getMethod().equals(dynaMethodName)) {
                        throw new MissingMethodException(methodName, clazz, arguments, true);
                    }
                }

                // cache method
                GriffonClass griffonClass = ApplicationHolder.getApplication().getArtifactManager().findGriffonClass(clazz);
                ExpandoMetaClass metaClass = (ExpandoMetaClass) griffonClass.getMetaClass();
                ExpandoMetaClass sourceMetaClass = new ExpandoMetaClass(clazz, true, true);
                sourceMetaClass.addMetaMethod(method);
                Set<ExpandoMetaClass> expandos = new HashSet<ExpandoMetaClass>();
                expandos.add(sourceMetaClass);
                metaClass.refreshInheritedMethods(expandos);

                if (LOG.isDebugEnabled()) {
                    LOG.debug("Caching missing method " + clazz.getName() + "." + methodName);
                }

                return returnValue;
            }
        }
        throw new MissingMethodException(methodName, clazz, arguments, true);
    }

    private MetaMethod methodFor(final String methodName, final String dynaMethodName) {
        return new ClosureStaticMetaMethod(methodName, clazz, new Closure<Object>(this) {
            protected Object doCall(Object[] args) {
                try {
                    return domainHandler.invokeStatic(clazz, dynaMethodName, args);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }
}