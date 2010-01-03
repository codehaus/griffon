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
import griffon.jung.builder.factory.*

import edu.uci.ics.jung.graph.Forest
import edu.uci.ics.jung.visualization.*
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse
import edu.uci.ics.jung.algorithms.layout.*

/**
 * @author Andres.Almiray
 */
class JungGriffonAddon {
    def factories = [
        "basicVisualizationViewer": new VisualizationViewerFactory(BasicVisualizationServer),
        "visualizationViewer": new VisualizationViewerFactory(VisualizationViewer),
        "balloonLayout": new LayoutFactory(BalloonLayout, Forest),
        "circleLayout": new LayoutFactory(CircleLayout),
        "dagLayout": new LayoutFactory(DAGLayout),
        "frLayout": new LayoutFactory(FRLayout),
        "frLayout2": new LayoutFactory(FRLayout2),
        "isomLayout": new LayoutFactory(ISOMLayout),
        "kkLayout": new LayoutFactory(KKLayout),
        "radialTreeLayout": new LayoutFactory(RadialTreeLayout, Forest),
        "springLayout": new LayoutFactory(SpringLayout),
        "springLayout2": new LayoutFactory(SpringLayout2),
        "staticLayout2": new LayoutFactory(StaticLayout),
        "treeLayout": new LayoutFactory(TreeLayout, Forest),
        "defaultModalGraphMouse": new VisualizationChildFactory(DefaultModalGraphMouse, "graphMouse")
    ]
}
