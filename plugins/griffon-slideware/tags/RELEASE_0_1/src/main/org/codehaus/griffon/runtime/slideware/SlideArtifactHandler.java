/*
 * Copyright 2009-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.codehaus.griffon.runtime.slideware;

import griffon.core.GriffonApplication;
import griffon.core.GriffonClass;
import griffon.plugins.slideware.GriffonSlideClass;
import org.codehaus.griffon.runtime.core.ArtifactHandlerAdapter;

/**
 * Handler for 'Slide' artifacts.
 *
 * @author Andres Almiray
 */
public class SlideArtifactHandler extends ArtifactHandlerAdapter {
    public SlideArtifactHandler(GriffonApplication app) {
        super(app, GriffonSlideClass.TYPE, GriffonSlideClass.TRAILING);
    }

    protected GriffonClass newGriffonClassInstance(Class clazz) {
        return new DefaultGriffonSlideClass(getApp(), clazz);
    }
}
