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
import org.codehaus.griffon.runtime.domain.DomainHandler;

import groovy.lang.MissingMethodException;

/**
 * @author Andres Almiray
 */
public abstract class AbstractSavePersistentMethod extends AbstractPersistentInstanceMethodInvocation {
    public AbstractSavePersistentMethod(DomainHandler domainHandler) {
        super(domainHandler);
    }

    protected final Object invokeInternal(GriffonDomainClass domainClass, Object target, String methodName, Object[] arguments) {
        if(target == null) {
            throw new MissingMethodException(methodName, domainClass.getClazz(), arguments);
        }
        if(shouldInsert(domainClass, target, arguments)) {
            return insert(domainClass, target, arguments);
        }
        return save(domainClass, target, arguments);
    }
   
    protected abstract boolean shouldInsert(GriffonDomainClass domainClass, Object target, Object[] arguments);

    protected Object insert(GriffonDomainClass domainClass, Object target, Object[] arguments) {
        return target;
    }

    protected Object save(GriffonDomainClass domainClass, Object target, Object[] arguments) {
        return target;
    }
}