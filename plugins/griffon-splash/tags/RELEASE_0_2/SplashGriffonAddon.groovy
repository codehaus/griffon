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

import griffon.core.GriffonApplication
import griffon.plugins.splash.SplashScreen

/**
 * @author Jim Shingler
 * @author Andres Almiray
 */
class SplashGriffonAddon {
    static void display(GriffonApplication app) {
        def image = app.config.splash.image ?: 'splash.png'
        if(image instanceof String) image = Thread.currentThread().contextClassLoader.getResource(image)

        app.execSync {
            SplashScreen splash = SplashScreen.instance
            if(image instanceof URL) {
                splash.setImage((URL) image)
                splash.splash()
                splash.waitForSplash()
            }
        }
    }

    def events = [
        ReadyEnd: { app ->
            app.execAsync {
                SplashScreen.instance.dispose() 
            }
        }
    ]
}
