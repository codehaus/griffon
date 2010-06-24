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

import griffon.core.ArtifactInfo;
import griffon.domain.DomainHandler;
import griffon.domain.DomainClassUtils;

/**
 * @author Andres Almiray
 */
public abstract class AbstractPersistentStaticDynamicMethodInvocation 
                      extends AbstractStaticDynamicMethodInvocation
                      implements PersistentMethodInvocation {
    private final DomainHandler domainHandler;
    
    public AbstractPersistentStaticDynamicMethodInvocation(DomainHandler domainHandler, Pattern pattern) {
        super(pattern);
        this.domainHandler = domainHandler;
    }

    public DomainHandler getDomainHandler() {
        return domainHandler;
    }

    public ArtifactInfo getArtifactInfoFor(Class clazz) {
        return DomainClassUtils.getInstance().getArtifactInfoFor(clazz);
    }

    public final Object invoke(Class clazz, String methodName, Object[] arguments) {
	    return invokeInternal(getArtifactInfoFor(clazz), clazz, methodName, arguments);
	}
	
    protected abstract Object invokeInternal(ArtifactInfo artifactInfo, Class clazz, String methodName, Object[] arguments);
}