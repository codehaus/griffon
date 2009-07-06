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
class GroupGfxRuntime extends DrawableGfxRuntime {
   GroupGfxRuntime(GfxNode node, GfxContext context){
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
}