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

import java.util.regex.Pattern;

import griffon.core.ArtifactManager;
import griffon.core.GriffonClass;
import griffon.domain.GriffonDomainClass;
import org.codehaus.griffon.runtime.domain.DomainHandler;

/**
 * @author Andres Almiray
 */
public abstract class AbstractPersistentInstanceDynamicMethodInvocation 
                      extends AbstractInstanceDynamicMethodInvocation
                      implements PersistentMethodInvocation {
    private final DomainHandler domainHandler;
    
    public AbstractPersistentInstanceDynamicMethodInvocation(DomainHandler domainHandler, Pattern pattern) {
        super(pattern);
        this.domainHandler = domainHandler;
    }

    public DomainHandler getDomainHandler() {
        return domainHandler;
    }

    public GriffonDomainClass getDomainClassFor(Class clazz) {
        GriffonClass griffonClass = ArtifactManager.getInstance().findGriffonClass(clazz);
        if(griffonClass instanceof GriffonDomainClass) return (GriffonDomainClass) griffonClass;
        throw new RuntimeException("Class "+ clazz.getName() + " is not a domain class.");
    }

    public final Object invoke(Object target, String methodName, Object[] arguments) {
	    return invokeInternal(getDomainClassFor(target.getClass()), target, methodName, arguments);
	}
	
    protected abstract Object invokeInternal(GriffonDomainClass domainClass, Object target, String methodName, Object[] arguments);
}