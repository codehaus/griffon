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

import org.gnome.gdk.Event
import org.gnome.gtk.Gtk
import org.gnome.gtk.Widget
import org.gnome.gtk.Window
import org.gnome.glib.Glib

/**
 * @author Andres Almiray
 */
class GtkApplication implements StandaloneGriffonApplication {
    @Delegate private final BaseGriffonApplication _base

    List windows = []

    GtkApplication() {
        _base = new BaseGriffonApplication(this)
        UIThreadHelper.instance.setUIThreadHandler(new GtkUIThreadHandler())
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
        _base.shutdown()
        Gtk.mainQuit()
        System.exit(0)
    }

    public Object createApplicationContainer() {
        Window window = new Window()
        def selfConfig = config
        window.connect(new Window.DeleteEvent() {
            public boolean onDeleteEvent(Widget source, Event event) {
                windows.remove(source)
                if(selfConfig.application?.autoShutdown && windows.size() <= 1) {
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
}
