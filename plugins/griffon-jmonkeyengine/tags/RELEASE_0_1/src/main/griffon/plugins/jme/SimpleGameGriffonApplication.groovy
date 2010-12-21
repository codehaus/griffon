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

package griffon.plugins.jme

import com.jme3.system.AppSettings
import org.codehaus.griffon.runtime.core.BaseGriffonApplication
import griffon.swing.AbstractSwingGriffonApplication

/**
 * 
 * @author Andres Almiray
 */
class SimpleGameGriffonApplication extends AbstractSwingGriffonApplication implements GriffonGameApplication {
    private boolean gameDispensed = false
    
    final GriffonGameDelegate game

    SimpleGameGriffonApplication(String[] args = BaseGriffonApplication.EMPTY_ARGS) {
        super(args)
        game = new GriffonGameDelegate(this)
        game.showSettings = false
    }

    void realize() {
        super.realize()
        
        AppSettings appSettings = new AppSettings(true)
        appSettings.putAll(config.jme.settings ?: ['Title': config.application.title]) 
        
        game.settings = appSettings
    }

    void show() {
        if(config.jme.settings.show) game.showSettings = true
        
        game.start()
        callReady()
    }
    
    void exit() {
        game.quit()
        super.exit()
    }

    Object createApplicationContainer() {
        if(gameDispensed) {
            super.createApplicationContainer()
        } else {
            gameDispensed = true
            game
        }
    }
    
    public static void main(String[] args) {
        AbstractSwingGriffonApplication.run(SimpleGameGriffonApplication, args)
    }
}
