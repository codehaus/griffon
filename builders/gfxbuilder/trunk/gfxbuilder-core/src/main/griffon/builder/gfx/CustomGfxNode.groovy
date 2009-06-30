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

package griffon.builder.gfx

import java.awt.AlphaComposite
import java.awt.Composite
import java.awt.Shape
import java.awt.Color
import java.awt.Paint
import java.awt.geom.AffineTransform
import java.awt.geom.Rectangle2D
import java.beans.PropertyChangeEvent

import griffon.builder.gfx.runtime.*
import griffon.builder.gfx.nodes.transforms.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
abstract class CustomGfxNode extends VisualGfxNode {
   private VisualGfxNode _node
   private static final GfxBuilder GFXBUILDER = new GfxBuilder()

   CustomGfxNode() {
      super("customNode")
   }

   CustomGfxNode(String name) {
      super(name)
   }

   VisualGfxNode getNode() {
      if(!_node) {
         _node = createNode(GFXBUILDER)
         _node.addPropertyChangeListener(this)
      }
      _node
   }

   abstract VisualGfxNode createNode(GfxBuilder builder)

   Shape calculateShape() {
      getNode().getShape()
   }

   void propertyChanged(PropertyChangeEvent event) {
      if(event.source == _node) {
         onDirty(event)
      } else {
         super.propertyChanged(event)
      }
   }

   GfxRuntime getRuntime() {
      _runtime
   }

   GfxRuntime createRuntime(GfxContext context) {
      // TODO custom runtime?
      _runtime = new VisualGfxRuntime(this, context)
      _runtime
   }

   void onDirty( PropertyChangeEvent event ) {
      _node = null
      super.onDirty(event)
   }

   protected void applyBeforeAll(GfxContext context) {
      getNode()
      super.applyBeforeAll(context)
   }

   protected void applyNode(GfxContext context) {
      if( shouldSkip(context) ) return
      _node.apply(context)
   }

   protected void applyNestedNode(GfxNode node, GfxContext context) {
      // node.apply(context)
   }

   protected boolean shouldSkip(GfxContext context) {
      // if _node !visible or not withtin clipBounds
      false
   }
}