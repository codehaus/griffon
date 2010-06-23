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

import java.util.regex.Pattern;

/**
 * @author Andres Almiray
 */
public abstract class AbstractSavePersistentMethod extends AbstractDynamicPersistentMethod {
    public AbstractSavePersistentMethod(DomainHandler domainHandler) {
        super(domainHandler, Pattern.compile("^"+ DynamicMethod.SAVE.getMethodName() +"$"));
    }

    protected final Object doInvokeInternal(ArtifactInfo artifactInfo, Object target, String methodName, Object[] arguments) {
        if(target == null) {
            throw new MissingMethodException(methodName, artifactInfo.getKlass(), arguments);
        }
        if(shouldInsert(artifactInfo, target, arguments)) {
            return insert(artifactInfo, target, arguments);
        }
        return save(artifactInfo, target, arguments);
    }
   
    protected abstract boolean shouldInsert(ArtifactInfo artifactInfo, Object target, Object[] arguments);

    protected Object insert(ArtifactInfo artifactInfo, Object target, Object[] arguments) {
        return target;
    }

    protected Object save(ArtifactInfo artifactInfo, Object target, Object[] arguments) {
        return target;
    }
}
