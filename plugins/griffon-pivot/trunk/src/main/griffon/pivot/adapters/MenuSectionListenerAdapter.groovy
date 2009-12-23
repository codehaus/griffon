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
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk.Menu
import org.apache.pivot.wtk.Menu.Section
//import org.apache.pivot.wtk.Menu.SectionListener

/**
 * @author Andres Almiray
 */
class MenuSectionListenerAdapter extends BuilderDelegate implements Menu.SectionListener {
    private Closure onItemInserted
    private Closure onItemsRemoved
    private Closure onNameChanged
 
    MenuSectionListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onItemInserted(Closure callback) {
        onItemInserted = callback
        onItemInserted.delegate = this
    }

    void onItemsRemoved(Closure callback) {
        onItemsRemoved = callback
        onItemsRemoved.delegate = this
    }

    void onNameChanged(Closure callback) {
        onNameChanged = callback
        onNameChanged.delegate = this
    }

    void itemInserted(Section arg0, int arg1) {
        if(onItemInserted) onItemInserted(arg0, arg1)
    }

    void itemsRemoved(Section arg0, int arg1, Sequence arg2) {
        if(onItemsRemoved) onItemsRemoved(arg0, arg1, arg2)
    }

    void nameChanged(Section arg0, String arg1) {
        if(onNameChanged) onNameChanged(arg0, arg1)
    }
}
