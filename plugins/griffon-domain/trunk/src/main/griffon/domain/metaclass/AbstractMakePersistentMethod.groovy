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

import griffon.core.ArtifactInfo
import griffon.domain.DomainHandler
import java.util.regex.Pattern

/**
 * @author Andres Almiray
 */
public abstract class AbstractMakePersistentMethod extends AbstractStaticPersistentMethod {
    public AbstractMakePersistentMethod(DomainHandler domainHandler) {
        super(domainHandler, Pattern.compile('^'+ DynamicMethod.MAKE.getMethodName() +'$'))
    }

    protected final Object doInvokeInternal(ArtifactInfo artifactInfo, Class clazz, String methodName, Object[] arguments) {
        if(!arguments) {
            return artifactInfo.newInstance()
        } else if(arguments[0] instanceof Map) {
            return make(artifactInfo, (Map) arguments[0])
        }
        throw new MissingMethodException(methodName, clazz, arguments)
    }

    protected Object make(ArtifactInfo artifactInfo, Map props) {
        def instance = artifactInfo.newInstance()
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
