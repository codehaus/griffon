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
import org.apache.pivot.util.Vote
import org.apache.pivot.wtk.TableView
import org.apache.pivot.collections.Dictionary
import org.apache.pivot.wtk.TableView.RowEditor
import org.apache.pivot.wtk.TableView.RowEditorListener

/**
 * @author Andres Almiray
 */
class TableViewRowEditorListenerAdapter extends BuilderDelegate implements TableView.RowEditorListener {
    private Closure onPreviewEditRow
    private Closure onEditRowVetoed
    private Closure onRowEditing
    private Closure onPreviewSaveChanges
    private Closure onSaveChangesVetoed
    private Closure onChangesSaved
    private Closure onEditCancelled
 
    TableViewRowEditorListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onPreviewEditRow(Closure callback) {
        onPreviewEditRow = callback
        onPreviewEditRow.delegate = this
    }

    void onEditRowVetoed(Closure callback) {
        onEditRowVetoed = callback
        onEditRowVetoed.delegate = this
    }

    void onRowEditing(Closure callback) {
        onRowEditing = callback
        onRowEditing.delegate = this
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

    Vote previewEditRow(RowEditor arg0, TableView arg1, int arg2, int arg3) {
        if(onPreviewEditRow) onPreviewEditRow(arg0, arg1, arg2, arg3); else Vote.APPROVE
    }

    void editRowVetoed(RowEditor arg0, Vote arg1) {
        if(onEditRowVetoed) onEditRowVetoed(arg0, arg1)
    }

    void rowEditing(RowEditor arg0, TableView arg1, int arg2, int arg3) {
        if(onRowEditing) onRowEditing(arg0, arg1, arg2, arg3)
    }

    Vote previewSaveChanges(RowEditor arg0, TableView arg1, int arg2, int arg3, Dictionary arg4) {
        if(onPreviewSaveChanges) onPreviewSaveChanges(arg0, arg1, arg2, arg3, arg4); else Vote.APPROVE
    }

    void saveChangesVetoed(RowEditor arg0, Vote arg1) {
        if(onSaveChangesVetoed) onSaveChangesVetoed(arg0, arg1)
    }

    void changesSaved(RowEditor arg0, TableView arg1, int arg2, int arg3) {
        if(onChangesSaved) onChangesSaved(arg0, arg1, arg2, arg3)
    }

    void editCancelled(RowEditor arg0, TableView arg1, int arg2, int arg3) {
        if(onEditCancelled) onEditCancelled(arg0, arg1, arg2, arg3)
    }
}