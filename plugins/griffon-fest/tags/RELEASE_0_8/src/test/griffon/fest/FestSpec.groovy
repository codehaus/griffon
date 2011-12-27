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

import spock.lang.Specification
import griffon.util.ApplicationHolder
import org.fest.swing.fixture.FrameFixture
import griffon.swing.SwingGriffonApplication
import java.util.concurrent.CountDownLatch

/**
 * @author Andres Almiray
 */
class FestSpec extends Specification {
    protected SwingGriffonApplication app = ApplicationHolder.application
    protected FrameFixture window
    private boolean realized = false
    private final Object realizedLock = new Object()

    def final setup() {
        initApp()
        onSetup()
    }

    def final cleanup() {
        onCleanup()
        window.cleanUp()
    }

    protected void setupConfig(SwingGriffonApplication app) { }
    protected void onSetup() {}
    protected void onCleanup() {}

    private final void initApp() {
        synchronized(realizedLock) {
            if(!realized) {
                app.metaClass.exit = { ->
                    // NOOP
                }

                def latch = new CountDownLatch(1)
                setupConfig(app)
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
