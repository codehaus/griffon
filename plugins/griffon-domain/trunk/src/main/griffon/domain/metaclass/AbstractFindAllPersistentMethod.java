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
package griffon.domain.metaclass;

import griffon.domain.GriffonDomainClass;
import griffon.domain.DomainClassUtils;
import org.codehaus.griffon.runtime.domain.DomainHandler;
import griffon.domain.orm.Criterion;

import groovy.lang.Closure;
import groovy.lang.MissingMethodException;

import java.util.Map;
import java.util.Iterator;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Andres Almiray
 */
public abstract class AbstractFindAllPersistentMethod extends AbstractPersistentStaticMethodInvocation {
    public AbstractFindAllPersistentMethod(DomainHandler domainHandler) {
        super(domainHandler);
    }

    protected Object invokeInternal(GriffonDomainClass domainClass, String methodName, Object[] arguments) {
        if(arguments.length == 0) {
            return findAll(domainClass);
        }
        final Object arg = arguments[0];
        if(arg instanceof Criterion) {
            if(arguments.length == 1) {
                return findByCriterion(domainClass, (Criterion)arg, Collections.emptyMap());
            } else if(arguments[1] instanceof Map) {
                return findByCriterion(domainClass, (Criterion)arg, (Map) arguments[1]);
            }
        } else if(arg instanceof Closure) {
            return findByCriterion(domainClass, DomainClassUtils.getInstance().buildCriterion((Closure)arg), Collections.emptyMap());
        } else if(arg instanceof Map) {
            if(arguments.length == 1) {
                return findByProperties(domainClass, (Map) arg);
            } else if(arguments[1] instanceof Closure) {
                return findByCriterion(domainClass, DomainClassUtils.getInstance().buildCriterion((Closure)arguments[1]), (Map) arg);
            }
        } else if(domainClass.getClazz().isAssignableFrom(arg.getClass()) ) {
            return findByExample(domainClass, arg);
        }
        throw new MissingMethodException(methodName, domainClass.getClazz(), arguments);
    }

    protected Collection findAll(GriffonDomainClass domainClass) {
        return Collections.emptyList();
    }

    protected Collection findByProperties(GriffonDomainClass domainClass, Map properties) {
        return Collections.emptyList();
    }

    protected Collection findByExample(GriffonDomainClass domainClass, Object example) {
        return Collections.emptyList();
    }

    protected Collection findByCriterion(GriffonDomainClass domainClass, Criterion criterion, Map options) {
        return Collections.emptyList();
    }
}