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
package griffon.domain.metaclass;

import griffon.core.ArtifactInfo;
import griffon.domain.DomainHandler;
import java.util.regex.Pattern;

/**
 * Abstract base class for dynamic persistent methods
 */
public abstract class AbstractDynamicPersistentMethod
                      extends AbstractPersistentMethodInvocation
                      implements DynamicMethodInvocation {
    public AbstractDynamicPersistentMethod(DomainHandler domainHandler, Pattern pattern) {
        super(domainHandler, pattern);
    }

    public final Object invoke(Object target, String methodName, Object[] arguments) {
        return doInvokeInternal(getArtifactInfoFor(target.getClass()), target, methodName, arguments);
    }

    protected abstract Object doInvokeInternal(ArtifactInfo artifactInfo, Object target, String methodName, Object[] arguments);
}
