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

import griffon.core.GriffonApplication;
import griffon.core.ArtifactInfo;
import java.util.regex.Pattern;

/**
 * Abstract base class for static persistent methods
 * 
 * @author Steven Devijver
 * @author Graeme Rocher
 * 
 * @since Aug 8, 2005
 */
public abstract class AbstractStaticPersistentMethod extends
        AbstractStaticMethodInvocation {
    private final GriffonApplication app;
    private final ArtifactInfo domainClass;

    public AbstractStaticPersistentMethod(GriffonApplication app, ArtifactInfo domainClass, Pattern pattern) {
        super(pattern);
        this.app = app;
        this.domainClass = domainClass;
    }

    public GriffonApplication getApp() {
        return app;
    }

    public ArtifactInfo getDomainClass() {
        return domainClass;
    }

    public final Object invoke(Class clazz, String methodName, Object[] arguments) {
        return doInvokeInternal(clazz, methodName, arguments);
    }

    protected abstract Object doInvokeInternal(Class clazz, String methodName, Object[] arguments);
}
