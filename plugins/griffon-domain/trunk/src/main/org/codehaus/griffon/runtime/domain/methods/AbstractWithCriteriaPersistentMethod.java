/*
 * Copyright 2010 the original author or authors.
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
package org.codehaus.griffon.runtime.domain.methods;

import griffon.domain.DomainClassUtils;
import griffon.domain.GriffonDomain;
import griffon.domain.GriffonDomainClass;
import griffon.domain.GriffonDomainHandler;
import griffon.domain.methods.WithCriteriaMethod;
import griffon.domain.orm.Criterion;
import groovy.lang.Closure;
import groovy.lang.MissingMethodException;

import java.util.Collection;
import java.util.Collections;

/**
 * @author Andres Almiray
 */
public abstract class AbstractWithCriteriaPersistentMethod extends AbstractPersistentStaticMethodInvocation implements WithCriteriaMethod {
    public AbstractWithCriteriaPersistentMethod(GriffonDomainHandler griffonDomainHandler) {
        super(griffonDomainHandler);
    }

    @SuppressWarnings("unchecked")
    protected Object invokeInternal(GriffonDomainClass domainClass, String methodName, Object[] arguments) {
        if (arguments.length == 1) {
            final Object arg = arguments[0];
            if (arg instanceof Criterion) {
                return withCriteria(domainClass, (Criterion) arg);
            } else if (arg instanceof Closure) {
                return withCriteria(domainClass, DomainClassUtils.getInstance().buildCriterion((Closure) arg));
            }
        }
        throw new MissingMethodException(methodName, domainClass.getClazz(), arguments);
    }

    protected Collection<GriffonDomain> withCriteria(GriffonDomainClass domainClass, Criterion criterion) {
        return Collections.<GriffonDomain>emptyList();
    }
}