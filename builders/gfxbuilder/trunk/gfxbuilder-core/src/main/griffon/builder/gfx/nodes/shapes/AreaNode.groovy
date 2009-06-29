/*
 * Copyright 2007-2009 the original author or authors.
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
 */

package griffon.builder.gfx.nodes.shapes

import java.awt.Shape
import java.awt.geom.Area
import griffon.builder.gfx.GfxAttribute
import griffon.builder.gfx.VisualGfxNode
import griffon.builder.gfx.runtime.VisualGfxRuntime

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class AreaNode extends AbstractShapeGfxNode {
    private String areaMethod

    public AreaNode(String name, String methodName) {
        super("area-"+name)
        this.areaMethod = methodName
    }

    Shape calculateShape() {
        def shapeNodes = _nodes.findAll{ it instanceof VisualGfxNode }
        if( !shapeNodes ) {
           throw new IllegalArgumentException("No nested shapes on ${this}")
        }

        def shapes = shapeNodes.inject([]) { node, list ->
           def s = node.localShape
           if(s) list << s
           list
        }

        Area area = new Area(shapes[0])
        gos[1..-1].each { shape ->
           area."$areaMethod" new Area(shape)
        }
    }
}