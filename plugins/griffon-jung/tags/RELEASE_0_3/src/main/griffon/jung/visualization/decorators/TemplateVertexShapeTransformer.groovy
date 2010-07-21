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

import org.apache.commons.collections15.Transformer

import java.awt.Shape
import java.awt.geom.Rectangle2D

/**
 * @author Andres Almiray
 */
class TemplateVertexShapeTransformer<V> extends BasicVertexShapeTransformer<V> {
   TemplateVertexShapeTransformer(Shape template = new Rectangle2D.Double(0d, 0d, 1d, 1d)) {
      super(DEFAULT_TRANSFORMER.curry(template), true)
   }

   TemplateVertexShapeTransformer(Transformer<V,Integer> vsf, Transformer<V,Float> varf, Shape template = new Rectangle2D.Double(0d, 0d, 1d, 1d)) {
      super(vsf, varf, DEFAULT_TRANSFORMER.curry(template), true)
   }

   private static final DEFAULT_TRANSFORMER = {shape, v -> shape}
}
