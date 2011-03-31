/*
 * Copyright 2008-2011 the original author or authors.
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
 * See the License for the specific language govenrning permissions and
 * limitations under the License.
 */
package griffon.swt

import griffon.util.GriffonExceptionHandler
import griffon.util.UIThreadHelper
import griffon.util.UIThreadHandler
import org.eclipse.swt.SWT
import org.eclipse.swt.widgets.Display
import org.eclipse.swt.widgets.Shell
import griffon.application.StandaloneGriffonApplication
import org.codehaus.griffon.runtime.core.BaseGriffonApplication

/**
 * Basic implementation of {@code GriffonApplication} that runs in standalone mode using SWT.
 *
 * @author Andres Almiray
 */
abstract class AbstractSWTGriffonApplication implements SWTGriffonApplication, StandaloneGriffonApplication {
    @Delegate protected final BaseGriffonApplication baseApp
    final WindowManager windowManager
    WindowDisplayHandler windowDisplayHandler
    private final WindowDisplayHandler defaultWindowDisplayHandler = new ConfigurableWindowDisplayHandler()
    private static final Class[] CTOR_ARGS = [String[].class] as Class[]    
    protected final Display defaultDisplay

    AbstractSWTGriffonApplication(String[] args = BaseGriffonApplication.EMPTY_ARGS) {        
        defaultDisplay = Display.default
        UIThreadHelper.instance.setUIThreadHandler(getUIThreadHandler())
        baseApp = new BaseGriffonApplication(this, args)
        windowManager = new WindowManager(this)
        addShutdownHandler(windowManager)
    }

    protected UIThreadHandler getUIThreadHandler() {
        new SWTUIThreadHandler()
    }

    final WindowDisplayHandler resolveWindowDisplayHandler() {
        windowDisplayHandler ?: defaultWindowDisplayHandler
    }

    void bootstrap() {
        initialize()
    }

    void realize() {
        startup()
    }

    void show() {
        windowManager.show(windowManager.startingWindow)

        callReady()
    }

    boolean shutdown() {
        if(baseApp.shutdown()) {
            exit()
        }
        false
    }

    void exit() {
        System.exit(0)
    }

    /**
     * Calls the ready lifecycle method after the UI thread calms down
     */
    protected void callReady() {
        ready()
        Shell mainWindow = windowManager.startingWindow
        while(!mainWindow.isDisposed()) {
            if(!defaultDisplay.readAndDispatch()) {
                defaultDisplay.sleep()
            }
        }
    }

    public Object createApplicationContainer() {
        def appContainer = null
        UIThreadHelper.instance.executeSync {
            appContainer = new Shell(defaultDisplay, SWT.BORDER | SWT.CLOSE | SWT.MIN | SWT.MAX | SWT.RESIZE | SWT.TITLE)
        }
        return appContainer
    }
    
    static run(Class applicationClass, String[] args) {
        GriffonExceptionHandler.registerExceptionHandler()
        StandaloneGriffonApplication app = applicationClass.getDeclaredConstructor(CTOR_ARGS).newInstance([args] as Object[])
        app.bootstrap()
        app.realize()
        app.show()        
    }
}
