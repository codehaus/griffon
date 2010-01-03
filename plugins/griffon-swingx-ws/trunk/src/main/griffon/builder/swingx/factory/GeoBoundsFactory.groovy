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

import org.jdesktop.swingx.mapviewer.GeoBounds

/**
 * @author Andres Almiray
 */
class GeoBoundsFactory extends AbstractFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        if(FactoryBuilderSupport.checkValueIsTypeNotString(value, name, GeoBounds)) {
            return value
        }

        def minLat = toNumber(attributes.remove("minLat") ?: 0d)
        def minLng = toNumber(attributes.remove("minLng") ?: 0d)
        def maxLat = toNumber(attributes.remove("maxLat") ?: 0d)
        def maxLng = toNumber(attributes.remove("maxLng") ?: 0d)
        new GeoBounds(minLat.doubleValue(), minLong.doubleValue(), maxLat.doubleValue(), maxLng.doubleValue())
    }

    private toNumber(obj) {
        if(obj instanceof Number) obj
        Double.parseDouble(obj.toString())
    }
}
