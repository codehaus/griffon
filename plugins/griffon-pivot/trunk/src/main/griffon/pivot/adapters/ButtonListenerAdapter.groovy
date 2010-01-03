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
import org.apache.pivot.wtk.Button
import org.apache.pivot.wtk.ButtonGroup
import org.apache.pivot.wtk.Button.DataRenderer
import org.apache.pivot.wtk.ButtonListener

/**
 * @author Andres Almiray
 */
class ButtonListenerAdapter extends BuilderDelegate implements ButtonListener {
    private Closure onButtonDataChanged
    private Closure onDataRendererChanged
    private Closure onActionChanged
    private Closure onToggleButtonChanged
    private Closure onTriStateChanged
    private Closure onButtonGroupChanged
    private Closure onSelectedKeyChanged
    private Closure onStateKeyChanged
 
    ButtonListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onButtonDataChanged(Closure callback) {
        onButtonDataChanged = callback
        onButtonDataChanged.delegate = this
    }

    void onDataRendererChanged(Closure callback) {
        onDataRendererChanged = callback
        onDataRendererChanged.delegate = this
    }

    void onActionChanged(Closure callback) {
        onActionChanged = callback
        onActionChanged.delegate = this
    }

    void onToggleButtonChanged(Closure callback) {
        onToggleButtonChanged = callback
        onToggleButtonChanged.delegate = this
    }

    void onTriStateChanged(Closure callback) {
        onTriStateChanged = callback
        onTriStateChanged.delegate = this
    }

    void onButtonGroupChanged(Closure callback) {
        onButtonGroupChanged = callback
        onButtonGroupChanged.delegate = this
    }

    void onSelectedKeyChanged(Closure callback) {
        onSelectedKeyChanged = callback
        onSelectedKeyChanged.delegate = this
    }

    void onStateKeyChanged(Closure callback) {
        onStateKeyChanged = callback
        onStateKeyChanged.delegate = this
    }

    void buttonDataChanged(Button arg0, Object arg1) {
        if(onButtonDataChanged) onButtonDataChanged(arg0, arg1)
    }

    void dataRendererChanged(Button arg0, DataRenderer arg1) {
        if(onDataRendererChanged) onDataRendererChanged(arg0, arg1)
    }

    void actionChanged(Button arg0, Action arg1) {
        if(onActionChanged) onActionChanged(arg0, arg1)
    }

    void toggleButtonChanged(Button arg0) {
        if(onToggleButtonChanged) onToggleButtonChanged(arg0)
    }

    void triStateChanged(Button arg0) {
        if(onTriStateChanged) onTriStateChanged(arg0)
    }

    void buttonGroupChanged(Button arg0, ButtonGroup arg1) {
        if(onButtonGroupChanged) onButtonGroupChanged(arg0, arg1)
    }

    void selectedKeyChanged(Button arg0, String arg1) {
        if(onSelectedKeyChanged) onSelectedKeyChanged(arg0, arg1)
    }

    void stateKeyChanged(Button arg0, String arg1) {
        if(onStateKeyChanged) onStateKeyChanged(arg0, arg1)
    }
}