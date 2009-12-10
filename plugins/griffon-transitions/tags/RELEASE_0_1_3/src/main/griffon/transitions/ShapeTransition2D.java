/*
 * Copyright 2009 the original author or authors.
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

package griffon.transitions;

import com.bric.image.transition.Transition2D;
import com.bric.image.transition.vanilla.AbstractShapeTransition2D;
import java.awt.Shape;

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class ShapeTransition2D extends AbstractShapeTransition2D {
   private final Shape _shape;
   private final String _shapeName;

   public ShapeTransition2D(Shape shape, String shapeName) {
       this(shape, shapeName, Transition2D.OUT);
   }

   public ShapeTransition2D(Shape shape, String shapeName, int type) {
      super(type);
      _shape = shape;
      _shapeName = shapeName;
   }

    public Shape getShape() {
        return _shape;
    }

    public String getShapeName() {
        return _shapeName;
    }
}
