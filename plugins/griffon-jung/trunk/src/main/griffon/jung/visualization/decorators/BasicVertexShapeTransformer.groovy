/*
 * Copyright 2009-2010 the original author or authors.
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
 * limitations under the License.
 */
package griffon.jung.visualization.decorators

import edu.uci.ics.jung.visualization.decorators.AbstractVertexShapeTransformer
import org.apache.commons.collections15.Transformer

import java.awt.Shape
import java.awt.geom.Rectangle2D
import java.awt.geom.AffineTransform

/**
 * @author Andres.Almiray
 */
class BasicVertexShapeTransformer<V> extends AbstractVertexShapeTransformer<V> implements Transformer<V,Shape> {
   private final Transformer<V,Shape> shapeTransformer
   private final boolean cacheShapes
   private final vertexCache = new WeakHashMap<V,Shape>()

   BasicVertexShapeTransformer(Closure shapeTransformer, boolean cacheShapes = true) {
      this(shapeTransformer as Transformer<V,Shape>, cacheShapes)
   }

   BasicVertexShapeTransformer(Transformer<V,Shape> shapeTransformer, boolean cacheShapes = true) {
      super()
      this.shapeTransformer = shapeTransformer
      this.cacheShapes = cacheShapes
   }

   BasicVertexShapeTransformer(Transformer<V,Integer> vsf, Transformer<V,Float> varf, Closure shapeTransformer, boolean cacheShapes = true) {
      this(vsf, varf, shapeTransformer as Transformer<V,Shape>, cacheShapes)
   }

   BasicVertexShapeTransformer(Transformer<V,Integer> vsf, Transformer<V,Float> varf, Transformer<V,Shape> shapeTransformer, boolean cacheShapes = true) {
      super(vsf, varf)
      this.shapeTransformer = shapeTransformer
      this.cacheShapes = cacheShapes
   }

   Shape transform(V v) {
      if(cacheShapes) {
         Shape shape = vertexCache.get(v)
         if(!shape) {
            shape = normalizeShape(v, createShape(v))
            vertexCache.put(v, shape)
         }
         return shape
      } else {
         return normalizeShape(v, createShape(v))
      }
   }

   protected Shape createShape(V v) {
      shapeTransformer.transform(v)
   }

   private Shape normalizeShape(V v, Shape shape) {
      Rectangle2D bounds = factory.getRectangle(v)
      Rectangle2D sbounds = shape.bounds2D
      double s = Math.min(bounds.width/sbounds.width, bounds.height/sbounds.height)
      double tx = sbounds.minX + sbounds.width/2
      double ty = sbounds.minY + sbounds.height/2
      AffineTransform at = AffineTransform.getScaleInstance(s, s)
      at.translate(-tx,-ty)
      return at.createTransformedShape(shape)
   }
}
