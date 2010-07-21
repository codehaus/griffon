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

import griffon.util.Environment
import griffon.jgraph.factory.*
import griffon.jgraph.shape.*
import com.mxgraph.view.mxGraph
import com.mxgraph.view.mxStylesheet
import com.mxgraph.canvas.mxGraphics2DCanvas
import com.mxgraph.shape.mxIShape
import java.awt.Shape

/**
 * @author Andres.Almiray
 */
class JgraphGriffonAddon {
    private final Map STYLES = [:]

    def addonInit = { app ->
        ConfigObject graphConfig = null
        try {
            Class graphConfigClass = app.class.classLoader.loadClass('GraphConfig')
            graphConfig = new ConfigSlurper(Environment.current.name).parse(graphConfigClass)
        } catch(Exception x) {
            // ignore
        }
        if(!graphConfig) return

        graphConfig.shapes?.each { name, shape ->
            if(shape instanceof Shape) shape = new ShapeAdapter(shape)
            mxGraphics2DCanvas.putShape(name, shape)
        }
        graphConfig.styles?.each { name, values ->
            Map settings = [:]
            values.each { k, v -> settings[k] = v }
            STYLES[name] = settings
        }

        mxGraph.metaClass.applyGraphStyle = { String name ->
            if(name && STYLES[name]) {
                delegate.stylesheet.putCellStyle(name, STYLES[name])
            } 
        }
    }

    def factories = [
        graphComponent: new GraphComponentFactory(),
        graph: new GraphFactory()
    ]

    def methods = [
        applyGraphStyle: { mxGraph graph, String name ->
            if(graph && name && STYLES[name]) {
                graph.stylesheet.putCellStyle(name, STYLES[name])
            } 
        }
    ]
}
