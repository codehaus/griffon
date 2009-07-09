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
import java.awt.Graphics
import java.awt.geom.AffineTransform
import java.beans.PropertyChangeEvent

import griffon.builder.gfx.event.GfxInputEvent
import griffon.builder.gfx.event.GfxInputListener
import griffon.builder.gfx.runtime.GfxRuntime
import griffon.builder.gfx.runtime.DrawableGfxRuntime
import griffon.builder.gfx.nodes.transforms.Transforms

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
abstract class AbstractDrawableNode extends GfxNode implements GfxInputListener, DrawableNode, Transformable {
   private Transforms _transforms
   protected GfxRuntime _runtime
   private Graphics _previousGraphics

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
   @GfxAttribute(alias="pt") boolean passThrough = false
   @GfxAttribute(alias="tx") double translateX = Double.NaN
   @GfxAttribute(alias="ty") double translateY = Double.NaN
   @GfxAttribute(alias="ra") double rotateAngle = Double.NaN
   @GfxAttribute(alias="sx") double scaleX = Double.NaN
   @GfxAttribute(alias="sy") double scaleY = Double.NaN

   AbstractDrawableNode(String name) {
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
      _transforms = transforms.clone()
      if(_transforms) _transforms.addPropertyChangeListener(this)
      firePropertyChange("transforms", oldValue, _transforms)
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
      if(keyPressed) this.@keyPressed(e)
   }

   void keyReleased(GfxInputEvent e) {
      if(keyReleased) this.@keyReleased(e)
   }

   void keyTyped(GfxInputEvent e) {
      if(keyTyped) this.@keyTyped(e)
   }

   void mouseClicked(GfxInputEvent e) {
      if(mouseClicked) this.@mouseClicked(e)
   }

   void mouseDragged(GfxInputEvent e) {
      if(mouseDragged) this.@mouseDragged(e)
   }

   void mouseEntered(GfxInputEvent e) {
      if(mouseEntered) this.@mouseEntered(e)
   }

   void mouseExited(GfxInputEvent e) {
      if(mouseExited) this.@mouseExited(e)
   }

   void mouseMoved(GfxInputEvent e) {
      if(mouseMoved) this.@mouseMoved(e)
   }

   void mousePressed(GfxInputEvent e) {
      if(mousePressed) this.@mousePressed(e)
   }

   void mouseReleased(GfxInputEvent e) {
      if(mouseReleased) this.@mouseReleased(e)
   }

   void mouseWheelMoved(GfxInputEvent e) {
      if(mouseWheelMoved) this.@mouseWheelMoved(e)
   }

   final void apply(GfxContext context) {
       if(!visible || !enabled) return
       beforeApply(context)
       applyNode(context)
       afterApply(context)
   }

   protected void beforeApply(GfxContext context) {
      _previousGraphics = context.g
      context.g = context.g.create()
      _transforms.apply(context)
      createRuntime(context)
      //if(shouldSkip(context)) return
      if(!Double.isNaN(opacity)) {
         context.g.composite = AlphaComposite.SrcOver.derive(opacity as float)
      }
   }

   protected abstract void applyNode(GfxContext contex)

   protected void afterApply(GfxContext context) {
      context.g.dispose()
      context.g = _previousGraphics
      //if(shouldSkip(context)) return
      addAsEventTarget(context)
   }

   protected void addAsEventTarget(GfxContext context) {
      if( visible || keyPressed || keyReleased || keyTyped || mouseClicked ||
          mouseDragged || mouseEntered || mouseExited || mouseMoved ||
          mousePressed || mouseReleased || mouseWheelMoved || enabled ){
          context.eventTargets << this
      }
   }

   protected boolean shouldSkip(GfxContext context){
      return false
   }
}