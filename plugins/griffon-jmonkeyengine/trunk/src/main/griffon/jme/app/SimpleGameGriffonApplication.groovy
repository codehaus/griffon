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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.jme.app

import com.jme.app.SimpleGame
// import griffon.util.BaseGriffonApplication
import griffon.core.GriffonApplication
import griffon.util.GriffonApplicationHelper
import griffon.util.GriffonExceptionHandler
import griffon.util.EventRouter
import griffon.util.Metadata
import griffon.util.UIThreadHelper
import java.awt.Toolkit

/**
 * @author Andres Almiray
 */
class SimpleGameGriffonApplication extends SimpleGame implements GriffonApplication {
    // @Delegate private final BaseGriffonApplication _base

    List appFrames  = []
    SimpleGameDelegate gameDelegate

    SimpleGameGriffonApplication() {
       super()
       // _base = new BaseGriffonApplication(this)
       loadApplicationProperties()
    }

    void bootstrap() {
        GriffonApplicationHelper.prepare(this)
        gameDelegate = resolveGameDelegate()
        event("BootstrapEnd",[this])
    }

    void realize() {
        GriffonApplicationHelper.startup(this)
    }

    void show() {
        callReady()
        start()
    }

/*
    void shutdown() {
        stop()
        _base.shutdown()
        System.exit(0)
    }
*/

    Object createApplicationContainer() {
        def appContainer = GriffonApplicationHelper.createJFrameApplication(this)
        try {
            appFrames += appContainer
        } catch (Throwable ignored) {
            // if it doesn't have a window closing event, ignore it
        }
        return appContainer
    }

    private SimpleGameDelegate resolveGameDelegate() {
        String delegateClassName = config?.jme?.simpleGameDelegate ?: 'griffon.jme.app.SimpleGameDelegate'
        try {
            Class delegateClass = getClass().classLoader.loadClass(delegateClassName)
            return delegateClass.getDeclaredConstructor([SimpleGameGriffonApplication] as Class[]).newInstance([this] as Object[])
        } catch(Exception e) {
            return new SimpleGameDelegate(this)
        }
    }

    protected void simpleInitGame() {
        event("InitGameStart", [])
        gameDelegate.simpleInitGame() 
        event("InitGameEnd", [])
    }

    protected void simpleUpdate() {
        gameDelegate.simpleUpdate() 
    }

    static void main(String[] args) {
        GriffonExceptionHandler.registerExceptionHandler()
        SimpleGameGriffonApplication sgga = new SimpleGameGriffonApplication()
        sgga.bootstrap()
        sgga.realize()
        sgga.show()
    }

    // ==================================================

    /**
     * Calls the ready lifecycle mhetod after the UI thread calms down
     */
    private void callReady() {
        // wait for EDT to empty out.... somehow
        boolean empty = false
        while (true) {
            UIThreadHelper.instance.executeSync {empty = Toolkit.defaultToolkit.systemEventQueue.peekEvent() == null}
            if (empty) break
            sleep(100)
        }

        ready();
    }

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
    // because only the getter was defined in IGriffonApplication
    Properties getApplicationProperties() {
        return applicationProperties
    }
    void setApplicationProperties(Properties applicationProperties) {
        this.applicationProperties = applicationProperties
    }
    public void loadApplicationProperties() {
        this.applicationProperties = Metadata.getCurrent()
    }

    Class getConfigClass() {
        return getClass().classLoader.loadClass("Application")
    }

    Class getBuilderClass() {
        return getClass().classLoader.loadClass("Builder")
    }

    Class getEventsClass() {
        try{
           return getClass().classLoader.loadClass("Events")
        } catch( ignored ) {
           // ignore - no global event handler will be used
        }
        return null
    }

    void initialize() {
        GriffonApplicationHelper.runScriptInsideEDT("Initialize", this)
    }

    void ready() {
        event("ReadyStart",[this])
        GriffonApplicationHelper.runScriptInsideEDT("Ready", this)
        event("ReadyEnd",[this])
    }

    void shutdown() {
        event("ShutdownStart",[this])
        stop()
        List mvcNames = []
        mvcNames.addAll(groups.keySet())
        mvcNames.each {
            GriffonApplicationHelper.destroyMVCGroup(this, it)
        }
        GriffonApplicationHelper.runScriptInsideEDT("Shutdown", this)
        System.exit()
    }

    void startup() {
        event("StartupStart",[this])
        GriffonApplicationHelper.runScriptInsideEDT("Startup", this)
        event("StartupEnd",[this])
    }

    void event( String eventName, List params = [] ) {
        eventRouter.publish(eventName, params)
    }

    void addApplicationEventListener( listener ) {
       eventRouter.addEventListener(listener)
    }

    void removeApplicationEventListener( listener ) {
       eventRouter.removeEventListener(listener)
    }

    void addApplicationEventListener( String eventName, Closure listener ) {
       eventRouter.addEventListener(eventName,listener)
    }

    void removeApplicationEventListener( String eventName, Closure listener ) {
       eventRouter.removeEventListener(eventName,listener)
    }

    void addMvcGroup(String mvcType, Map<String, String> mvcPortions) {
       mvcGroups[mvcType] = mvcPortions
    }
}
