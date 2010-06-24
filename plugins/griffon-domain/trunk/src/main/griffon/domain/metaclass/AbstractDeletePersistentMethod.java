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

import griffon.core.ArtifactInfo;
import griffon.domain.DomainHandler;

import groovy.lang.MissingMethodException;

/**
 * @author Andres Almiray
 */
public abstract class AbstractDeletePersistentMethod extends AbstractPersistentInstanceMethodInvocation {
    public AbstractDeletePersistentMethod(DomainHandler domainHandler) {
        super(domainHandler);
    }

    protected final Object invokeInternal(ArtifactInfo artifactInfo, Object target, String methodName, Object[] arguments) {
        if(target == null || (arguments != null && arguments.length > 0)) {
            throw new MissingMethodException(methodName, artifactInfo.getKlass(), arguments);
        }
        return delete(artifactInfo, target);
    }

    protected Object delete(ArtifactInfo artifactInfo, Object target) {
        return target;
    }
}