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
import org.apache.pivot.wtk.TextInput
import org.apache.pivot.wtk.text.TextNode
import org.apache.pivot.wtk.TextInputListener
import org.apache.pivot.wtk.text.validation.Validator

/**
 * @author Andres Almiray
 */
class TextInputListenerAdapter extends BuilderDelegate implements TextInputListener {
    private Closure onTextKeyChanged
    private Closure onTextNodeChanged
    private Closure onTextSizeChanged
    private Closure onMaximumLengthChanged
    private Closure onPasswordChanged
    private Closure onPromptChanged
    private Closure onTextValidatorChanged
    private Closure onTextValidChanged
 
    TextInputListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onTextKeyChanged(Closure callback) {
        onTextKeyChanged = callback
        onTextKeyChanged.delegate = this
    }

    void onTextNodeChanged(Closure callback) {
        onTextNodeChanged = callback
        onTextNodeChanged.delegate = this
    }

    void onTextSizeChanged(Closure callback) {
        onTextSizeChanged = callback
        onTextSizeChanged.delegate = this
    }

    void onMaximumLengthChanged(Closure callback) {
        onMaximumLengthChanged = callback
        onMaximumLengthChanged.delegate = this
    }

    void onPasswordChanged(Closure callback) {
        onPasswordChanged = callback
        onPasswordChanged.delegate = this
    }

    void onPromptChanged(Closure callback) {
        onPromptChanged = callback
        onPromptChanged.delegate = this
    }

    void onTextValidatorChanged(Closure callback) {
        onTextValidatorChanged = callback
        onTextValidatorChanged.delegate = this
    }

    void onTextValidChanged(Closure callback) {
        onTextValidChanged = callback
        onTextValidChanged.delegate = this
    }

    void textKeyChanged(TextInput arg0, String arg1) {
        if(onTextKeyChanged) onTextKeyChanged(arg0, arg1)
    }

    void textNodeChanged(TextInput arg0, TextNode arg1) {
        if(onTextNodeChanged) onTextNodeChanged(arg0, arg1)
    }

    void textSizeChanged(TextInput arg0, int arg1) {
        if(onTextSizeChanged) onTextSizeChanged(arg0, arg1)
    }

    void maximumLengthChanged(TextInput arg0, int arg1) {
        if(onMaximumLengthChanged) onMaximumLengthChanged(arg0, arg1)
    }

    void passwordChanged(TextInput arg0) {
        if(onPasswordChanged) onPasswordChanged(arg0)
    }

    void promptChanged(TextInput arg0, String arg1) {
        if(onPromptChanged) onPromptChanged(arg0, arg1)
    }

    void textValidatorChanged(TextInput arg0, Validator arg1) {
        if(onTextValidatorChanged) onTextValidatorChanged(arg0, arg1)
    }

    void textValidChanged(TextInput arg0) {
        if(onTextValidChanged) onTextValidChanged(arg0)
    }
}