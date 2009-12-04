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

import groovy.swing.factory.ComponentFactory
import org.jdesktop.swingx.JXMapKit
import org.jdesktop.swingx.JXMapViewer

import griffon.builder.swingx.factory.JXMapKitFactory
import griffon.builder.swingx.factory.JXMapViewerFactory
import griffon.builder.swingx.factory.JXHtmlFormFactory
import griffon.builder.swingx.factory.GeoBoundsFactory
import griffon.builder.swingx.factory.GeoPositionFactory
import griffon.builder.swingx.factory.WaypointFactory
import griffon.builder.swingx.factory.TileFactoryFactory
import griffon.builder.swingx.factory.TileFactoryInfoFactory

/**
 * @author Andres Almiray
 */
class SwingxWsGriffonAddon {
    def factories = [
        jxmapKit: new JXMapKitFactory(),
        jxmapViewer: new JXMapViewerFactory(),
        htmlForm: new JXHtmlFormFactory(),
        geoBounds: new GeoBoundsFactory(),
        geoPosition: new GeoPositionFactory(),
        waypoint: new WaypointFactory(),
        tileFactory: new TileFactoryFactory(),
        tileFactoryInfo: new TileFactoryInfoFactory()
    ]
}
