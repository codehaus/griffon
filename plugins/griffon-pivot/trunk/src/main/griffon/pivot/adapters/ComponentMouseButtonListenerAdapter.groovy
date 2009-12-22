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
import org.apache.pivot.wtk.Mouse.Button
import org.apache.pivot.wtk.ComponentMouseButtonListener

/**
 * @author Andres Almiray
 */
class ComponentMouseButtonListenerAdapter extends BuilderDelegate implements ComponentMouseButtonListener {
    private Closure onMouseClick
    private Closure onMouseDown
    private Closure onMouseUp
 
    ComponentMouseButtonListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onMouseClick(Closure callback) {
        onMouseClick = callback
        onMouseClick.delegate = this
    }

    void onMouseDown(Closure callback) {
        onMouseDown = callback
        onMouseDown.delegate = this
    }

    void onMouseUp(Closure callback) {
        onMouseUp = callback
        onMouseUp.delegate = this
    }

    boolean mouseClick(Component arg0, Button arg1, int arg2, int arg3, int arg4) {
        if(onMouseClick) onMouseClick(arg0, arg1, arg2, arg3, arg4)
    }

    boolean mouseDown(Component arg0, Button arg1, int arg2, int arg3) {
        if(onMouseDown) onMouseDown(arg0, arg1, arg2, arg3)
    }

    boolean mouseUp(Component arg0, Button arg1, int arg2, int arg3) {
        if(onMouseUp) onMouseUp(arg0, arg1, arg2, arg3)
    }
}