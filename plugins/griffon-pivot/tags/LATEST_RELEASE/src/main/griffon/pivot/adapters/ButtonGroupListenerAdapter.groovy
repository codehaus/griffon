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
import org.apache.pivot.wtk.Button
import org.apache.pivot.wtk.ButtonGroup
import org.apache.pivot.wtk.ButtonGroupListener

/**
 * @author Andres Almiray
 */
class ButtonGroupListenerAdapter extends BuilderDelegate implements ButtonGroupListener {
    private Closure onButtonAdded
    private Closure onButtonRemoved
    private Closure onSelectionChanged
 
    ButtonGroupListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onButtonAdded(Closure callback) {
        onButtonAdded = callback
        onButtonAdded.delegate = this
    }

    void onButtonRemoved(Closure callback) {
        onButtonRemoved = callback
        onButtonRemoved.delegate = this
    }

    void onSelectionChanged(Closure callback) {
        onSelectionChanged = callback
        onSelectionChanged.delegate = this
    }

    void buttonAdded(ButtonGroup arg0, Button arg1) {
        if(onButtonAdded) onButtonAdded(arg0, arg1)
    }

    void buttonRemoved(ButtonGroup arg0, Button arg1) {
        if(onButtonRemoved) onButtonRemoved(arg0, arg1)
    }

    void selectionChanged(ButtonGroup arg0, Button arg1) {
        if(onSelectionChanged) onSelectionChanged(arg0, arg1)
    }
}