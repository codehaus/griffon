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
import org.apache.pivot.wtk.Action
import org.apache.pivot.wtk.Keyboard.KeyStroke
import org.apache.pivot.wtk.Window
import org.apache.pivot.wtk.Window.ActionMapping
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk.WindowActionMappingListener

/**
 * @author Andres Almiray
 */
class WindowActionMappingListenerAdapter extends BuilderDelegate implements WindowActionMappingListener {
    private Closure onActionChanged
    private Closure onActionMappingAdded
    private Closure onActionMappingsRemoved
    private Closure onKeyStrokeChanged
 
    WindowActionMappingListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onActionChanged(Closure callback) {
        onActionChanged = callback
        onActionChanged.delegate = this
    }

    void onActionMappingAdded(Closure callback) {
        onActionMappingAdded = callback
        onActionMappingAdded.delegate = this
    }

    void onActionMappingsRemoved(Closure callback) {
        onActionMappingsRemoved = callback
        onActionMappingsRemoved.delegate = this
    }

    void onKeyStrokeChanged(Closure callback) {
        onKeyStrokeChanged = callback
        onKeyStrokeChanged.delegate = this
    }

    void actionChanged(ActionMapping arg0, Action arg1) {
        if(onActionChanged) onActionChanged(arg0, arg1)
    }

    void actionMappingAdded(Window arg0) {
        if(onActionMappingAdded) onActionMappingAdded(arg0)
    }

    void actionMappingsRemoved(Window arg0, int arg1, Sequence arg2) {
        if(onActionMappingsRemoved) onActionMappingsRemoved(arg0, arg1, arg2)
    }

    void keyStrokeChanged(ActionMapping arg0, KeyStroke arg1) {
        if(onKeyStrokeChanged) onKeyStrokeChanged(arg0, arg1)
    }
}