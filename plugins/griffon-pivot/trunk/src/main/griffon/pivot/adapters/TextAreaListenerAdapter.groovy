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
import org.apache.pivot.wtk.TextArea
import org.apache.pivot.wtk.text.Document
import org.apache.pivot.wtk.TextAreaListener

/**
 * @author Andres Almiray
 */
class TextAreaListenerAdapter extends BuilderDelegate implements TextAreaListener {
    private Closure onTextKeyChanged
    private Closure onDocumentChanged
    private Closure onEditableChanged
 
    TextAreaListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onTextKeyChanged(Closure callback) {
        onTextKeyChanged = callback
        onTextKeyChanged.delegate = this
    }

    void onDocumentChanged(Closure callback) {
        onDocumentChanged = callback
        onDocumentChanged.delegate = this
    }

    void onEditableChanged(Closure callback) {
        onEditableChanged = callback
        onEditableChanged.delegate = this
    }

    void textKeyChanged(TextArea arg0, String arg1) {
        if(onTextKeyChanged) onTextKeyChanged(arg0, arg1)
    }

    void documentChanged(TextArea arg0, Document arg1) {
        if(onDocumentChanged) onDocumentChanged(arg0, arg1)
    }

    void editableChanged(TextArea arg0) {
        if(onEditableChanged) onEditableChanged(arg0)
    }
}