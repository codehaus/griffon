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

import griffon.domain.GriffonDomain;
import griffon.domain.GriffonDomainClass;
import org.codehaus.griffon.runtime.domain.DomainHandler;

import groovy.lang.MissingMethodException;

import java.util.Map;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Andres Almiray
 */
public abstract class AbstractListPersistentMethod extends AbstractPersistentStaticMethodInvocation {
    public AbstractListPersistentMethod(DomainHandler domainHandler) {
        super(domainHandler);
    }

    protected final Object invokeInternal(GriffonDomainClass domainClass, String methodName, Object[] arguments) {
        if(arguments.length == 0) {
            return list(domainClass);
        } else if(arguments[0] instanceof Map) {
            return list(domainClass, (Map) arguments[0]);
        }
        throw new MissingMethodException(methodName, domainClass.getClazz(), arguments);
    }

    protected Collection<GriffonDomain> list(GriffonDomainClass domainClass) {
        return Collections.<GriffonDomain>emptyList();
    }

    protected Collection<GriffonDomain> list(GriffonDomainClass domainClass, Map props) {
        return Collections.<GriffonDomain>emptyList();
    }
}