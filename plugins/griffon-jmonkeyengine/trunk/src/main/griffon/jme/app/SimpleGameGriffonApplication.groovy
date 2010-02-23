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
import griffon.core.GriffonApplication
import griffon.application.StandaloneGriffonApplication
import griffon.util.GriffonApplicationHelper
import griffon.util.GriffonExceptionHandler
import griffon.util.EventRouter
import griffon.util.Metadata
import griffon.util.UIThreadHelper
import java.awt.Toolkit

/**
 * @author Andres Almiray
 */
class SimpleGameGriffonApplication extends SimpleGame implements StandaloneGriffonApplication {
    @Delegate private final BaseGriffonApplication _base

    List appFrames  = []
    SimpleGameDelegate gameDelegate

    SimpleGameGriffonApplication() {
       _base = new BaseGriffonApplication(this)
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
        ready()
        start()
    }

    void shutdown() {
        stop()
        _base.shutdown()
        System.exit(0)
    }

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
}
