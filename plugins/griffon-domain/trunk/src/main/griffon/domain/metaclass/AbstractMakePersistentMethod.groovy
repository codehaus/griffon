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
package griffon.domain.metaclass

import griffon.domain.GriffonDomainClass;
import org.codehaus.griffon.runtime.domain.DomainHandler;

/**
 * @author Andres Almiray
 */
public abstract class AbstractMakePersistentMethod extends AbstractPersistentStaticMethodInvocation {
    public AbstractMakePersistentMethod(DomainHandler domainHandler) {
        super(domainHandler)
    }

    protected final Object invokeInternal(GriffonDomainClass domainClass, String methodName, Object[] arguments) {
        if(!arguments) {
            return artifactInfo.newInstance()
        } else if(arguments[0] instanceof Map) {
            return make(artifactInfo, (Map) arguments[0])
        }
        throw new MissingMethodException(methodName, domainClass.getClazz(), arguments)
    }

    protected Object make(GriffonDomainClass domainClass, Map props) {
        def instance = domainClass.newInstance()
        props.each { k, v ->
            try {
                instance[k] = v
            } catch(MissingPropertyException mpe) {
                // ignore ??
            }
        }
        return instance
    }
}