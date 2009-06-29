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

package griffon.builder.gfx.runtime

import java.awt.Color
import java.awt.Paint
import java.awt.BasicStroke
import java.awt.Stroke
import java.awt.Shape
import java.awt.geom.Area
import java.awt.geom.AffineTransform
import griffon.builder.gfx.*

import griffon.builder.gfx.nodes.transforms.Transform

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class VisualGfxRuntime extends AbstractGfxRuntime {
   protected def _fill
   protected def _paint
   protected def _borderColor
   protected def _borderWidth
   protected def _stroke
   protected def _shape
   protected def _transformedShape
   protected def _localShape
   protected def _boundingShape
   protected double _cx = Double.NaN
   protected double _cy = Double.NaN
   protected double _x = Double.NaN
   protected double _y = Double.NaN

   VisualGfxRuntime(GfxNode node, GfxContext context){
      super(node, context)
   }

   /**
    * Returns the borderColor taking into account inherited value from group.<p>
    *
    * @return a java.awt.Color
    */
   public def getBorderColor(){
      if( !_borderColor ){
         _borderColor = _context.g.color
         if( _context.groupSettings.borderColor != null ){
            _borderColor = _context.groupSettings.borderColor
         }
         if( _node.borderColor != null ){
            _borderColor = _node.borderColor
         }
         if( _borderColor instanceof Boolean && !_borderColor ){
            _borderColor = null
         }else{
            if( _borderColor instanceof String ){
               _borderColor = Colors.getColor(_borderColor)
            }
            if( !_borderColor ){
               _borderColor = _context.g.color
            }
         }
      }
      _borderColor
   }

   /**
    * Returns the borderWidth taking into account inherited value from group.<p>
    *
    * @return an int
    */
   public def getBorderWidth(){
      if( _borderWidth == null ){
         _borderWidth = 1
         if( _context.groupSettings.borderWidth != null ){
            _borderWidth = _context.groupSettings.borderWidth
         }
         if( _node.borderWidth != null ){
            _borderWidth = _node.borderWidth
         }
      }
      _borderWidth
   }

   /**
    * Returns the fill to be used.<p>
    * Fill will be determined with the following order<ol>
    * <li>fill property (inherited from group too) if set to false</li>
    * <li>nested paint node</li>
    * <li>fill property (inherited from group too) if set to non null, non false</li>
    * </ol>
    *
    * @return either <code>false</code>/<code>null</code> (no fill), java.awt.Color, java.awt.Paint or MultiPaintProvider 
    */
   public def getFill() {
      if( _fill == null ){
         if( _context.groupSettings.fill != null ){
            _fill = _context.groupSettings.fill
         }
         if( _node.fill != null ){
            _fill = _node.fill
         }

         def pp = getPaint()
         if( _fill instanceof Boolean && !_fill ){
            _fill = null
         }else if( pp != null ){
            if( pp instanceof PaintProvider ){
               //_fill = pp.runtime(context).paint
            }else if( pp instanceof MultiPaintProvider ){
               // let the caller handle it
               _fill = pp
            }
         }else if( _fill ){
            switch( _fill ){
               case String:
                  _fill = Colors.getColor(_fill)
                  break
               case PaintProvider:
                  //_fill = _fill.runtime(context).paint
                  break
               case true:
                  _fill = _context?.g.color
                  break
               case Color:
               case Paint:
                  /* do nothing */
                  break
            }
         }
      }
      _fill
   }

   /**
    * Returns a nested paint() node if any.<p>
    *
    * @return either a PaintProvider, MultipaintProvider or null if no nested paint() node is found
    */
   public def getPaint() {
      if( !_paint ){
          _node.nodes.each { n ->
             if(n instanceof PaintProvider || n instanceof MultiPaintProvider) _paint = n
          }
      }
      _paint
   }

   /**
    * Returns a java.awt.Stroke.<p>
    * Stroke will be determined with the following order<ol>
    * <li>nested stroke operation</li>
    * <li>borderWidth property (inherited from group too)</li>
    * <li>default stroke on Graphics object</li>
    * </ol>
    *
    * @return a java.awt.Stroke
    */
   public def getStroke() {
      if( _stroke == null ){
         def s = _node.findLast { it instanceof StrokeProvider }
         def bw = getBorderWidth()
         if( s ){
            _stroke = s.getStroke()
         }else if( bw ){
            def ps = _context.g.stroke
            if( ps instanceof BasicStroke ){
               _stroke = ps.derive(width:bw)
            }else{
               _stroke = new BasicStroke( bw as float )
            }
         }else{
            _stroke = _context.g.stroke
         }
      }
      _stroke
   }

   /**
    * Returns the shape after applying explicit transformations.<p>
    *
    * @return a java.awt.Shape
    */
   public def getShape() {
      if( !_shape ){
         _shape = _node.shape
      }
      _shape
   }

   /**
    * Returns the shape after applying local transformations.<p>
    *
    * @return a java.awt.Shape
    */
   /*public def getLocalShape() {
      if( !_localShape ) {
         _localShape = getShape()
         if(_localShape) {
            double x = _localShape.bounds.x
            double y = _localShape.bounds.x
            double cx = x + (_localShape.bounds.width/2)
            double cy = y + (_localShape.bounds.height/2)
            AffineTransform affineTransform = new AffineTransform()
            if(!Double.isNaN(_node.sx) && !Double.isNaN(_node.sy)) {
               affineTransform.concatenate AffineTransform.getTranslateInstance(x-cx, y-cy)
               affineTransform.concatenate AffineTransform.getScaleInstance(_node.sx, _node.sy)
            }
            if(!Double.isNaN(_node.tx) && !Double.isNaN(_node.ty)) {
               affineTransform.concatenate AffineTransform.getTranslateInstance(_node.tx, _node.ty)
            }
            if(!Double.isNaN(_node.ra)) {
               affineTransform.concatenate AffineTransform.getRotateInstance(Math.toRadians(_node.ra),cx, cy)
            }
            _localShape = affineTransform.createTransformedShape(_localShape)
         }
      }
      _localShape
   }*/

   /**
    * Returns the shape after applying transformations.<p>
    *
    * @return a java.awt.Shape
    */
   public def getTransformedShape() {
      if( !_transformedShape ) {
         _transformedShape = _node.getLocalShape()
         if(_transformedShape) {
            AffineTransform affineTransform = new AffineTransform()
            affineTransform.concatenate _context.g.transform
            _node.transforms.each { t ->
               if(t.transform) affineTransform.concatenate t.transform
            }
            _transformedShape = affineTransform.createTransformedShape(_transformedShape)
         }
      }
      _transformedShape
   }

   /**
    * Returns the bounding shape including stroked border.<p>
    *
    * @return a java.awt.Shape
    */
   public def getBoundingShape() {
      if( !_boundingShape ){
         def s = getTransformedShape()
         if(s) {
            def st = getStroke()
            _boundingShape = new Area(s)
            _boundingShape.add(new Area(st.createStrokedShape(_boundingShape)))
         }
      }
      _boundingShape
   }

   public double getCx() {
      if(Double.isNaN(_cx)) {
         def s = getTransformedShape()
         if(s) {
            _cx = s.bounds.x + (s.bounds.width/2)
            _cy = s.bounds.y + (s.bounds.height/2)
         }
      }
      _cx
   }

   public double getCy() {
      if(Double.isNaN(_cy)) {
         def s = getTransformedShape()
         if(s) {
            _cx = s.bounds.x + (s.bounds.width/2)
            _cy = s.bounds.y + (s.bounds.height/2)
         }
      }
      _cy
   }

   public double getX() {
      if(Double.isNaN(_x)) {
         def s = getTransformedShape()
         if(s) {
            _x = s.bounds.x
            _y = s.bounds.y
         }
      }
      _x
   }

   public double getY() {
      if(Double.isNaN(_y)) {
         def s = getTransformedShape()
         if(s) {
            _y = s.bounds.x
            _y = s.bounds.y
         }
      }
      _y
   }
}