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
import org.apache.pivot.wtk.ListView
import org.apache.pivot.wtk.ListView.SelectMode
import org.apache.pivot.collections.List
import org.apache.pivot.util.Filter
import org.apache.pivot.wtk.ListView.ItemEditor
import org.apache.pivot.wtk.ListView.ItemRenderer
import org.apache.pivot.wtk.ListViewListener

/**
 * @author Andres Almiray
 */
class ListViewListenerAdapter extends BuilderDelegate implements ListViewListener {
    private Closure onListDataChanged
    private Closure onItemRendererChanged
    private Closure onItemEditorChanged
    private Closure onSelectModeChanged
    private Closure onCheckmarksEnabledChanged
    private Closure onDisabledItemFilterChanged
    private Closure onDisabledCheckmarkFilterChanged
    private Closure onSelectedItemKeyChanged
    private Closure onSelectedItemsKeyChanged
 
    ListViewListenerAdapter(FactoryBuilderSupport builder) {
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

    void onItemEditorChanged(Closure callback) {
        onItemEditorChanged = callback
        onItemEditorChanged.delegate = this
    }

    void onSelectModeChanged(Closure callback) {
        onSelectModeChanged = callback
        onSelectModeChanged.delegate = this
    }

    void onCheckmarksEnabledChanged(Closure callback) {
        onCheckmarksEnabledChanged = callback
        onCheckmarksEnabledChanged.delegate = this
    }

    void onDisabledItemFilterChanged(Closure callback) {
        onDisabledItemFilterChanged = callback
        onDisabledItemFilterChanged.delegate = this
    }

    void onDisabledCheckmarkFilterChanged(Closure callback) {
        onDisabledCheckmarkFilterChanged = callback
        onDisabledCheckmarkFilterChanged.delegate = this
    }

    void onSelectedItemKeyChanged(Closure callback) {
        onSelectedItemKeyChanged = callback
        onSelectedItemKeyChanged.delegate = this
    }

    void onSelectedItemsKeyChanged(Closure callback) {
        onSelectedItemsKeyChanged = callback
        onSelectedItemsKeyChanged.delegate = this
    }

    void listDataChanged(ListView arg0, List arg1) {
        if(onListDataChanged) onListDataChanged(arg0, arg1)
    }

    void itemRendererChanged(ListView arg0, ItemRenderer arg1) {
        if(onItemRendererChanged) onItemRendererChanged(arg0, arg1)
    }

    void itemEditorChanged(ListView arg0, ItemEditor arg1) {
        if(onItemEditorChanged) onItemEditorChanged(arg0, arg1)
    }

    void selectModeChanged(ListView arg0, SelectMode arg1) {
        if(onSelectModeChanged) onSelectModeChanged(arg0, arg1)
    }

    void checkmarksEnabledChanged(ListView arg0) {
        if(onCheckmarksEnabledChanged) onCheckmarksEnabledChanged(arg0)
    }

    void disabledItemFilterChanged(ListView arg0, Filter arg1) {
        if(onDisabledItemFilterChanged) onDisabledItemFilterChanged(arg0, arg1)
    }

    void disabledCheckmarkFilterChanged(ListView arg0, Filter arg1) {
        if(onDisabledCheckmarkFilterChanged) onDisabledCheckmarkFilterChanged(arg0, arg1)
    }

    void selectedItemKeyChanged(ListView arg0, String arg1) {
        if(onSelectedItemKeyChanged) onSelectedItemKeyChanged(arg0, arg1)
    }

    void selectedItemsKeyChanged(ListView arg0, String arg1) {
        if(onSelectedItemsKeyChanged) onSelectedItemsKeyChanged(arg0, arg1)
    }
}