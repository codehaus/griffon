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
import org.apache.pivot.wtk.ComponentStateListener

/**
 * @author Andres Almiray
 */
class ComponentStateListenerAdapter extends BuilderDelegate implements ComponentStateListener {
    private Closure onEnabledChanged
    private Closure onFocusedChanged
 
    ComponentStateListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onEnabledChanged(Closure callback) {
        onEnabledChanged = callback
        onEnabledChanged.delegate = this
    }

    void onFocusedChanged(Closure callback) {
        onFocusedChanged = callback
        onFocusedChanged.delegate = this
    }

    void enabledChanged(Component arg0) {
        if(onEnabledChanged) onEnabledChanged(arg0)
    }

    void focusedChanged(Component arg0, Component arg1) {
        if(onFocusedChanged) onFocusedChanged(arg0, arg1)
    }
}