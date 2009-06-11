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

import groovy.util.GroovySwingTestCase
import com.feature50.clarity.ClarityConstants

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class CSSBuilderTests extends GroovySwingTestCase {
   void testCssClassAttribute() {
      testInEDT {
         def builder = new CSSBuilder()
         builder.panel {
            button(id: "button", name: "button", cssClass: "active")
         }
         assert "active" == builder.button.getClientProperty(ClarityConstants.CLIENT_PROPERTY_CLASS_KEY)
      }
   }

   void testFindComponentByName_shortcut() {
      testInEDT {
         def builder = new CSSBuilder()
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
         def builder = new CSSBuilder()
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
}