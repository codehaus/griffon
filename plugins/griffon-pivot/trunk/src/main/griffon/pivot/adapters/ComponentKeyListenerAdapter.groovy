/*
 * Copyright 2009 the original author or authors.
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

package griffon.pivot.adapters
 
import griffon.pivot.impl.BuilderDelegate
import org.apache.pivot.wtk.Component
import org.apache.pivot.wtk.Keyboard.KeyLocation
import org.apache.pivot.wtk.ComponentKeyListener

/**
 * @author Andres Almiray
 */
class ComponentKeyListenerAdapter extends BuilderDelegate implements ComponentKeyListener {
    private Closure onKeyPressed
    private Closure onKeyReleased
    private Closure onKeyTyped
 
    ComponentKeyListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onKeyPressed(Closure callback) {
        onKeyPressed = callback
        onKeyPressed.delegate = this
    }

    void onKeyReleased(Closure callback) {
        onKeyReleased = callback
        onKeyReleased.delegate = this
    }

    void onKeyTyped(Closure callback) {
        onKeyTyped = callback
        onKeyTyped.delegate = this
    }

    boolean keyPressed(Component arg0, int arg1, KeyLocation arg2) {
        if(onKeyPressed) onKeyPressed(arg0, arg1, arg2)
    }

    boolean keyReleased(Component arg0, int arg1, KeyLocation arg2) {
        if(onKeyReleased) onKeyReleased(arg0, arg1, arg2)
    }

    boolean keyTyped(Component arg0, char arg1) {
        if(onKeyTyped) onKeyTyped(arg0, arg1)
    }
}