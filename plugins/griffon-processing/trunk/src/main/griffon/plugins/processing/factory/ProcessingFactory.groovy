/*
 * Copyright 2010 the original author or authors.
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

package griffon.plugins.processing.factory

import processing.core.PApplet

/**
 * @author Andres Almiray
 */
class ProcessingFactory extends AbstractFactory {
    boolean isLeaf() {
        return true
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        PApplet applet
        if(FactoryBuilderSupport.checkValueIsTypeNotString(value, name, PApplet)) {
            applet = value
        }
        if(attributes.containsKey('source')) {
            applet = attributes.remove('source') 
        }
        if(!applet) {
            throw new IllegalArgumentException("In $name you must define a node value or a property source: of type ${PApplet.class.name}")
        }
        
        if(!attributes.containsKey('init') || attributes.remove('init')) applet.init()
        
        return applet
    }
}
