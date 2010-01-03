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
import org.apache.pivot.wtk.Component
import org.apache.pivot.wtk.ComponentMouseListener

/**
 * @author Andres Almiray
 */
class ComponentMouseListenerAdapter extends BuilderDelegate implements ComponentMouseListener {
    private Closure onMouseOut
    private Closure onMouseMove
    private Closure onMouseOver
 
    ComponentMouseListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onMouseOut(Closure callback) {
        onMouseOut = callback
        onMouseOut.delegate = this
    }

    void onMouseMove(Closure callback) {
        onMouseMove = callback
        onMouseMove.delegate = this
    }

    void onMouseOver(Closure callback) {
        onMouseOver = callback
        onMouseOver.delegate = this
    }

    void mouseOut(Component arg0) {
        if(onMouseOut) onMouseOut(arg0)
    }

    boolean mouseMove(Component arg0, int arg1, int arg2) {
        if(onMouseMove) onMouseMove(arg0, arg1, arg2)
    }

    void mouseOver(Component arg0) {
        if(onMouseOver) onMouseOver(arg0)
    }
}