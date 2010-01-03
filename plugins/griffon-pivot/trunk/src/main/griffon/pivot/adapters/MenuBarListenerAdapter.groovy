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
import org.apache.pivot.wtk.MenuBar
import org.apache.pivot.wtk.MenuBar.Item
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk.MenuBarListener

/**
 * @author Andres Almiray
 */
class MenuBarListenerAdapter extends BuilderDelegate implements MenuBarListener {
    private Closure onItemInserted
    private Closure onItemsRemoved
    private Closure onActiveItemChanged
 
    MenuBarListenerAdapter(FactoryBuilderSupport builder) {
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

    void onActiveItemChanged(Closure callback) {
        onActiveItemChanged = callback
        onActiveItemChanged.delegate = this
    }

    void itemInserted(MenuBar arg0, int arg1) {
        if(onItemInserted) onItemInserted(arg0, arg1)
    }

    void itemsRemoved(MenuBar arg0, int arg1, Sequence arg2) {
        if(onItemsRemoved) onItemsRemoved(arg0, arg1, arg2)
    }

    void activeItemChanged(MenuBar arg0, Item arg1) {
        if(onActiveItemChanged) onActiveItemChanged(arg0, arg1)
    }
}