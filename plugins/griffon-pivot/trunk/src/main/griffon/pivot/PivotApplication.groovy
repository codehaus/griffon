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

import griffon.core.BaseGriffonApplication
// import griffon.application.StandaloneGriffonApplication
import griffon.util.GriffonApplicationHelper
import griffon.util.GriffonExceptionHandler
import griffon.util.UIThreadHelper

import org.apache.pivot.collections.Map as PivotMap
import org.apache.pivot.wtk.Application
import org.apache.pivot.wtk.DesktopApplicationContext
import org.apache.pivot.wtk.Display
import org.apache.pivot.wtk.Window
import org.apache.pivot.wtk.Alert
import org.apache.pivot.wtk.Component
import org.apache.pivot.wtk.DialogCloseListener
import org.apache.pivot.wtk.MessageType
import org.apache.pivot.wtk.Prompt
import org.apache.pivot.wtk.SheetCloseListener

import griffon.core.GriffonApplication
import griffon.util.EventRouter

/**
 * @author Andres.Almiray
 */
class PivotApplication implements /*StandaloneGriffonApplication,*/ Application, GriffonApplication {
//    @Delegate private final BaseGriffonApplication _base
    private Display display

    List windows = []

    PivotApplication() {
        UIThreadHelper.instance.setUIThreadHandler(new PivotUIThreadHandler())
//        _base = new BaseGriffonApplication(this)
    }

    void startup(Display display, PivotMap<String, String> properties) {
        this.display = display
        bootstrap()
        realize()
        show()
    }

    // ------------------------------------------

    boolean shutdown(boolean optional) {
        shutdown()
        false
    }

    void suspend() {
        event("AppSuspend", [this])
    }

    void resume() {
        event("AppResume", [this])
    }

    // ------------------------------------------

    void alert(Map args) {
        Map params = [type: MessageType.INFO, owner: (windows.size()?windows[0]:null), message: ''] + args
        if(!params.display && !params.owner) {
            throw new IllegalArgumentException("In alert() you must define a value for either display: or owner:")
        }

        List realArgs = [params.type, params.message]
        if(params.body && !(params.body instanceof Component)) {
            throw new IllegalArgumentException("In alert() you must define a value body: of type ${Component.class.name}")
        }
        realArgs << params.body
        realArgs << (params.display ? params.display : params.owner)
        realArgs << params.listener instanceof DialogCloseListener ? params.listener : (params.listener as DialogCloseListener)
        Alert.alert(*realArgs) 
    }

    void prompt(Map args) {
        Map params = [type: MessageType.INFO, owner: (windows.size()?windows[0]:null), message: ''] + args
        if(!params.display && !params.owner) {
            throw new IllegalArgumentException("In prompt() you must define a value for either display: or owner:")
        }

        List realArgs = [params.type, params.message]
        if(params.body && !(params.body instanceof Component)) {
            throw new IllegalArgumentException("In prompt() you must define a value body: of type ${Component.class.name}")
        }
        realArgs << params.body
        realArgs << (params.display ? params.display : params.owner)
        realArgs << params.listener instanceof SheetCloseListener ? params.listener : (params.listener as SheetCloseListener)
        Prompt.prompt(*realArgs) 
    }

    // ------------------------------------------

    private void bootstrap() {
        applicationProperties = new Properties()
        applicationProperties.load(getClass().getResourceAsStream('/application.properties'))
        GriffonApplicationHelper.prepare(this)
        event("BootstrapEnd",[this])
    }

    private void realize() {
        GriffonApplicationHelper.startup(this)
    }

    private void show() {
        if(windows.size() > 0) {
            UIThreadHelper.instance.executeSync {
                windows[0].open(display)
            }
        }
        ready()
    }

    public void ready() {
        event("ReadyStart",[this])
        GriffonApplicationHelper.runScriptInsideUIThread("Ready", this)
        event("ReadyEnd",[this])
    }

    public void shutdown() {
//        _base.shutdown()
        doShutdown()
        windows.each {
            window -> window.close()
        }
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
        DesktopApplicationContext.main(PivotApplication, args)
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
