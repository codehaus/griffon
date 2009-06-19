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

package griffon.builder.fx

import groovy.util.GroovySwingTestCase
import javafx.stage.*
import javafx.scene.*
import javafx.scene.shape.*
import javafx.scene.text.*
import javafx.scene.layout.*
import javafx.scene.control.*
import javafx.scene.paint.*
import javafx.scene.image.*
import javafx.scene.transform.*
import javafx.scene.effect.*
import javafx.scene.effect.light.*
import javafx.ext.swing.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class FxBuilderTests extends GroovySwingTestCase {
   FxBuilder builder

   void setUp() {
      builder = new FxBuilder()
   }

   void testSwingButton() {
      testInEDT {
         boolean clicked = false
         def button = builder.swingButton(text: "Button", action: { clicked = true}, visible: true)
         assert button
         assert button.text == "Button"
         assert !clicked
         //button.getJButton().doClick()
         //assert clicked
         button.createAbstractButton()
         button.createJComponent()
         println button
         println button.getAbstractButton()
         println button.getJComponent()
         println button.properties
      }
   }

   void testStage() {
        def countCache = [:]
        def counter = { target ->
            def key = target.toString()
            def count = countCache.get(key,0)
            countCache[key] = ++count
            target.text = "Click me! ("+count+")"
        }
        testInEDT {
            builder.stage(title: "Griffon + FX", width: 240, height: 84) {
                builder.scene {
                    builder.vbox {
                    // swingButton(text: "Click me!", id: "button1",
                    //   width: 230, action: {counter(button1)})
                        builder.swingButton(text: "Click me!", id: "button2",
                            width: 230, action: {counter(button2)})
                    }
                }
            }
        }
   }
}