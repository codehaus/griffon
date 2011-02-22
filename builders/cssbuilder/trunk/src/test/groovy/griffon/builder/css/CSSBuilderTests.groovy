/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.builder.css

import groovy.swing.SwingBuilder
import groovy.util.GroovySwingTestCase
import com.feature50.clarity.ClarityConstants
import java.awt.Color
import javax.swing.*
import javax.swing.border.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class CSSBuilderTests extends GroovySwingTestCase {
   CSSBuilder builder

   void setUp() {
      builder = new CSSBuilder()
      def factories = new SwingBuilder().getFactories()
      builder.registerFactory("panel", factories["panel"])
      builder.registerFactory("button", factories["button"])
      builder.addAttributeDelegate(SwingBuilder.&objectIDAttributeDelegate)
      builder[SwingBuilder.DELEGATE_PROPERTY_OBJECT_ID] = SwingBuilder.DEFAULT_DELEGATE_PROPERTY_OBJECT_ID
   }

   void testCssClassAttribute() {
      testInEDT {
         builder.panel {
            button(id: "button", name: "button", cssClass: "active")
         }
         assert "active" == builder.button.getClientProperty(ClarityConstants.CLIENT_PROPERTY_CLASS_KEY)
      }
   }

   void testCssClassProperty() {
      testInEDT {
         builder.panel {
            button(id: "button", name: "button", cssClass: "active")
         }
         assert "active" == builder.button.cssClass
         builder.button.cssClass = "inactive"
         assert "inactive" == builder.button.cssClass
      }
   }

   void testFindComponentByName_shortcut() {
      testInEDT {
         def panel = builder.panel {
            button(id: "button1", name: "button1")
            button(id: "button2")
            panel {
               button(id: "button3", name: "button3")
            }
         }
         assert builder.button1 == panel.$("button1")
         assert !panel.$("button2")
         assert builder.button3 == panel.$("button3")
      }
   }

   void testFindComponentsByName_shortcut() {
      testInEDT {
         def panel = builder.panel {
            button(id: "button1", name: "button1")
            button(id: "button2")
            panel {
               button(id: "button3", name: "button3")
            }
         }
         assert [builder.button1, builder.button3] == panel.$s("button1","button3")
      }
   }

   void testFindComponentsBySelector_shortcut() {
      testInEDT {
         def panel = builder.panel {
            button(id: "button1", name: "button1", cssClass: "c1")
            button(id: "button2", cssClass: "c1")
            panel {
               button(id: "button3", name: "button3")
            }
         }
         assert [builder.button1, builder.button2, builder.button3] == panel.$$("jbutton")
         assert [builder.button1, builder.button2] == panel.$$(".c1")
         assert [builder.button3] == panel.$$("#button3")
      }
   }

   void testCSSDecorator() {
      testInEDT {
         def panel = builder.panel {
            button(id: "button1", name: "button1")
            button(id: "button2", name: "button2")
            button(id: "button3", name: "button3", cssClass: "active")
         }
         CSSDecorator.decorate("griffon/builder/css/simple",panel)
         assert panel.background == Color.RED
         assert builder.button1.background == Color.GREEN
         assert builder.button2.background == Color.BLUE
         assert builder.button3.background == Color.YELLOW
      }
   }
/*
   void testMargins() {
      testInEDT {
         def panel = builder.panel {
            button(id: "button1", name: "button1")
            button(id: "button2", name: "button2")
            button(id: "button3", name: "button3")
            button(id: "button4", name: "button4")
            button(id: "button5", name: "button5")
         }
         def insets1 = (1..5).inject([:]) { m, v ->
            def key = "button"+v
            def button = builder[key]
            m[key] = button.border.getBorderInsets(button)
            m
         }
         CSSDecorator.decorate("griffon/builder/css/margin",panel)
         def insets2 = (1..5).inject([:]) { m, v ->
            def key = "button"+v
            def button = builder[key]
            m[key] = button.border.getBorderInsets(button)
            m
         }

         assert BorderUtils.isMargin(builder.button1.border)
         assert BorderUtils.isMargin(builder.button2.border)
         assert BorderUtils.isMargin(builder.button3.border)
         assert BorderUtils.isMargin(builder.button4.border)
         assert !BorderUtils.isMargin(builder.button5.border)

         assert insets2.button1.top == (insets1.button1.top + 1)
         assert insets2.button1.bottom == insets1.button1.bottom
         assert insets2.button1.left == insets1.button1.left
         assert insets2.button1.right == insets1.button1.right

         assert insets2.button2.top == (insets1.button2.top + 1)
         assert insets2.button2.bottom == (insets1.button2.bottom + 2)
         assert insets2.button2.left == insets1.button2.left
         assert insets2.button2.right == insets1.button2.right

         assert insets2.button3.top == (insets1.button3.top + 1)
         assert insets2.button3.bottom == (insets1.button3.bottom + 3)
         assert insets2.button3.left == (insets1.button3.left + 2)
         assert insets2.button3.right == (insets1.button3.right + 2)

         assert insets2.button4.top == (insets1.button4.top + 1)
         assert insets2.button4.bottom == (insets1.button4.bottom + 3)
         assert insets2.button4.left == (insets1.button4.left + 4)
         assert insets2.button4.right == (insets1.button4.right + 2)

         assert insets2.button5.top == insets1.button5.top
         assert insets2.button5.bottom == insets1.button5.bottom
         assert insets2.button5.left == insets1.button5.left
         assert insets2.button5.right == insets1.button5.right
      }
   }

   void testMargins2() {
      testInEDT {
         def panel = builder.panel {
            button(id: "button1", name: "button1")
            button(id: "button2", name: "button2")
            button(id: "button3", name: "button3")
            button(id: "button4", name: "button4")
            button(id: "button5", name: "button5")
         }
         def insets1 = (1..5).inject([:]) { m, v ->
            def key = "button"+v
            def button = builder[key]
            m[key] = button.border.getBorderInsets(button)
            m
         }
         CSSDecorator.decorate("griffon/builder/css/margin2",panel)
         def insets2 = (1..5).inject([:]) { m, v ->
            def key = "button"+v
            def button = builder[key]
            m[key] = button.border.getBorderInsets(button)
            m
         }

         assert BorderUtils.isMargin(builder.button1.border)
         assert BorderUtils.isMargin(builder.button2.border)
         assert BorderUtils.isMargin(builder.button3.border)
         assert BorderUtils.isMargin(builder.button4.border)
         assert !BorderUtils.isMargin(builder.button5.border)

         assert insets2.button1.top == (insets1.button1.top + 1)
         assert insets2.button1.bottom == insets1.button1.bottom
         assert insets2.button1.left == insets1.button1.left
         assert insets2.button1.right == insets1.button1.right

         assert insets2.button2.top == (insets1.button2.top + 1)
         assert insets2.button2.bottom == (insets1.button2.bottom + 2)
         assert insets2.button2.left == insets1.button2.left
         assert insets2.button2.right == insets1.button2.right

         assert insets2.button3.top == (insets1.button3.top + 1)
         assert insets2.button3.bottom == (insets1.button3.bottom + 2)
         assert insets2.button3.left == (insets1.button3.left + 3)
         assert insets2.button3.right == insets1.button3.right

         assert insets2.button4.top == (insets1.button4.top + 1)
         assert insets2.button4.bottom == (insets1.button4.bottom + 2)
         assert insets2.button4.left == (insets1.button4.left + 3)
         assert insets2.button4.right == (insets1.button4.right + 4)

         assert insets2.button5.top == insets1.button5.top
         assert insets2.button5.bottom == insets1.button5.bottom
         assert insets2.button5.left == insets1.button5.left
         assert insets2.button5.right == insets1.button5.right
      }
   }
*/
   void testPaddings() {
      testInEDT {
         def panel = builder.panel {
            button(id: "button1", name: "button1")
            button(id: "button2", name: "button2")
            button(id: "button3", name: "button3")
            button(id: "button4", name: "button4")
            button(id: "button5", name: "button5")
         }
         def insets1 = (1..5).inject([:]) { m, v ->
            def key = "button"+v
            def button = builder[key]
            m[key] = button.border.getBorderInsets(button)
            m
         }
         CSSDecorator.decorate("griffon/builder/css/padding",panel)
         def insets2 = (1..5).inject([:]) { m, v ->
            def key = "button"+v
            def button = builder[key]
            m[key] = button.border.getBorderInsets(button)
            m
         }

         assert BorderUtils.isPadding(builder.button1.border)
         assert BorderUtils.isPadding(builder.button2.border)
         assert BorderUtils.isPadding(builder.button3.border)
         assert BorderUtils.isPadding(builder.button4.border)
         assert !BorderUtils.isPadding(builder.button5.border)

         assert insets2.button1.top == (insets1.button1.top + 1)
         assert insets2.button1.bottom == insets1.button1.bottom
         assert insets2.button1.left == insets1.button1.left
         assert insets2.button1.right == insets1.button1.right

         assert insets2.button2.top == (insets1.button2.top + 1)
         assert insets2.button2.bottom == (insets1.button2.bottom + 2)
         assert insets2.button2.left == insets1.button2.left
         assert insets2.button2.right == insets1.button2.right

         assert insets2.button3.top == (insets1.button3.top + 1)
         assert insets2.button3.bottom == (insets1.button3.bottom + 3)
         assert insets2.button3.left == (insets1.button3.left + 2)
         assert insets2.button3.right == (insets1.button3.right + 2)

         assert insets2.button4.top == (insets1.button4.top + 1)
         assert insets2.button4.bottom == (insets1.button4.bottom + 3)
         assert insets2.button4.left == (insets1.button4.left + 4)
         assert insets2.button4.right == (insets1.button4.right + 2)

         assert insets2.button5.top == insets1.button5.top
         assert insets2.button5.bottom == insets1.button5.bottom
         assert insets2.button5.left == insets1.button5.left
         assert insets2.button5.right == insets1.button5.right
      }
   }

   void testPaddings2() {
      testInEDT {
         def panel = builder.panel {
            button(id: "button1", name: "button1")
            button(id: "button2", name: "button2")
            button(id: "button3", name: "button3")
            button(id: "button4", name: "button4")
            button(id: "button5", name: "button5")
         }
         def insets1 = (1..5).inject([:]) { m, v ->
            def key = "button"+v
            def button = builder[key]
            m[key] = button.border.getBorderInsets(button)
            m
         }
         CSSDecorator.decorate("griffon/builder/css/padding2",panel)
         def insets2 = (1..5).inject([:]) { m, v ->
            def key = "button"+v
            def button = builder[key]
            m[key] = button.border.getBorderInsets(button)
            m
         }

         assert BorderUtils.isPadding(builder.button1.border)
         assert BorderUtils.isPadding(builder.button2.border)
         assert BorderUtils.isPadding(builder.button3.border)
         assert BorderUtils.isPadding(builder.button4.border)
         assert !BorderUtils.isPadding(builder.button5.border)

         assert insets2.button1.top == (insets1.button1.top + 1)
         assert insets2.button1.bottom == insets1.button1.bottom
         assert insets2.button1.left == insets1.button1.left
         assert insets2.button1.right == insets1.button1.right

         assert insets2.button2.top == (insets1.button2.top + 1)
         assert insets2.button2.bottom == (insets1.button2.bottom + 2)
         assert insets2.button2.left == insets1.button2.left
         assert insets2.button2.right == insets1.button2.right

         assert insets2.button3.top == (insets1.button3.top + 1)
         assert insets2.button3.bottom == (insets1.button3.bottom + 2)
         assert insets2.button3.left == (insets1.button3.left + 3)
         assert insets2.button3.right == insets1.button3.right

         assert insets2.button4.top == (insets1.button4.top + 1)
         assert insets2.button4.bottom == (insets1.button4.bottom + 2)
         assert insets2.button4.left == (insets1.button4.left + 3)
         assert insets2.button4.right == (insets1.button4.right + 4)

         assert insets2.button5.top == insets1.button5.top
         assert insets2.button5.bottom == insets1.button5.bottom
         assert insets2.button5.left == insets1.button5.left
         assert insets2.button5.right == insets1.button5.right
      }
   }
/*
   void testMarginsAndPaddings() {
      testInEDT {
         def panel = builder.panel {
            button(id: "button1", name: "button1")
            button(id: "button2", name: "button2")
            button(id: "button3", name: "button3")
            button(id: "button4", name: "button4")
            button(id: "button5", name: "button5")
         }
         def insets1 = (1..5).inject([:]) { m, v ->
            def key = "button"+v
            def button = builder[key]
            m[key] = button.border.getBorderInsets(button)
            m
         }
         CSSDecorator.decorate(["griffon/builder/css/margin","griffon/builder/css/padding"],panel)


         def insets2 = (1..5).inject([:]) { m, v ->
            def key = "button"+v
            def button = builder[key]
            m[key] = button.border.getBorderInsets(button)
            m
         }

         assert BorderUtils.isMargin(builder.button1.border)
         assert BorderUtils.isMargin(builder.button2.border)
         assert BorderUtils.isMargin(builder.button3.border)
         assert BorderUtils.isMargin(builder.button4.border)
         assert !BorderUtils.isMargin(builder.button5.border)

         assert insets2.button1.top == (insets1.button1.top + 2)
         assert insets2.button1.bottom == insets1.button1.bottom
         assert insets2.button1.left == insets1.button1.left
         assert insets2.button1.right == insets1.button1.right

         assert insets2.button2.top == (insets1.button2.top + 2)
         assert insets2.button2.bottom == (insets1.button2.bottom + 4)
         assert insets2.button2.left == insets1.button2.left
         assert insets2.button2.right == insets1.button2.right

         assert insets2.button3.top == (insets1.button3.top + 2)
         assert insets2.button3.bottom == (insets1.button3.bottom + 6)
         assert insets2.button3.left == (insets1.button3.left + 4)
         assert insets2.button3.right == (insets1.button3.right + 4)

         assert insets2.button4.top == (insets1.button4.top + 2)
         assert insets2.button4.bottom == (insets1.button4.bottom + 6)
         assert insets2.button4.left == (insets1.button4.left + 8)
         assert insets2.button4.right == (insets1.button4.right + 4)

         assert insets2.button5.top == insets1.button5.top
         assert insets2.button5.bottom == insets1.button5.bottom
         assert insets2.button5.left == insets1.button5.left
         assert insets2.button5.right == insets1.button5.right
      }
   }

   void testMarginsAndPaddings2() {
      testInEDT {
         def panel = builder.panel {
            button(id: "button1", name: "button1")
            button(id: "button2", name: "button2")
            button(id: "button3", name: "button3")
            button(id: "button4", name: "button4")
            button(id: "button5", name: "button5")
         }
         def insets1 = (1..5).inject([:]) { m, v ->
            def key = "button"+v
            def button = builder[key]
            m[key] = button.border.getBorderInsets(button)
            m
         }
         CSSDecorator.decorate(["griffon/builder/css/margin2","griffon/builder/css/padding2"],panel)


         def insets2 = (1..5).inject([:]) { m, v ->
            def key = "button"+v
            def button = builder[key]
            m[key] = button.border.getBorderInsets(button)
            m
         }

         assert BorderUtils.isMargin(builder.button1.border)
         assert BorderUtils.isMargin(builder.button2.border)
         assert BorderUtils.isMargin(builder.button3.border)
         assert BorderUtils.isMargin(builder.button4.border)
         assert !BorderUtils.isMargin(builder.button5.border)

         assert insets2.button1.top == (insets1.button1.top + 2)
         assert insets2.button1.bottom == insets1.button1.bottom
         assert insets2.button1.left == insets1.button1.left
         assert insets2.button1.right == insets1.button1.right

         assert insets2.button2.top == (insets1.button2.top + 2)
         assert insets2.button2.bottom == (insets1.button2.bottom + 4)
         assert insets2.button2.left == insets1.button2.left
         assert insets2.button2.right == insets1.button2.right

         assert insets2.button3.top == (insets1.button3.top + 2)
         assert insets2.button3.bottom == (insets1.button3.bottom + 4)
         assert insets2.button3.left == (insets1.button3.left + 6)
         assert insets2.button3.right == insets1.button3.right

         assert insets2.button4.top == (insets1.button4.top + 2)
         assert insets2.button4.bottom == (insets1.button4.bottom + 4)
         assert insets2.button4.left == (insets1.button4.left + 6)
         assert insets2.button4.right == (insets1.button4.right + 8)

         assert insets2.button5.top == insets1.button5.top
         assert insets2.button5.bottom == insets1.button5.bottom
         assert insets2.button5.left == insets1.button5.left
         assert insets2.button5.right == insets1.button5.right
      }
   }
*/
   void testBorders() {
      testInEDT {
         def panel = builder.panel {
            button(id: "button0", name: "button0")
            button(id: "button1", name: "button1")
            button(id: "button2", name: "button2")
            button(id: "button3", name: "button3")
            button(id: "button4", name: "button4")
            button(id: "button5", name: "button5")
         }
         def insets1 = (0..5).inject([:]) { m, v ->
            def key = "button"+v
            def button = builder[key]
            m[key] = button.border.getBorderInsets(button)
            m
         }
         CSSDecorator.decorate("griffon/builder/css/borders",panel)
         def insets2 = (0..5).inject([:]) { m, v ->
            def key = "button"+v
            def button = builder[key]
            m[key] = button.border.getBorderInsets(button)
            m
         }

         assert insets2.button0.top == 5
         assert insets2.button0.bottom == 5
         assert insets2.button0.left == 5
         assert insets2.button0.right == 5
         assert builder.button0.border.borderColor == Color.BLUE

         assert insets2.button1.top == 1
         assert insets2.button1.bottom == 0
         assert insets2.button1.left == 0
         assert insets2.button1.right == 0

         assert insets2.button2.top == 1
         assert insets2.button2.bottom == 2
         assert insets2.button2.left == 0
         assert insets2.button2.right == 0

         assert insets2.button3.top == 1
         assert insets2.button3.bottom == 2
         assert insets2.button3.left == 3
         assert insets2.button3.right == 0

         assert insets2.button4.top == 1
         assert insets2.button4.bottom == 2
         assert insets2.button4.left == 3
         assert insets2.button4.right == 4

         assert insets2.button5.top == insets1.button5.top
         assert insets2.button5.bottom == insets1.button5.bottom
         assert insets2.button5.left == insets1.button5.left
         assert insets2.button5.right == insets1.button5.right
      }
   }

   void testMarginsAndPaddingsAndBorders2() {
      testInEDT {
         def panel = builder.panel {
            button(id: "button1", name: "button1")
            button(id: "button2", name: "button2")
            button(id: "button3", name: "button3")
            button(id: "button4", name: "button4")
            button(id: "button5", name: "button5")
         }
         def insets1 = (1..5).inject([:]) { m, v ->
            def key = "button"+v
            def button = builder[key]
            m[key] = button.border.getBorderInsets(button)
            m
         }
         CSSDecorator.decorate(["griffon/builder/css/margin2","griffon/builder/css/padding2","griffon/builder/css/borders"],panel)


         def insets2 = (1..5).inject([:]) { m, v ->
            def key = "button"+v
            def button = builder[key]
            m[key] = button.border.getBorderInsets(button)
            m
         }

         /*
         assert BorderUtils.isMargin(builder.button1.border)
         assert BorderUtils.isMargin(builder.button2.border)
         assert BorderUtils.isMargin(builder.button3.border)
         assert BorderUtils.isMargin(builder.button4.border)
         assert !BorderUtils.isMargin(builder.button5.border)

         assert insets2.button1.top == 3
         assert insets2.button1.bottom == 0
         assert insets2.button1.left == 0
         assert insets2.button1.right == 0

         assert insets2.button2.top == 3
         assert insets2.button2.bottom == 6
         assert insets2.button2.left == 0
         assert insets2.button2.right == 0

         assert insets2.button3.top == 3
         assert insets2.button3.bottom == 6
         assert insets2.button3.left == 9
         assert insets2.button3.right == 0

         assert insets2.button4.top == 3
         assert insets2.button4.bottom == 6
         assert insets2.button4.left == 9
         assert insets2.button4.right == 12
         */

         assert insets2.button1.top == 2
         assert insets2.button1.bottom == 0
         assert insets2.button1.left == 0
         assert insets2.button1.right == 0

         assert insets2.button2.top == 2
         assert insets2.button2.bottom == 4
         assert insets2.button2.left == 0
         assert insets2.button2.right == 0

         assert insets2.button3.top == 2
         assert insets2.button3.bottom == 4
         assert insets2.button3.left == 6
         assert insets2.button3.right == 0

         assert insets2.button4.top == 2
         assert insets2.button4.bottom == 4
         assert insets2.button4.left == 6
         assert insets2.button4.right == 8

         assert insets2.button5.top == insets1.button5.top
         assert insets2.button5.bottom == insets1.button5.bottom
         assert insets2.button5.left == insets1.button5.left
         assert insets2.button5.right == insets1.button5.right
      }
   }

   void testSwingClientProperties() {
      testInEDT {
         def panel = builder.panel {
            button(id: "button1", name: "button1")
            button(id: "button2", name: "button2")
            button(id: "button3", name: "button3")
         }
         CSSDecorator.decorate("griffon/builder/css/swing",panel)
         assert builder.button1.getClientProperty("foo") == "FOO"
         assert builder.button2.getClientProperty("bar") == "BAR"
         assert !builder.button3.getClientProperty("foo")
         assert !builder.button3.getClientProperty("bar")
      }
   }

   void testSizes() {
      testInEDT {
         def panel = builder.panel {
            button(id: "button1", name: "button1")
         }
         CSSDecorator.decorate("griffon/builder/css/sizes",panel)
         def size = builder.button1.size
         assert size.width == 150
         assert size.height == 20
         size = builder.button1.preferredSize
         assert size.width == 175
         assert size.height == 25
         size = builder.button1.minimumSize
         assert size.width == 100
         assert size.height == 10
         size = builder.button1.maximumSize
         assert size.width == 200
         assert size.height == 30
      }
   }

   void testExpression() {
      testInEDT {
         def panel = builder.panel {
            button(id: "button1", name: "button1")
         }
         CSSBindings.instance.active = false
         CSSDecorator.decorate("griffon/builder/css/expressions", panel)
         assert builder.button1.foreground == Color.BLACK
         CSSBindings.instance.active = true
         CSSDecorator.decorate("griffon/builder/css/expressions", panel)
         assert builder.button1.foreground == Color.RED
      }
   }

   void testHexColors() {
      testInEDT {
         def panel = builder.panel()
         CSSDecorator.decorate("griffon/builder/css/colors", panel)
         assert panel.background == new Color(153, 153, 153)
      }
   }
}
