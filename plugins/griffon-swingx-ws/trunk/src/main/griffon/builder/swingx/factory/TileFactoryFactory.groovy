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

package griffon.builder.swingx.factory

import org.jdesktop.swingx.JXMapKit
import org.jdesktop.swingx.JXMapViewer
import org.jdesktop.swingx.mapviewer.TileFactory
import org.jdesktop.swingx.mapviewer.TileFactoryInfo
import org.jdesktop.swingx.mapviewer.DefaultTileFactory

/**
 * @author Andres Almiray
 */
class TileFactoryFactory extends AbstractFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        if(FactoryBuilderSupport.checkValueIsTypeNotString(value, name, TileFactory)) {
            return value
        }

        def tileFactoryInfo = attributes.remove('tileFactoryInfo')
        if(!tileFactoryInfo &&
            (!TileFactoryInfoFactory.KEYS[0].disjoint(attributes.keySet()) ||
             !TileFactoryInfoFactory.KEYS[1].disjoint(attributes.keySet()) ||
             !TileFactoryInfoFactory.KEYS[2].disjoint(attributes.keySet()))) {
            tileFactoryInfo = new TileFactoryInfoFactory().newInstance(builder, name, value, attributes)
        }
        if(!tileFactoryInfo) {
            throw new IllegalArgumentException("In $name you must define a value for tileFactoryInfo:")
        }

        new DefaultTileFactory(tileFactoryInfo)
    }

    public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if(parent instanceof JXMapKit || parent instanceof JXMapViewer) {
            parent.tileFactory = child
        }
    }
}
