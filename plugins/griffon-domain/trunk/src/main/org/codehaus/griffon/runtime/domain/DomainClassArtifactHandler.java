/*
 * Copyright 2010-2011 the original author or authors.
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

package org.codehaus.griffon.runtime.domain;

import griffon.core.GriffonApplication;
import griffon.core.GriffonClass;
import griffon.domain.GriffonDomain;
import griffon.domain.GriffonDomainClass;
import groovy.lang.Closure;
import org.codehaus.griffon.runtime.core.ArtifactHandlerAdapter;

/**
 * Handler for 'Domain' artifacts.
 *
 * @author Andres Almiray
 */
public class DomainClassArtifactHandler extends ArtifactHandlerAdapter {
    public DomainClassArtifactHandler(GriffonApplication app) {
        super(app, GriffonDomainClass.TYPE, GriffonDomainClass.TRAILING);
    }

    protected GriffonClass newGriffonClassInstance(Class clazz) {
        return new DefaultGriffonDomainClass(getApp(), clazz);
    }

    @SuppressWarnings("unchecked")
    public static boolean isDomainClass(Class clazz) {
        return clazz != null &&
                !Closure.class.isAssignableFrom(clazz) &&
                !Enum.class.isAssignableFrom(clazz) &&
                GriffonDomain.class.isAssignableFrom(clazz);
    }
}
