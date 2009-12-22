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
import org.apache.pivot.wtk.ListButton
import org.apache.pivot.collections.List
import org.apache.pivot.util.Filter
import org.apache.pivot.wtk.ListButtonListener
import org.apache.pivot.wtk.ListView.ItemRenderer

/**
 * @author Andres Almiray
 */
class ListButtonListenerAdapter extends BuilderDelegate implements ListButtonListener {
    private Closure onListDataChanged
    private Closure onItemRendererChanged
    private Closure onDisabledItemFilterChanged
    private Closure onSelectedItemKeyChanged
 
    ListButtonListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onListDataChanged(Closure callback) {
        onListDataChanged = callback
        onListDataChanged.delegate = this
    }

    void onItemRendererChanged(Closure callback) {
        onItemRendererChanged = callback
        onItemRendererChanged.delegate = this
    }

    void onDisabledItemFilterChanged(Closure callback) {
        onDisabledItemFilterChanged = callback
        onDisabledItemFilterChanged.delegate = this
    }

    void onSelectedItemKeyChanged(Closure callback) {
        onSelectedItemKeyChanged = callback
        onSelectedItemKeyChanged.delegate = this
    }

    void listDataChanged(ListButton arg0, List arg1) {
        if(onListDataChanged) onListDataChanged(arg0, arg1)
    }

    void itemRendererChanged(ListButton arg0, ItemRenderer arg1) {
        if(onItemRendererChanged) onItemRendererChanged(arg0, arg1)
    }

    void disabledItemFilterChanged(ListButton arg0, Filter arg1) {
        if(onDisabledItemFilterChanged) onDisabledItemFilterChanged(arg0, arg1)
    }

    void selectedItemKeyChanged(ListButton arg0, String arg1) {
        if(onSelectedItemKeyChanged) onSelectedItemKeyChanged(arg0, arg1)
    }
}