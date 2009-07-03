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
import java.beans.PropertyChangeEvent

import griffon.builder.gfx.runtime.*
import griffon.builder.gfx.nodes.transforms.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
abstract class AbstractDrawableGfxNode extends AggregateGfxNode implements GfxInputListener, Drawable, Transformable {
   private Transforms _transforms
   protected GfxRuntime _runtime
   private previousGraphics

   Closure beforeRender
   Closure afterRender

   Closure keyPressed
   Closure keyReleased
   Closure keyTyped
   Closure mouseClicked
   Closure mouseDragged
   Closure mouseEntered
   Closure mouseExited
   Closure mouseMoved
   Closure mousePressed
   Closure mouseReleased
   Closure mouseWheelMoved

   @GfxAttribute(alias="v")  boolean visible = true
   @GfxAttribute(alias="o")  double opacity = Double.NaN
   @GfxAttribute(alias="c")  Composite composite = null

   AbstractDrawableGfxNode(String name) {
      super(name)
      setTransforms(new Transforms())
   }

   void propertyChanged(PropertyChangeEvent event) {
      if(event.source == _transforms) {
         onDirty(event)
      } else {
         super.propertyChanged(event)
      }
   }

   void setTransforms(Transforms transforms) {
      def oldValue = _transforms
      if(_transforms) _transforms.removePropertyChangeListener(this)
      _transforms = transforms
      if(_transforms) _transforms.addPropertyChangeListener(this)
      firePropertyChange("transforms", oldValue, transforms)
   }

   Transforms getTransforms() {
      _transforms
   }

   Transforms getTxs() {
      _transforms
   }

   GfxRuntime getRuntime() {
      _runtime
   }

   GfxRuntime createRuntime(GfxContext context) {
      _runtime = new DrawableGfxRuntime(this, context)
      _runtime
   }

   void keyPressed(GfxInputEvent e) {
      if( keyPressed ) this.@keyPressed(e)
   }

   void keyReleased(GfxInputEvent e) {
      if( keyReleased ) this.@keyReleased(e)
   }

   void keyTyped(GfxInputEvent e) {
      if( keyTyped ) this.@keyTyped(e)
   }

   void mouseClicked(GfxInputEvent e) {
      if( mouseClicked ) this.@mouseClicked(e)
   }

   void mouseDragged(GfxInputEvent e) {
      if( mouseDragged ) this.@mouseDragged(e)
   }

   void mouseEntered(GfxInputEvent e) {
      if( mouseEntered ) this.@mouseEntered(e)
   }

   void mouseExited(GfxInputEvent e) {
      if( mouseExited ) this.@mouseExited(e)
   }

   void mouseMoved(GfxInputEvent e) {
      if( mouseMoved ) this.@mouseMoved(e)
   }

   void mousePressed(GfxInputEvent e) {
      if( mousePressed ) this.@mousePressed(e)
   }

   void mouseReleased(GfxInputEvent e) {
      if( mouseReleased ) this.@mouseReleased(e)
   }

   void mouseWheelMoved(GfxInputEvent e) {
      if( mouseWheelMoved ) this.@mouseWheelMoved(e)
   }

   protected void applyBeforeAll(GfxContext context) {
      previousGraphics = context.g
      context.g = context.g.create()
      _transforms?.apply(context)
      createRuntime(context)
      if(shouldSkip(context)) return
      if(!Double.isNaN(opacity)) {
         context.g.composite = AlphaComposite.SrcOver.derive(opacity as float)
      }
   }

   protected void applyAfterAll(GfxContext context) {
      context.g.dispose()
      context.g = previousGraphics
   }

   protected boolean shouldSkip(GfxContext context){
      return !visible
   }
}