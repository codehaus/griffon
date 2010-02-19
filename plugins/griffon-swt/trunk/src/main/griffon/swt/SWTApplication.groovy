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

import griffon.core.GriffonApplication
import griffon.util.EventRouter
import griffon.util.Metadata

/**
 * @author Andres Almiray
 */
class SWTApplication implements StandaloneGriffonApplication, GriffonApplication {
//    @Delegate private final BaseGriffonApplication _base
    private final Display defaultDisplay

    List shells = []

    SWTApplication() {
        UIThreadHelper.instance.setUIThreadHandler(new SWTUIThreadHandler())
//        _base = new BaseGriffonApplication(this)
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
//        _base.shutdown()
        doShutdown()
        UIThreadHelper.instance.executeSync { 
            defaultDisplay.shells?.each { it.dispose() }
        }
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

    // ==============================================================

    Map<String, ?> addons = [:]
    Map<String, String> addonPrefixes = [:]

    Map<String, Map<String, String>> mvcGroups = [:]
    Map models      = [:]
    Map views       = [:]
    Map controllers = [:]
    Map builders    = [:]
    Map groups      = [:]

    Binding bindings = new Binding()
    Properties applicationProperties
    ConfigObject config
    ConfigObject builderConfig
    Object eventsConfig

    private final EventRouter eventRouter = new EventRouter()

    // define getter/setter otherwise it will be treated as a read-only property
    // because only the getter was defined in GriffonApplication
    public Properties getApplicationProperties() {
        return applicationProperties
    }
    public void setApplicationProperties(Properties applicationProperties) {
        this.applicationProperties = applicationProperties
    }
    public void loadApplicationProperties() {
        this.applicationProperties = Metadata.getCurrent()
    }

    public Metadata getMetadata() {
        Metadata.current
    }

    public Class getConfigClass() {
        return getClass().classLoader.loadClass("Application")
    }

    public Class getBuilderClass() {
        return getClass().classLoader.loadClass("Builder")
    }

    public Class getEventsClass() {
        try{
           return getClass().classLoader.loadClass("Events")
        } catch( ignored ) {
           // ignore - no global event handler will be used
        }
        return null
    }

    public void initialize() {
        GriffonApplicationHelper.runScriptInsideUIThread("Initialize", this)
    }

    private void doShutdown() {
        event("ShutdownStart",[this])
        List mvcNames = []
        mvcNames.addAll(groups.keySet())
        mvcNames.each { 
            GriffonApplicationHelper.destroyMVCGroup(this, it)
        }
        GriffonApplicationHelper.runScriptInsideUIThread("Shutdown", this)
    }

    public void startup() {
        event("StartupStart",[this])
        GriffonApplicationHelper.runScriptInsideUIThread("Startup", this)
        event("StartupEnd",[this])
    }

    public void event( String eventName, List params = [] ) {
        eventRouter.publish(eventName, params)
    }

    public void addApplicationEventListener( listener ) {
       eventRouter.addEventListener(listener)
    }

    public void removeApplicationEventListener( listener ) {
       eventRouter.removeEventListener(listener)
    }

    public void addApplicationEventListener( String eventName, Closure listener ) {
       eventRouter.addEventListener(eventName,listener)
    }

    public void removeApplicationEventListener( String eventName, Closure listener ) {
       eventRouter.removeEventListener(eventName,listener)
    }

    public void addMvcGroup(String mvcType, Map<String, String> mvcPortions) {
       mvcGroups[mvcType] = mvcPortions
    }
}
