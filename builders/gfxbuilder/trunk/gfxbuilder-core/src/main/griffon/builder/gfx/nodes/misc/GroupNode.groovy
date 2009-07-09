/*
 * Copyright 2007-2008 the original author or authors.
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

package griffon.builder.gfx.nodes.misc

import griffon.builder.gfx.*
import griffon.builder.gfx.runtime.*

import java.awt.Rectangle
import java.awt.Shape
import java.awt.Graphics
import java.awt.geom.Area
import java.awt.geom.AffineTransform

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class GroupNode extends AbstractGfxNode  {
    private Map _previousGroupSettings = [:]
    private Shape _shape

    GroupNode() {
        super("group")
        passThrough = true
    }

    GfxRuntime createRuntime(GfxContext context) {
        _runtime = new GroupGfxRuntime(this, context)
        _runtime
    }

    protected void beforeApply(GfxContext context) {
       //createRuntime(context)
       super.beforeApply(context)
       //if(shouldSkip(context)) return
       _previousGroupSettings = [:]
       _previousGroupSettings.putAll(context.groupSettings)
       if(borderColor != null) context.groupSettings.borderColor = borderColor
       if(borderWidth != null) context.groupSettings.borderWidth = borderWidth
       if(fill != null) context.groupSettings.fill = fill
    }

    protected void afterApply(GfxContext context) {
       //if(shouldSkip(context)) return
       context.groupSettings = _previousGroupSettings
       super.afterApply(context)
    }
/*
    protected boolean shouldSkip(GfxContext context){
       return !visible
    }
*/
    protected void applyThisNode(GfxContext context) {
//       if(shouldSkip(context)) return
    }

    protected void applyNestedNode(GfxNode node, GfxContext context) {
//       if(shouldSkip(context)) return
       node.apply(context)
    }

    Shape calculateShape() {
       GfxContext context = getRuntime().getContext()
       List shapes = []

       getNodes().each { node ->
          if(!node.enabled) return
          if(node instanceof DrawableNode) {
             if(!node.visible) return
             if(!node.getRuntime()) node.createRuntime(context)
             Graphics gcopy = null
             if(node.txs.enabled && !node.txs.empty) {
                gcopy = context.g
                context.g = gcopy.create()
             }
             try {
                Shape s = node.getRuntime().getTransformedShape()
                if(s) shapes << s
             } finally {
                if(gcopy) {
                   context.g.dispose()
                   context.g = gcopy
                }
             }
          }
       }

       if(!shapes) return null

       Area area = new Area(shapes[0])
       if(shapes.size() > 1) {
          shapes[1..-1].each { shape ->
             area.add(shape instanceof Area ? shape : new Area(shape))
          }
       }
       area
    }
}