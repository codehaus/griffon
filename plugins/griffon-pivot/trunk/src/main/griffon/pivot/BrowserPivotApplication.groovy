/*
 * Copyright 2008-2009 the original author or authors.
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

// import griffon.application.StandaloneGriffonApplication
import griffon.util.GriffonExceptionHandler

import org.apache.pivot.wtk.BrowserApplicationContext
import org.apache.pivot.wtk.Application
import org.apache.pivot.wtk.Window
import java.lang.reflect.Field

/**
 * @author Andres.Almiray
 */
class BroswerPivotApplication extends AbstractPivotApplication /*implements StandaloneGriffonApplication*/ {
    private boolean appletContainerDispensed = false

    protected void shutdown() {
        event("StopStart",[this])
        GriffonApplicationHelper.runScriptInsideUIThread("Stop", this)
        event("StopEnd",[this])
        super.shutdown()
    }

    public Object createApplicationContainer() {
        if(appletContainerDispensed) {
            return new Window(display)
        } else {
            appletContainerDispensed = true
            return getHostApplet()
        }
    }

    private BrowserApplicationContext.HostApplet getHostApplet() {
        Field hostAppletsField = BrowserApplicationContext.class.getDeclaredField('hostApplets')
        hostAppletsField.setAccessible(true)
        List hostApplets = hostAppletsField.get(null)
        Application self = this
        hostApplets.find { it.application == self }
    }
}
