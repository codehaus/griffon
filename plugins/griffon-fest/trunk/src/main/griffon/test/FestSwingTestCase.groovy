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

package griffon.test

import org.fest.swing.fixture.FrameFixture
import javax.swing.JFrame
import griffon.application.SwingApplication
import griffon.util.GriffonApplicationHelper

/**
 * @author Andres.Almiray
 */
abstract class FestSwingTestCase extends GroovyTestCase {
   protected SwingApplication app
   protected FrameFixture window

   protected final void setUp() throws Exception {
      initApp()
      onSetUp()
   }

   protected final void tearDown() throws Exception {
      window.cleanUp()
      onTearDown()
   }

   protected void onSetUp() throws Exception { }
   protected void onTearDown() throws Exception { }

   private final void initApp() {
      app.metaClass.shutdown = {
         GriffonApplicationHelper.runScriptInsideEDT("Shutdown", delegate)
         delegate.appFrames.each { it.visible = false }
      }
      window = initWindow()
      window.component().defaultCloseOperation = JFrame.DISPOSE_ON_CLOSE
      window.show()
   }

   protected FrameFixture initWindow() {
      if(app.appFrames.size() > 0) return new FrameFixture(app.appFrames[0])
      throw new IllegalStateException("Application does not have Frames!")
   }
}
