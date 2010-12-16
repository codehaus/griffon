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
package org.codehaus.griffon.runtime.camel;

import griffon.core.GriffonApplication;
import griffon.core.GriffonClass;
import griffon.plugins.camel.GriffonRouteClass;
import org.codehaus.griffon.runtime.core.SpringArtifactHandlerAdapter;

/**
 * @author Andres Almiray
 */
public class RouteArtifactHandler extends SpringArtifactHandlerAdapter {
    public RouteArtifactHandler(GriffonApplication app) {
        super(app, GriffonRouteClass.TYPE, GriffonRouteClass.TRAILING);
    }

    protected GriffonClass newGriffonClassInstance(Class clazz) {
        return new DefaultGriffonRouteClass(getApp(), clazz);
    }

    public boolean isArtifactClass(Class clazz) {
        // class shouldn't be null and shoud ends with Route suffix
        if(clazz == null || !clazz.getName().endsWith(GriffonRouteClass.TRAILING)) return false;
        return false;
    }
}