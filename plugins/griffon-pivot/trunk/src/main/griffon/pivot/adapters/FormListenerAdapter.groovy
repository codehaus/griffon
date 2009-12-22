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
import org.apache.pivot.wtk.Form
import org.apache.pivot.wtk.Form.Section
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk.FormListener

/**
 * @author Andres Almiray
 */
class FormListenerAdapter extends BuilderDelegate implements FormListener {
    private Closure onSectionInserted
    private Closure onSectionsRemoved
    private Closure onSectionHeadingChanged
    private Closure onFieldInserted
    private Closure onFieldsRemoved
 
    FormListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onSectionInserted(Closure callback) {
        onSectionInserted = callback
        onSectionInserted.delegate = this
    }

    void onSectionsRemoved(Closure callback) {
        onSectionsRemoved = callback
        onSectionsRemoved.delegate = this
    }

    void onSectionHeadingChanged(Closure callback) {
        onSectionHeadingChanged = callback
        onSectionHeadingChanged.delegate = this
    }

    void onFieldInserted(Closure callback) {
        onFieldInserted = callback
        onFieldInserted.delegate = this
    }

    void onFieldsRemoved(Closure callback) {
        onFieldsRemoved = callback
        onFieldsRemoved.delegate = this
    }

    void sectionInserted(Form arg0, int arg1) {
        if(onSectionInserted) onSectionInserted(arg0, arg1)
    }

    void sectionsRemoved(Form arg0, int arg1, Sequence arg2) {
        if(onSectionsRemoved) onSectionsRemoved(arg0, arg1, arg2)
    }

    void sectionHeadingChanged(Section arg0) {
        if(onSectionHeadingChanged) onSectionHeadingChanged(arg0)
    }

    void fieldInserted(Section arg0, int arg1) {
        if(onFieldInserted) onFieldInserted(arg0, arg1)
    }

    void fieldsRemoved(Section arg0, int arg1, Sequence arg2) {
        if(onFieldsRemoved) onFieldsRemoved(arg0, arg1, arg2)
    }
}