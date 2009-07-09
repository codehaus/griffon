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

import java.awt.Shape
import java.awt.geom.AffineTransform
import griffon.builder.gfx.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class DrawableGfxRuntime extends AbstractGfxRuntime {
   protected def _shape
   protected def _transformedShape
   protected double _cx = Double.NaN
   protected double _cy = Double.NaN
   protected double _x = Double.NaN
   protected double _y = Double.NaN

   DrawableGfxRuntime(GfxNode node, GfxContext context){
      super(node, context)
   }

   /**
    * Returns the shape after applying explicit transformations.<p>
    *
    * @return a java.awt.Shape
    */
   public def getShape() {
      if( !_shape ){
         _shape = _node.getShape()
      }
      _shape
   }

   public def getLocalShape() {
      _node.getLocalShape()
   }

   /**
    * Returns the shape after applying transformations.<p>
    *
    * @return a java.awt.Shape
    */
   public def getTransformedShape() {
      if( !_transformedShape ) {
         _transformedShape = getLocalShape()
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
      getTransformedShape()
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
            _x = s.bounds.x
            _y = s.bounds.y
         }
      }
      _y
   }
}