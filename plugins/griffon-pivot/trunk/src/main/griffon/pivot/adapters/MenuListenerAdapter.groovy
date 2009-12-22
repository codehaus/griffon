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
import org.apache.pivot.wtk.Menu
import org.apache.pivot.wtk.Menu.Item
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk.MenuListener

/**
 * @author Andres Almiray
 */
class MenuListenerAdapter extends BuilderDelegate implements MenuListener {
    private Closure onActiveItemChanged
    private Closure onSectionInserted
    private Closure onSectionsRemoved
 
    MenuListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onActiveItemChanged(Closure callback) {
        onActiveItemChanged = callback
        onActiveItemChanged.delegate = this
    }

    void onSectionInserted(Closure callback) {
        onSectionInserted = callback
        onSectionInserted.delegate = this
    }

    void onSectionsRemoved(Closure callback) {
        onSectionsRemoved = callback
        onSectionsRemoved.delegate = this
    }

    void activeItemChanged(Menu arg0, Item arg1) {
        if(onActiveItemChanged) onActiveItemChanged(arg0, arg1)
    }

    void sectionInserted(Menu arg0, int arg1) {
        if(onSectionInserted) onSectionInserted(arg0, arg1)
    }

    void sectionsRemoved(Menu arg0, int arg1, Sequence arg2) {
        if(onSectionsRemoved) onSectionsRemoved(arg0, arg1, arg2)
    }
}