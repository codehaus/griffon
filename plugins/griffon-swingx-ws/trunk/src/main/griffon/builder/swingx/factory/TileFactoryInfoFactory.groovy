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

import org.jdesktop.swingx.mapviewer.TileFactoryInfo

/**
 * @author Andres Almiray
 */
class TileFactoryInfoFactory extends AbstractFactory {
    public static final KEYS = [
        ['minimumZoomLevel', 'maximumZoomLevel', 'totalMapZoom', 'tileSize'], 
        ['xr2l', 'yt2b'], 
        ['baseURL', 'xparam', 'yparam', 'zparam']
    ]
    private static final DEFAULTS = [
        minimumZoomLevel: 1, 
        maximumZoomLevel: 15, 
        totalMapZoom: 17, 
        tileSize: 256, 
        xparam: 'x', 
        yparam: 'y', 
        zparam: 'z', 
    ]

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        if(FactoryBuilderSupport.checkValueIsTypeNotString(value, name, TileFactoryInfo)) {
            return value
        }

        def args = []
        Map params = [:]
        params.putAll(attributes)

        if(attributes.containsKey('name')) {
            args << attributes.remove('name')
        }
        KEYS[0].each { key -> args << toNumber(attributes.remove(key) ?: DEFAULTS.get(key, 1d)) }
        KEYS[1].each { key -> args << toBoolean(attributes.remove(key) ?: true) }
        KEYS[2].each { key -> args << toString(attributes.remove(key) ?: DEFAULTS.get(key, '')) }

        if(!args) {
            throw new IllegalArgumentException("In $name, cannot understand properties: ${params}. ")
        }
        return new TileFactoryInfo(*args)
    }

    private toNumber(obj) {
        if(obj instanceof Number) obj.intValue()
        Double.parseDouble(obj.toString()).intValue()
    }

    private toBoolean(obj) {
        if(obj instanceof Boolean) obj.booleanValue()
        Boolean.valueOf(obj.toString()).booleanValue()
    }

    private toString(obj) {
        if(obj instanceof String) obj
        obj.toString()
    }
}
