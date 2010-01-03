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
import org.apache.pivot.util.Vote
import org.apache.pivot.wtk.ListView
import org.apache.pivot.wtk.ListView.ItemEditor
import org.apache.pivot.wtk.ListView.ItemEditorListener

/**
 * @author Andres Almiray
 */
class ListViewItemEditorListenerAdapter extends BuilderDelegate implements ListView.ItemEditorListener {
    private Closure onPreviewSaveChanges
    private Closure onSaveChangesVetoed
    private Closure onChangesSaved
    private Closure onEditCancelled
    private Closure onPreviewEditItem
    private Closure onEditItemVetoed
    private Closure onItemEditing
 
    ListViewItemEditorListenerAdapter(FactoryBuilderSupport builder) {
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

    void onPreviewEditItem(Closure callback) {
        onPreviewEditItem = callback
        onPreviewEditItem.delegate = this
    }

    void onEditItemVetoed(Closure callback) {
        onEditItemVetoed = callback
        onEditItemVetoed.delegate = this
    }

    void onItemEditing(Closure callback) {
        onItemEditing = callback
        onItemEditing.delegate = this
    }

    Vote previewSaveChanges(ItemEditor arg0, ListView arg1, int arg2, Object arg3) {
        if(onPreviewSaveChanges) onPreviewSaveChanges(arg0, arg1, arg2, arg3); else Vote.APPROVE
    }

    void saveChangesVetoed(ItemEditor arg0, Vote arg1) {
        if(onSaveChangesVetoed) onSaveChangesVetoed(arg0, arg1)
    }

    void changesSaved(ItemEditor arg0, ListView arg1, int arg2) {
        if(onChangesSaved) onChangesSaved(arg0, arg1, arg2)
    }

    void editCancelled(ItemEditor arg0, ListView arg1, int arg2) {
        if(onEditCancelled) onEditCancelled(arg0, arg1, arg2)
    }

    Vote previewEditItem(ItemEditor arg0, ListView arg1, int arg2) {
        if(onPreviewEditItem) onPreviewEditItem(arg0, arg1, arg2); else Vote.APPROVE
    }

    void editItemVetoed(ItemEditor arg0, Vote arg1) {
        if(onEditItemVetoed) onEditItemVetoed(arg0, arg1)
    }

    void itemEditing(ItemEditor arg0, ListView arg1, int arg2) {
        if(onItemEditing) onItemEditing(arg0, arg1, arg2)
    }
}