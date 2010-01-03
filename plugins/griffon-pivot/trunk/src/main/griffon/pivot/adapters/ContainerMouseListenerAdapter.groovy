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

package griffon.pivot.adapters
 
import griffon.pivot.impl.BuilderDelegate
import org.apache.pivot.wtk.Container
import org.apache.pivot.wtk.Mouse.Button
import org.apache.pivot.wtk.Mouse.ScrollType
import org.apache.pivot.wtk.ContainerMouseListener

/**
 * @author Andres Almiray
 */
class ContainerMouseListenerAdapter extends BuilderDelegate implements ContainerMouseListener {
    private Closure onMouseWheel
    private Closure onMouseDown
    private Closure onMouseMove
    private Closure onMouseUp
 
    ContainerMouseListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onMouseWheel(Closure callback) {
        onMouseWheel = callback
        onMouseWheel.delegate = this
    }

    void onMouseDown(Closure callback) {
        onMouseDown = callback
        onMouseDown.delegate = this
    }

    void onMouseMove(Closure callback) {
        onMouseMove = callback
        onMouseMove.delegate = this
    }

    void onMouseUp(Closure callback) {
        onMouseUp = callback
        onMouseUp.delegate = this
    }

    boolean mouseWheel(Container arg0, ScrollType arg1, int arg2, int arg3, int arg4, int arg5) {
        if(onMouseWheel) onMouseWheel(arg0, arg1, arg2, arg3, arg4, arg5)
    }

    boolean mouseDown(Container arg0, Button arg1, int arg2, int arg3) {
        if(onMouseDown) onMouseDown(arg0, arg1, arg2, arg3)
    }

    boolean mouseMove(Container arg0, int arg1, int arg2) {
        if(onMouseMove) onMouseMove(arg0, arg1, arg2)
    }

    boolean mouseUp(Container arg0, Button arg1, int arg2, int arg3) {
        if(onMouseUp) onMouseUp(arg0, arg1, arg2, arg3)
    }
}