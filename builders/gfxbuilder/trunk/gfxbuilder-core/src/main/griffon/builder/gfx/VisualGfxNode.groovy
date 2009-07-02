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
abstract class VisualGfxNode extends AbstractDrawableGfxNode {
   //private ObservableMap _drag = new ObservableMap()
   private Shape _shape
   private Shape _localShape

   @GfxAttribute(alias="s")  boolean asShape = false
   @GfxAttribute(alias="bc") def/*Color*/ borderColor
   @GfxAttribute(alias="bw") double borderWidth = 1d
   @GfxAttribute(alias="f")  def/*Color*/ fill
   //@GfxAttribute(alias="pt") boolean passThrough = false
   //@GfxAttribute(alias="ad") boolean autoDrag = false
   @GfxAttribute(alias="p")  def/*Paint*/ paint = null
   @GfxAttribute(alias="st") def/*Stroke*/ stroke = null
   @GfxAttribute(alias="tx") double translateX = Double.NaN
   @GfxAttribute(alias="ty") double translateY = Double.NaN
   @GfxAttribute(alias="ra") double rotateAngle = Double.NaN
   @GfxAttribute(alias="sx") double scaleX = Double.NaN
   @GfxAttribute(alias="sy") double scaleY = Double.NaN

   VisualGfxNode(String name) {
      super(name)
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
            double _x = _localShape.bounds.x
            double _y = _localShape.bounds.x
            double _cx = _x + (_localShape.bounds.width/2)
            double _cy = _y + (_localShape.bounds.height/2)
            AffineTransform affineTransform = new AffineTransform()
            if(!Double.isNaN(sx) && !Double.isNaN(sy)) {
               affineTransform.concatenate AffineTransform.getTranslateInstance(_x-_cx, _y-_cy)
               affineTransform.concatenate AffineTransform.getScaleInstance(sx, sy)
            }
            if(!Double.isNaN(tx) && !Double.isNaN(ty)) {
               affineTransform.concatenate AffineTransform.getTranslateInstance(tx, ty)
            }
            if(!Double.isNaN(ra)) {
               affineTransform.concatenate AffineTransform.getRotateInstance(Math.toRadians(ra),_cx, _cy)
            }
            _localShape = affineTransform.createTransformedShape(_localShape)
         }
      }
      _localShape
   }

   GfxRuntime createRuntime(GfxContext context) {
      _runtime = new VisualGfxRuntime(this, context)
      _runtime
   }

   abstract Shape calculateShape()

   void onDirty( PropertyChangeEvent event ) {
      super.onDirty(event)
      _shape = null
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

   protected void applyNestedNode(GfxNode node, GfxContext context) {
      // node.apply(context)
   }

   protected boolean withinClipBounds(GfxContext context, Shape shape) {
      context.g.clipBounds ? shape.intersects(context.g.clipBounds) : false
   }

   protected boolean shouldSkip(GfxContext context){
      if( super.shouldSkip(context) ) return true
      Shape shape = runtime.transformedShape
      if( !shape ) return false
       // honor the clip
      return asShape || !withinClipBounds(context, shape)
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