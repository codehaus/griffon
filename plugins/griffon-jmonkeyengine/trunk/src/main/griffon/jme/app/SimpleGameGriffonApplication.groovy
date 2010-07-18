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
import griffon.core.BaseGriffonApplication
import griffon.util.GriffonExceptionHandler
import griffon.util.UIThreadHelper
import griffon.swing.*
import java.awt.EventQueue
import java.awt.Toolkit
import java.awt.Window

/**
 * @author Andres Almiray
 */
class SimpleGameGriffonApplication extends SimpleGame implements griffon.application.StandaloneGriffonApplication {
    @Delegate private final BaseGriffonApplication _base
    final WindowManager windowManager
    WindowDisplayHandler windowDisplayHandler
    private final WindowDisplayHandler defaultWindowDisplayHandler = new DefaultWindowDisplayHandler()

    private SimpleGameDelegate gameDelegate

    SimpleGameGriffonApplication() {
        UIThreadHelper.instance.setUIThreadHandler(new SwingUIThreadHandler())
        _base = new BaseGriffonApplication(this)
        windowManager = new WindowManager(this)
        addShutdownHandler(windowManager)
    }

    final WindowDisplayHandler resolveWindowDisplayHandler() {
        windowDisplayHandler ?: defaultWindowDisplayHandler
    }

    void bootstrap() {
        initialize()
        gameDelegate = resolveGameDelegate()
    }

    void realize() {
        startup()
    }

    void show() {
        List <Window> windows = windowManager.windows
        if(windows.size() > 0) windowManager.show(windows[0])

        callReady()
        start()
    }

    boolean shutdown() {
        if(_base.shutdown()) {
            exit()
        }
        false
    }

    void exit() {
        System.exit(0)
    }

    Object createApplicationContainer() {
        Window window = SwingUtils.createApplicationFrame(this)
        windowManager.attach(window)
        return window
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

    private void callReady() {
        // wait for EDT to empty out.... somehow
        boolean empty = false
        while (true) {
            UIThreadHelper.instance.executeSync {empty = Toolkit.defaultToolkit.systemEventQueue.peekEvent() == null}
            if (empty) break
            sleep(100)
        }

        ready()
    }

    protected void simpleInitGame() {
        event('InitGameStart', [])
        gameDelegate.simpleInitGame() 
        event('InitGameEnd', [])
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
}
