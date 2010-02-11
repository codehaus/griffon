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

import griffon.core.GriffonApplication;
import griffon.core.ArtifactInfo;
import griffon.domain.DynamicMethod;

import groovy.lang.MissingMethodException;

import java.util.regex.Pattern;

/**
 * @author Andres Almiray
 */
public abstract class AbstractCountPersistentMethod extends AbstractStaticPersistentMethod {
    public AbstractCountPersistentMethod(GriffonApplication app, ArtifactInfo domainClass) {
        super(app, domainClass, Pattern.compile("^"+ DynamicMethod.COUNT.getMethodName() +"$"));
    }

    protected final Object doInvokeInternal(Class clazz, String methodName, Object[] arguments) {
        if(arguments.length == 0) {
            return count(clazz);
        }
        throw new MissingMethodException(methodName, clazz, arguments);
    }

    protected int count(Class clazz) {
        return 0;
    }
}
