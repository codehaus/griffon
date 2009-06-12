/*
 * Copyright 2007-2008 the original author or authors.
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

package griffon.builder.flamingo

import org.jvnet.flamingo.common.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class FlamingoTests extends GroovySwingTestCase {
   void testCommandButton() {
      testInEDT {
         def flamingo = new FlamingoBuilder()
         def cmd = flamingo.commandButton("CMD")
         assert cmd.text == "CMD"
         cmd = flamingo.commandButton("CMD", icon: flamingo.svgIcon(resource: "icons/edit-paste.svg", class: FlamingoTests))
         assert cmd.text == "CMD"

         /*
         boolean called = false
         flamingo.noparent {
            actions {
              action( id: "cmdAction",
                 name: "CMD",
                 icon: svgIcon(resource: "icons/edit-paste.svg", class: FlamingoTests),
                 closure: { called = true; println "1111" },
                 shortDescription: "tooltip",
                 enabled: false)
            }
            commandButton(cmdAction, id: "cmd")
         }
         cmd = flamingo.cmd
         assert cmd.text == "CMD"
         assert cmd.enabled == false
         cmd.enabled = true
         cmd.doActionClick()
         assert called
         */
      }
   }
}