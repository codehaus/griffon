/*
 * Copyright 2009 the original author or authors.
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

import org.jdesktop.swingx.mapviewer.GeoPosition

/**
 * @author Andres Almiray
 */
class GeoPositionFactory extends AbstractFactory {
    private static final KEYS = [
       ["latitude", "longitud"],
       ["latDegrees", "latMinutes", "latSeconds", "lonDegrees", "lonMinutes", "lonSeconds"]
    ]

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        if(!value && attributes.empty) return new GeoPosition(0d, 0d)
        if(FactoryBuilderSupport.checkValueIsTypeNotString(value, name, GeoPosition)) {
            return value
        }

        def args = []
        Map params = [:]
        params.putAll(attributes)
        if(attributes.containsKey('coords')) {
            def coords = attributes.remove('coords')
            if(coords.size() != 2) {
                throw new IllegalArgumentException("In $name, attribute coords: must have 2 elements.")
            }
            args << toNumber(coords[0])
            args << toNumber(coords[1])
        } else {
            for(List keys in KEYS) {
               if(!keys.disjoint(attributes.keySet())) {
                   keys.each { key -> args << toNumber(attributes.remove(key) ?: 0d) }
                   break
               }
            } 
        }

        if(!args) {
            throw new IllegalArgumentException("In $name, cannot understand properties: ${params}. Provide one of the following: a double[] with 2 elements; latitude: and longitud:; or latDegrees:, latMinutes:, latSeconds:, lonDegrees:, lonMinutes, lonSeconds:")
        }
        return new GeoPosition(*args)
    }

    private toNumber(obj) {
        if(obj instanceof Number) obj
        Double.parseDouble(obj.toString())
    }
}
