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
import org.apache.pivot.wtk.TreeView
import org.apache.pivot.wtk.TreeView.SelectMode
import org.apache.pivot.collections.List
import org.apache.pivot.util.Filter
import org.apache.pivot.wtk.TreeView.NodeEditor
import org.apache.pivot.wtk.TreeView.NodeRenderer
import org.apache.pivot.wtk.TreeViewListener

/**
 * @author Andres Almiray
 */
class TreeViewListenerAdapter extends BuilderDelegate implements TreeViewListener {
    private Closure onSelectModeChanged
    private Closure onCheckmarksEnabledChanged
    private Closure onDisabledCheckmarkFilterChanged
    private Closure onTreeDataChanged
    private Closure onNodeRendererChanged
    private Closure onNodeEditorChanged
    private Closure onShowMixedCheckmarkStateChanged
    private Closure onDisabledNodeFilterChanged
 
    TreeViewListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onSelectModeChanged(Closure callback) {
        onSelectModeChanged = callback
        onSelectModeChanged.delegate = this
    }

    void onCheckmarksEnabledChanged(Closure callback) {
        onCheckmarksEnabledChanged = callback
        onCheckmarksEnabledChanged.delegate = this
    }

    void onDisabledCheckmarkFilterChanged(Closure callback) {
        onDisabledCheckmarkFilterChanged = callback
        onDisabledCheckmarkFilterChanged.delegate = this
    }

    void onTreeDataChanged(Closure callback) {
        onTreeDataChanged = callback
        onTreeDataChanged.delegate = this
    }

    void onNodeRendererChanged(Closure callback) {
        onNodeRendererChanged = callback
        onNodeRendererChanged.delegate = this
    }

    void onNodeEditorChanged(Closure callback) {
        onNodeEditorChanged = callback
        onNodeEditorChanged.delegate = this
    }

    void onShowMixedCheckmarkStateChanged(Closure callback) {
        onShowMixedCheckmarkStateChanged = callback
        onShowMixedCheckmarkStateChanged.delegate = this
    }

    void onDisabledNodeFilterChanged(Closure callback) {
        onDisabledNodeFilterChanged = callback
        onDisabledNodeFilterChanged.delegate = this
    }

    void selectModeChanged(TreeView arg0, SelectMode arg1) {
        if(onSelectModeChanged) onSelectModeChanged(arg0, arg1)
    }

    void checkmarksEnabledChanged(TreeView arg0) {
        if(onCheckmarksEnabledChanged) onCheckmarksEnabledChanged(arg0)
    }

    void disabledCheckmarkFilterChanged(TreeView arg0, Filter arg1) {
        if(onDisabledCheckmarkFilterChanged) onDisabledCheckmarkFilterChanged(arg0, arg1)
    }

    void treeDataChanged(TreeView arg0, List arg1) {
        if(onTreeDataChanged) onTreeDataChanged(arg0, arg1)
    }

    void nodeRendererChanged(TreeView arg0, NodeRenderer arg1) {
        if(onNodeRendererChanged) onNodeRendererChanged(arg0, arg1)
    }

    void nodeEditorChanged(TreeView arg0, NodeEditor arg1) {
        if(onNodeEditorChanged) onNodeEditorChanged(arg0, arg1)
    }

    void showMixedCheckmarkStateChanged(TreeView arg0) {
        if(onShowMixedCheckmarkStateChanged) onShowMixedCheckmarkStateChanged(arg0)
    }

    void disabledNodeFilterChanged(TreeView arg0, Filter arg1) {
        if(onDisabledNodeFilterChanged) onDisabledNodeFilterChanged(arg0, arg1)
    }
}