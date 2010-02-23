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

package griffon.swt

import griffon.core.BaseGriffonApplication
import griffon.application.StandaloneGriffonApplication
import griffon.util.GriffonApplicationHelper
import griffon.util.GriffonExceptionHandler
import griffon.util.UIThreadHelper

import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell

/**
 * @author Andres Almiray
 */
class SWTApplication implements StandaloneGriffonApplication {
    @Delegate private final BaseGriffonApplication _base
    private final Display defaultDisplay

    List shells = []

    SWTApplication() {
        _base = new BaseGriffonApplication(this)
        UIThreadHelper.instance.setUIThreadHandler(new SWTUIThreadHandler())
        loadApplicationProperties()
        defaultDisplay = Display.default
    }

    public void bootstrap() {
        GriffonApplicationHelper.prepare(this)
        event("BootstrapEnd",[this])
    }

    public void realize() {
        GriffonApplicationHelper.startup(this)
    }

    public void show() {
        if(shells.size() > 0) {
            UIThreadHelper.instance.executeSync {
                shells[0].open()
            }
        }
        ready()
    }

    public void ready() {
        event("ReadyStart",[this])
        GriffonApplicationHelper.runScriptInsideUIThread("Ready", this)
        event("ReadyEnd",[this])

        while(!shells[0].isDisposed()) {
            if(!defaultDisplay.readAndDispatch()) {
                defaultDisplay.sleep()
            }
        }
    }

    public void shutdown() {
        doShutdown()
        UIThreadHelper.instance.executeSync { 
            defaultDisplay.shells?.each { it.dispose() }
        }
        _base.shutdown()
        System.exit(0)
    }

    public Object createApplicationContainer() {
        def appContainer = null
        UIThreadHelper.instance.executeSync {
            appContainer = new Shell(defaultDisplay, SWT.BORDER | SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.TITLE)
            shells += appContainer
        }
        return appContainer
    }

    public static void main(String[] args) {
        GriffonExceptionHandler.registerExceptionHandler()
        SWTApplication sa = new SWTApplication()
        sa.bootstrap()
        sa.realize()
        sa.show()
    }
}
