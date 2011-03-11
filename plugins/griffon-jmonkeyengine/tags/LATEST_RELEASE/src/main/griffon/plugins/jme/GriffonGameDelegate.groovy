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

import com.jme3.app.SimpleApplication
import com.jme3.renderer.RenderManager

/**
 * 
 * @author Andres Almiray
 */
class GriffonGameDelegate extends SimpleApplication {
    final GriffonGameApplication app
    
    Closure onInit
    Closure onRender
    Closure onUpdate
    
    GriffonGameDelegate(GriffonGameApplication app) {
        this.app = app
    }

    void simpleInitApp() {
        if(onInit) {
            onInit.delegate = this
            onInit.resolveStrategy = Closure.DELEGATE_FIRST
            onInit.call()
        }
        app.event('JmeInit', [app])
    }

    void simpleUpdate(float tpf) {
        if(onUpdate) {
            onUpdate.delegate = this
            onUpdate.resolveStrategy = Closure.DELEGATE_FIRST
            onUpdate(tpf)
        }
        app.event('JmeUpdate', [app, tpf])   
    }

    void simpleRender(RenderManager rm) {
        if(onRender) {
            onRender.delegate = this
            onRender.resolveStrategy = Closure.DELEGATE_FIRST
            onRender(rm)
        }
        app.event('JmeRender', [app, rm])
    }
    
    public void quit() {
        super.destroy();
    }
        
    public void destroy() {
        app.shutdown();
    }
}
