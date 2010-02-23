/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language govnerning permissions and
 * limitations under the License.
 */

package griffon.pivot

import griffon.application.StandaloneGriffonApplication
import griffon.util.GriffonApplicationHelper
import griffon.util.GriffonExceptionHandler
import griffon.util.UIThreadHelper

import org.apache.pivot.wtk.DesktopApplicationContext
import org.apache.pivot.wtk.Window

/**
 * @author Andres.Almiray
 */
class DesktopPivotApplication extends AbstractPivotApplication implements StandaloneGriffonApplication {
    List windows = []

    void bootstrap() {
        GriffonApplicationHelper.prepare(this)
        event("BootstrapEnd",[this])
    }

    void realize() {
        GriffonApplicationHelper.startup(this)
    }

    void show() {
        if(windows.size() > 0) {
            UIThreadHelper.instance.executeSync {
                windows[0].open(display)
            }
        }
        ready()
    }

    void shutdown() {
        windows.each {
            window -> window.close()
        }
        super.shutdown()
    }

    public Object createApplicationContainer() {
        def appContainer = null
        UIThreadHelper.instance.executeSync {
            appContainer = new Window()
            windows << appContainer
        }
        return appContainer
    }

    public static void main(String[] args) {
        GriffonExceptionHandler.registerExceptionHandler()
        DesktopApplicationContext.main(DesktopPivotApplication, args)
    }
}
