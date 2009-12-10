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

package griffon.builder.swingx

import org.jdesktop.swingx.JXMapKit
import org.jdesktop.swingx.JXMapViewer
import org.jdesktop.swingx.mapviewer.Waypoint
import org.jdesktop.swingx.mapviewer.WaypointPainter
import org.jdesktop.swingx.painter.Painter
import org.jdesktop.swingx.painter.CompoundPainter

/**
 * @author Andres Almiray
 */
class WaypointUtils {
    static WaypointPainter findWaypointPainter(target, boolean override = false) {
        if(!target) return null
        def waypointPainter = null
        def overlayPainter = null
        if(target instanceof JXMapKit) target = target.mainMap
        if(target instanceof JXMapViewer) overlayPainter = target.overlayPainter
        if(!overlayPainter) return null
        if(overlayPainter instanceof WaypointPainter) {
            waypointPainter = overlayPainter
            if(override) {
                waypointPainter = new WaypointPainter()
                copyWaypoints(overlayPainter, waypointPainter)
                target.overlayPainter = waypointPainter
            }
        } else if(overlayPainter instanceof CompoundPainter) {
            waypointPainter = overlayPainter.painters.find{ it instanceof WaypointPainter }
            if(!waypointPainter) {
                waypointPainter = updatePainters(overlayPainter) { painters ->
                   new WaypointPainter()
                }
            } else if(override) {
                waypointPainter = updatePainters(overlayPainter) { painters ->
                   painters.remove(waypointPainter) 
                   def wp = new WaypointPainter()
                   copyWaypoints(waypointPainter, wp)
                   wp
                }
            }
        } else {
            waypointPainter = new WaypointPainter()
            target.overlayPainter = new CompoundPainter(overlayPainter, waypointPainter)
        }
        waypointPainter 
    }

    private WaypointPainter updatePainters(CompoundPainter painter, Closure closure) {
        def newPainters = []
        newPainters.addAll(painter.painters.toList())
        def waypointPainter = closure(newPainters)
        newPainters << waypointPainter
        painter.painters = newPainters
        return waypointPainter
    }

    private void copyWaypoints(WaypointPainter src, WaypointPainter dest) {
        Set<Waypoint> waypoints = new HashSet<Waypoint>()
        waypoints.addAll(src.waypoints)
        dest.waypoints = waypoints
    }

    private void addWaypoint(WaypointPainter waypointPainter, Waypoint waypoint) {
        Set<Waypoint> waypoints = new HashSet<Waypoint>()
        waypoints.addAll(waypointPainter.waypoints)
        waypoints << waypoint
        waypointPainter.waypoints = waypoints
    }
}
