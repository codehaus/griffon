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
abstract class VisualGfxNode extends AggregateGfxNode implements GfxInputListener {
   private ObservableMap _drag = new ObservableMap()
   private Shape _shape
   private Shape _localShape
   private Transforms _transforms
   protected GfxRuntime _runtime
   private previousGraphics
   // private Filters

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

   @GfxAttribute(alias="s")  boolean asShape = false
   @GfxAttribute(alias="v")  boolean visible = true
   @GfxAttribute(alias="o")  double opacity = Double.NaN
   @GfxAttribute(alias="c")  Composite composite = null
   @GfxAttribute(alias="bc") def/*Color*/ borderColor
   @GfxAttribute(alias="bw") double borderWidth = 1d
   @GfxAttribute(alias="f")  def/*Color*/ fill
   @GfxAttribute(alias="pt") boolean passThrough = false
   @GfxAttribute(alias="ad") boolean autoDrag = false
   @GfxAttribute(alias="p")  def/*Paint*/ paint = null
   @GfxAttribute(alias="st") def/*Stroke*/ stroke = null
   @GfxAttribute(alias="tx") double translateX = Double.NaN
   @GfxAttribute(alias="ty") double translateY = Double.NaN
   @GfxAttribute(alias="ra") double rotateAngle = Double.NaN
   @GfxAttribute(alias="sx") double scaleX = Double.NaN
   @GfxAttribute(alias="sy") double scaleY = Double.NaN

   VisualGfxNode( String name ) {
      super( name )
      setTransforms(new Transforms())
   }

   Shape getShape() {
      if(!_shape) {
         _shape = calculateShape()
      }
      _shape
   }

   Shape getLocalShape() {
      if( !_localShape ) {
         _localShape = getShape()
         if(_localShape) {
            double __x = _localShape.bounds.x
            double __y = _localShape.bounds.x
            double __cx = __x + (_localShape.bounds.width/2)
            double __cy = __y + (_localShape.bounds.height/2)
            AffineTransform affineTransform = new AffineTransform()
            if(!Double.isNaN(sx) && !Double.isNaN(sy)) {
               affineTransform.concatenate AffineTransform.getTranslateInstance(__x-__cx, __y-__cy)
               affineTransform.concatenate AffineTransform.getScaleInstance(sx, sy)
            }
            if(!Double.isNaN(tx) && !Double.isNaN(ty)) {
               affineTransform.concatenate AffineTransform.getTranslateInstance(tx, ty)
            }
            if(!Double.isNaN(ra)) {
               affineTransform.concatenate AffineTransform.getRotateInstance(Math.toRadians(ra),__cx, __cy)
            }
            _localShape = affineTransform.createTransformedShape(_localShape)
         }
      }
      _localShape
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
      _runtime = new VisualGfxRuntime(this, context)
      _runtime
   }

   abstract Shape calculateShape()

   void onDirty( PropertyChangeEvent event ) {
      _shape = null
   }

   VisualGfxNode leftShift( VisualGfxNode node ) {
      nodes << node
      this
   }

   void keyPressed( GfxInputEvent e ) {
      if( keyPressed ) this.@keyPressed(e)
   }

   void keyReleased( GfxInputEvent e ) {
      if( keyReleased ) this.@keyReleased(e)
   }

   void keyTyped( GfxInputEvent e ) {
      if( keyTyped ) this.@keyTyped(e)
   }

   void mouseClicked( GfxInputEvent e ) {
      if( mouseClicked ) this.@mouseClicked(e)
   }

   void mouseDragged( GfxInputEvent e ) {
      //if( autoDrag ) this.trackDrag(e)
      if( mouseDragged ) this.@mouseDragged(e)
   }

   void mouseEntered( GfxInputEvent e ) {
      if( mouseEntered ) this.@mouseEntered(e)
   }

   void mouseExited( GfxInputEvent e ) {
      //if( autoDrag ) this.endDrag(e)
      if( mouseExited ) this.@mouseExited(e)
   }

   void mouseMoved( GfxInputEvent e ) {
      if( mouseMoved ) this.@mouseMoved(e)
   }

   void mousePressed( GfxInputEvent e ) {
      //if( autoDrag ) this.startDrag(e)
      if( mousePressed ) this.@mousePressed(e)
   }

   void mouseReleased( GfxInputEvent e ) {
      //if( autoDrag ) this.endDrag(e)
      if( mouseReleased ) this.@mouseReleased(e)
   }

   void mouseWheelMoved( GfxInputEvent e ) {
      if( mouseWheelMoved ) this.@mouseWheelMoved(e)
   }

   protected void applyBeforeAll(GfxContext context) {
      createRuntime(context)
      if(shouldSkip(context)) return
      previousGraphics = context.g
      context.g = context.g.create()
      if(!Double.isNaN(opacity)) {
         context.g.composite = AlphaComposite.SrcOver.derive(opacity as float)
      }
   }

   protected void applyNode(GfxContext context) {
      if( shouldSkip(context) ) return
      def shape = _runtime.transformedShape
      if( shape ) {
         //context.bounds = shape.bounds
         fill(context, shape)
         draw(context, shape)
      }
   }

   protected boolean applyBeforeNestedNodes(GfxContext context) {
      true
   }

   protected void applyNestedNode(GfxNode node, GfxContext context) {
      // node.apply(context)
   }

   protected boolean applyAfterNestedNodes(GfxContext context) {
      true
   }

   protected void applyAfterAll(GfxContext context) {
      context.g.dispose()
      context.g = previousGraphics
   }

   protected boolean withinClipBounds(GfxContext context, Shape shape) {
      context.g.clipBounds ? shape.intersects(context.g.clipBounds) : false
   }

   protected boolean shouldSkip(GfxContext context){
      Shape shape = runtime.transformedShape
      if( !shape ) return false
       // honor the clip
      if( asShape || !visible || !withinClipBounds(context, shape) ){
          return true
      }
      return false
   }

   protected void fill(GfxContext context, Shape shape){
       def __f = _runtime.fill
       def g = context.g

       switch( __f ){
          case Color:
             def color = g.color
             g.color = __f
             applyFill( context, shape )
             g.color = color
             break
          case Paint:
             def paint = g.paint
             g.paint = __f
             applyFill(context, shape)
             g.paint = paint
             break
          case PaintProvider:
             def paint = g.paint
             g.paint = __f.getPaint(shape.bounds)
             applyFill(context, shape)
             g.paint = paint
             break
          case MultiPaintProvider:
             __f.apply( context, shape )
             break
          default:
             // no fill
             break
       }
   }

   protected void applyFill(GfxContext context, Shape shape) {
        context.g.fill(shape)
   }

   protected void draw(GfxContext context, Shape shape) {
       def g = context.g
       def __bc = _runtime.borderColor
       def __st = _runtime.stroke
       //def __bp = findLast{ it instanceof BorderPaintProvider }

       /*if( __bp && __bp.paint ){
          def __ss = __st.createStrokedShape(shape)
          if( __bp.paint instanceof MultiPaintProvider ){
             __bp.apply(context,__ss)
          }else{
             def __p = g.paint
             g.paint = __bp.getPaint(context,__ss.bounds2D)
             g.fill(__ss)
             g.paint = __p
          }
       }else*/ if( __bc ){
          def __pc = g.color
          def __ps = g.stroke
          g.color = __bc
          if(__st) g.stroke = __st
          g.draw(shape)
          g.color = __pc
          if(__st) g.stroke = __ps
       }else{
          // don't draw the shape
       }
   }
}