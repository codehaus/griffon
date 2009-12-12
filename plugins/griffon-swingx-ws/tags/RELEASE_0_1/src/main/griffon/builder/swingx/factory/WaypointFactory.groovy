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

import griffon.builder.swingx.WaypointUtils
import org.jdesktop.swingx.JXMapKit
import org.jdesktop.swingx.JXMapViewer
import org.jdesktop.swingx.mapviewer.GeoPosition
import org.jdesktop.swingx.mapviewer.Waypoint

/**
 * @author Andres Almiray
 */
class WaypointFactory extends AbstractFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        if(FactoryBuilderSupport.checkValueIsTypeNotString(value, name, Waypoint)) {
            return value
        }

        def latitude = toNumber(attributes.remove('latitude') ?: 0d)
        def longitude = toNumber(attributes.remove('longitude') ?: 0d)
        new Waypoint(latitude, longitude)
    }

    public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if(parent instanceof JXMapKit) parent = parent.getMainMap()
        if(parent instanceof JXMapViewer) {
            def override = builder.parentContext?.override
            override = override != null ? override : true
            def waypointPainter = WaypointUtils.findWaypointPainter(parent, override)
            builder.parentContext.override = false
            WaypointUtils.addWaypoint(waypointPainter, child)
        }
    }

    private toNumber(obj) {
        if(obj instanceof Number) obj
        Double.parseDouble(obj.toString())
    }
}
