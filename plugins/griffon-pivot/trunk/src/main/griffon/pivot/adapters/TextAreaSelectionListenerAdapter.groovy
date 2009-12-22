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
import org.apache.pivot.wtk.TextArea
import org.apache.pivot.wtk.TextAreaSelectionListener

/**
 * @author Andres Almiray
 */
class TextAreaSelectionListenerAdapter extends BuilderDelegate implements TextAreaSelectionListener {
    private Closure onSelectionChanged
 
    TextAreaSelectionListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onSelectionChanged(Closure callback) {
        onSelectionChanged = callback
        onSelectionChanged.delegate = this
    }

    void selectionChanged(TextArea arg0, int arg1, int arg2) {
        if(onSelectionChanged) onSelectionChanged(arg0, arg1, arg2)
    }
}