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
import org.apache.pivot.collections.Sequence$Tree.Path
import org.apache.pivot.util.Vote
import org.apache.pivot.wtk.TreeView
import org.apache.pivot.wtk.TreeView.NodeEditor
import org.apache.pivot.wtk.TreeView.NodeEditorListener

/**
 * @author Andres Almiray
 */
class TreeViewNodeEditorListenerAdapter extends BuilderDelegate implements TreeView.NodeEditorListener {
    private Closure onPreviewSaveChanges
    private Closure onSaveChangesVetoed
    private Closure onChangesSaved
    private Closure onEditCancelled
    private Closure onPreviewEditNode
    private Closure onEditNodeVetoed
    private Closure onNodeEditing
 
    TreeViewNodeEditorListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onPreviewSaveChanges(Closure callback) {
        onPreviewSaveChanges = callback
        onPreviewSaveChanges.delegate = this
    }

    void onSaveChangesVetoed(Closure callback) {
        onSaveChangesVetoed = callback
        onSaveChangesVetoed.delegate = this
    }

    void onChangesSaved(Closure callback) {
        onChangesSaved = callback
        onChangesSaved.delegate = this
    }

    void onEditCancelled(Closure callback) {
        onEditCancelled = callback
        onEditCancelled.delegate = this
    }

    void onPreviewEditNode(Closure callback) {
        onPreviewEditNode = callback
        onPreviewEditNode.delegate = this
    }

    void onEditNodeVetoed(Closure callback) {
        onEditNodeVetoed = callback
        onEditNodeVetoed.delegate = this
    }

    void onNodeEditing(Closure callback) {
        onNodeEditing = callback
        onNodeEditing.delegate = this
    }

    Vote previewSaveChanges(NodeEditor arg0, TreeView arg1, Path arg2, Object arg3) {
        if(onPreviewSaveChanges) onPreviewSaveChanges(arg0, arg1, arg2, arg3); else Vote.APPROVE
    }

    void saveChangesVetoed(NodeEditor arg0, Vote arg1) {
        if(onSaveChangesVetoed) onSaveChangesVetoed(arg0, arg1)
    }

    void changesSaved(NodeEditor arg0, TreeView arg1, Path arg2) {
        if(onChangesSaved) onChangesSaved(arg0, arg1, arg2)
    }

    void editCancelled(NodeEditor arg0, TreeView arg1, Path arg2) {
        if(onEditCancelled) onEditCancelled(arg0, arg1, arg2)
    }

    Vote previewEditNode(NodeEditor arg0, TreeView arg1, Path arg2) {
        if(onPreviewEditNode) onPreviewEditNode(arg0, arg1, arg2); else Vote.APPROVE
    }

    void editNodeVetoed(NodeEditor arg0, Vote arg1) {
        if(onEditNodeVetoed) onEditNodeVetoed(arg0, arg1)
    }

    void nodeEditing(NodeEditor arg0, TreeView arg1, Path arg2) {
        if(onNodeEditing) onNodeEditing(arg0, arg1, arg2)
    }
}