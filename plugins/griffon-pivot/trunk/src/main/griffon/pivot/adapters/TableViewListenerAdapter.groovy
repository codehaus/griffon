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
import org.apache.pivot.wtk.TableView
import org.apache.pivot.wtk.TableView.SelectMode
import org.apache.pivot.collections.List
import org.apache.pivot.util.Filter
import org.apache.pivot.wtk.TableView.RowEditor
import org.apache.pivot.wtk.TableViewListener

/**
 * @author Andres Almiray
 */
class TableViewListenerAdapter extends BuilderDelegate implements TableViewListener {
    private Closure onSelectModeChanged
    private Closure onTableDataChanged
    private Closure onColumnSourceChanged
    private Closure onRowEditorChanged
    private Closure onDisabledRowFilterChanged
 
    TableViewListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onSelectModeChanged(Closure callback) {
        onSelectModeChanged = callback
        onSelectModeChanged.delegate = this
    }

    void onTableDataChanged(Closure callback) {
        onTableDataChanged = callback
        onTableDataChanged.delegate = this
    }

    void onColumnSourceChanged(Closure callback) {
        onColumnSourceChanged = callback
        onColumnSourceChanged.delegate = this
    }

    void onRowEditorChanged(Closure callback) {
        onRowEditorChanged = callback
        onRowEditorChanged.delegate = this
    }

    void onDisabledRowFilterChanged(Closure callback) {
        onDisabledRowFilterChanged = callback
        onDisabledRowFilterChanged.delegate = this
    }

    void selectModeChanged(TableView arg0, SelectMode arg1) {
        if(onSelectModeChanged) onSelectModeChanged(arg0, arg1)
    }

    void tableDataChanged(TableView arg0, List arg1) {
        if(onTableDataChanged) onTableDataChanged(arg0, arg1)
    }

    void columnSourceChanged(TableView arg0, TableView arg1) {
        if(onColumnSourceChanged) onColumnSourceChanged(arg0, arg1)
    }

    void rowEditorChanged(TableView arg0, RowEditor arg1) {
        if(onRowEditorChanged) onRowEditorChanged(arg0, arg1)
    }

    void disabledRowFilterChanged(TableView arg0, Filter arg1) {
        if(onDisabledRowFilterChanged) onDisabledRowFilterChanged(arg0, arg1)
    }
}