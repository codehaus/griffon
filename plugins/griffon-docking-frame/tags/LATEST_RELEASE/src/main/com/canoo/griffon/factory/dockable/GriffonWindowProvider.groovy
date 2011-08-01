/*
 * Copyright 2009-2011 the original author or authors.
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
package com.canoo.griffon.factory.dockable

import bibliothek.gui.dock.util.AbstractWindowProvider
import griffon.core.GriffonApplication
import griffon.swing.SwingGriffonApplication
import java.awt.Window

/**
 * @author Alexander Klein
 */
class GriffonWindowProvider extends AbstractWindowProvider {
    GriffonApplication app
    Window window
    boolean shown = false

    GriffonWindowProvider(GriffonApplication app) {
        if(!app instanceof SwingGriffonApplication)
            throw new IllegalArgumentException('dockingFrame only works with the Swing-Toolkit')
        this.app = app
        app.addApplicationEventListener(this)
    }

    def onWindowShown = { win ->
        if (window != win) {
            window = win
            shown = true
            fireWindowChanged(win)
        }
        else {
            if (!shown)
                fireVisibilityChanged(true)
            shown = true
        }
    }

    def onWindowHidden = { win ->
        if (window == win) {
            if (shown)
                fireVisibilityChanged(false)
            shown = false
        }
    }

    Window searchWindow() {
        return window
    }

    boolean isShowing() {
        return shown
    }
}
