/*
 * Copyright 2008-2010 the original author or authors.
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

package griffon.gtk

import griffon.core.BaseGriffonApplication
import griffon.application.StandaloneGriffonApplication
import griffon.util.GriffonApplicationHelper
import griffon.util.GriffonExceptionHandler
import griffon.util.UIThreadHelper

import griffon.core.GriffonApplication
import griffon.util.EventRouter
import griffon.util.Metadata

import org.gnome.gdk.Event
import org.gnome.gtk.Gtk
import org.gnome.gtk.Widget
import org.gnome.gtk.Window
import org.gnome.glib.Glib

/**
 * @author Andres Almiray
 */
class GtkApplication implements StandaloneGriffonApplication, GriffonApplication {
//    @Delegate private final BaseGriffonApplication _base

    List windows = []

    GtkApplication() {
        UIThreadHelper.instance.setUIThreadHandler(new GtkUIThreadHandler())
//        _base = new BaseGriffonApplication(this)
        loadApplicationProperties()
        // Glib.setProgramName(Metadata.current.getApplicationName())
    }

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
                windows[0].showAll()
            }
        }
        ready()
    }

    void ready() {
        event("ReadyStart",[this])
        GriffonApplicationHelper.runScriptInsideUIThread("Ready", this)
        event("ReadyEnd",[this])
        Gtk.main()
    }

    void shutdown() {
//        _base.shutdown()
        doShutdown()
        Gtk.mainQuit()
        System.exit(0)
    }

    public Object createApplicationContainer() {
        Window window = new Window()
        window.connect(new Window.DeleteEvent() {
            public boolean onDeleteEvent(Widget source, Event event) {
                windows.remove(source)
                if(config.application?.autoShutdown && windows.size() <= 1) {
                    shutdown()
                }
                return false
            }
        })
        windows << window
        return window
    }

    static void main(String[] args) {
        Gtk.init(args)
        GriffonExceptionHandler.registerExceptionHandler()
        GtkApplication gtka = new GtkApplication()
        gtka.bootstrap()
        gtka.realize()
        gtka.show()
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
