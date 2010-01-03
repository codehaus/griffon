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
import org.apache.pivot.wtk.Form
import org.apache.pivot.wtk.Form.Flag
import org.apache.pivot.wtk.FormAttributeListener

/**
 * @author Andres Almiray
 */
class FormAttributeListenerAdapter extends BuilderDelegate implements FormAttributeListener {
    private Closure onLabelChanged
    private Closure onFlagChanged
 
    FormAttributeListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onLabelChanged(Closure callback) {
        onLabelChanged = callback
        onLabelChanged.delegate = this
    }

    void onFlagChanged(Closure callback) {
        onFlagChanged = callback
        onFlagChanged.delegate = this
    }

    void labelChanged(Form arg0, Component arg1, String arg2) {
        if(onLabelChanged) onLabelChanged(arg0, arg1, arg2)
    }

    void flagChanged(Form arg0, Component arg1, Flag arg2) {
        if(onFlagChanged) onFlagChanged(arg0, arg1, arg2)
    }
}