package griffon.transitions;

import com.bric.image.transition.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Rectangle;

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
