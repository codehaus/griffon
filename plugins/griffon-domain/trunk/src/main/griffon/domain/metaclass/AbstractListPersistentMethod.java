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

import java.util.Map;
import java.util.Collection;
import java.util.Collections;
import java.util.regex.Pattern;

/**
 * @author Andres Almiray
 */
public abstract class AbstractListPersistentMethod extends AbstractStaticPersistentMethod {
    public AbstractListPersistentMethod(DomainHandler domainHandler) {
        super(domainHandler, Pattern.compile("^"+ DynamicMethod.LIST.getMethodName() +"$"));
    }

    protected final Object doInvokeInternal(ArtifactInfo artifactInfo, Class clazz, String methodName, Object[] arguments) {
        if(arguments.length == 0) {
            return list(artifactInfo, clazz);
        } else if(arguments[0] instanceof Map) {
            return list(artifactInfo, (Map) arguments[0]);
        }
        throw new MissingMethodException(methodName, clazz, arguments);
    }

    protected Collection list(ArtifactInfo artifactInfo, Class clazz) {
        return Collections.emptyList();
    }

    protected Collection list(ArtifactInfo artifactInfo, Map props) {
        return Collections.emptyList();
    }
}
