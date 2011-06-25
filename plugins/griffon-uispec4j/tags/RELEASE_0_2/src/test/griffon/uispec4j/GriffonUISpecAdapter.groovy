/*
 * Copyright 2011 the original author or authors.
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

package griffon.uispec4j

import org.uispec4j.Trigger
import org.uispec4j.UISpecAdapter
import org.uispec4j.Window
import org.uispec4j.interception.WindowInterceptor
import griffon.swing.SwingGriffonApplication

/**
 * @author Andres Almiray
 */
class GriffonUISpecAdapter implements UISpecAdapter {
    private SwingGriffonApplication app
    private Window window

    GriffonUISpecAdapter(SwingGriffonApplication app) {
        this.app = app
    }

    Window getMainWindow() {
        if (window == null) {
            window = new Window(app.windowManager.startingWindow)
        }
        app.windowManager.show(app.windowManager.startingWindow)
        window
    }
}
