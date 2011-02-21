/*
 * Copyright 2007-2010 the original author or authors.
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

package griffon.builder.gfx.nodes.strokes

import griffon.builder.gfx.StrokeProvider

import java.awt.Stroke
import com.jhlabs.awt.CompositeStroke
import griffon.builder.gfx.GfxAttribute
import griffon.builder.gfx.GfxContext

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class CompositeStrokeNode extends AbstractStrokeNode implements ComposableStroke {
   @GfxAttribute(alias="s1") def stroke1
   @GfxAttribute(alias="s2") def stroke2

   CompositeStrokeNode() {
      super("compositeStroke")
   }

   void addStroke(Stroke stroke){
      if(!stroke1){
         stroke1 = stroke
      } else if(!stroke2 && stroke != stroke1){
         stroke2 = stroke
      }
   }

   void addStroke(StrokeProvider stroke){
      if(!stroke1){
         stroke1 = stroke
      } else if(!stroke2 && stroke != stroke1){
         stroke2 = stroke
      }
   }

   ComposableStroke leftShift(Stroke stroke) {
      addStroke(stroke)
   }

   ComposableStroke leftShift(StrokeProvider stroke) {
      addStroke(stroke)
   }

   void apply(GfxContext context) {
      if(stroke1 instanceof StrokeProvider) stroke1.apply(context)
      if(stroke2 instanceof StrokeProvider) stroke2.apply(context)
   }

    protected Stroke createStroke() {
        if( !stroke1 || !stroke2 ){
           throw new IllegalArgumentException("${this} must have two strokes.")
        }

        def _s1 = stroke1 instanceof StrokeProvider ? stroke1.getStroke() : stroke1
        def _s2 = stroke2 instanceof StrokeProvider ? stroke2.getStroke() : stroke2
        return new CompositeStroke(_s1, _s2)
    }
}