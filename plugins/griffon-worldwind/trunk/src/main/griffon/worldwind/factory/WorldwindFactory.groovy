/*
 * Copyright 2009-2010 the original author or authors.
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

package griffon.worldwind.factory

import groovy.swing.factory.ComponentFactory
import gov.nasa.worldwind.awt.WorldWindowGLCanvas
import gov.nasa.worldwind.WorldWind
import gov.nasa.worldwind.avlist.AVKey

/**
 * @author Andres Almiray
 */
class WorldwindFactory extends ComponentFactory {
    WorldwindFactory() {
        super(WorldWindowGLCanvas)
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        def canvas = super.newInstance(builder, name, value, attributes)
        if(!attributes.containsKey('model')) {
            canvas.model = WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME)
        }
        canvas
    }
}
