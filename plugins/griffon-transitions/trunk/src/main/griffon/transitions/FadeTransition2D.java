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
 */

package griffon.transitions;

import com.bric.image.transition.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

/**
 * @author Andres Almiray
 */
public class FadeTransition2D extends Transition2D {
   private final Color _color;

   public FadeTransition2D() {
      this(Color.BLACK);
   }

   public FadeTransition2D(Color color) {
      _color = color;
   }

   public Transition2DInstruction[] getInstructions(float progress, Dimension size) {
      if(progress <= 0.5) {
         Color derived = new Color(
            _color.getRed(),
            _color.getGreen(),
            _color.getBlue(),
            progress*2
         );
         return new Transition2DInstruction[] {
               new ImageInstruction(true),
               new ShapeInstruction(new Rectangle(0,0,size.width,size.height),  derived, null, 0)
         };
      } else {
         Color derived = new Color(
            _color.getRed(),
            _color.getGreen(),
            _color.getBlue(),
            (1 - progress)*2
         );
         return new Transition2DInstruction[] {
               new ShapeInstruction(new Rectangle(0,0,size.width,size.height),  derived, null, 0),
               new ImageInstruction(false, progress)
         };
      }
   }

   public String toString() {
      return "Fade";
   }
}
