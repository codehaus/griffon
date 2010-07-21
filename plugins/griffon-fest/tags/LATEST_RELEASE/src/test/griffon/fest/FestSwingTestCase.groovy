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

package griffon.fest

import org.fest.swing.fixture.FrameFixture
import java.util.concurrent.CountDownLatch
import griffon.swing.SwingApplication
import griffon.test.GriffonUnitTestCase

/**
 * @author Andres Almiray
 */
abstract class FestSwingTestCase extends GriffonUnitTestCase {
    private boolean realized = false
    private final Object realizedLock = new Object()

    protected SwingApplication app
    protected FrameFixture window

    protected final void setUp() {
        super.setup()
        initApp()
        onSetUp()
    }

    protected final void tearDown() {
        window.cleanUp()
        onTearDown()
        super.tearDown()
    }
 
    protected void onSetUp() { }
    protected void onTearDown() { }
 
    private final void initApp() {
        synchronized(realizedLock) {
            if(!realized) {
                app.metaClass.exit = { ->
                    // NOOP
                }

                def latch = new CountDownLatch(1)
                app.execSync {
                    app.realize()
                    latch.countDown()
                }
                latch.await()
                realized = true
            }
        }
        window = initWindow()
        window.show()
    }
 
    protected FrameFixture initWindow() {
        if(app.windowManager.windows.size() > 0) return new FrameFixture(app.windowManager.windows[0])
        throw new IllegalStateException("Application does not have managed Windows!")
    }
}
