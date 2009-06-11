/*
 * Copyright 2009 the original author or authors.
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
         assert builder.button1.background == new Color(0,128,0)
         assert builder.button2.background == Color.BLUE
         assert builder.button3.background == Color.YELLOW
      }
   }
}