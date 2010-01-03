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
import org.apache.pivot.wtk.TableView.Column
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk.TableView.CellRenderer
import org.apache.pivot.wtk.TableViewColumnListener

/**
 * @author Andres Almiray
 */
class TableViewColumnListenerAdapter extends BuilderDelegate implements TableViewColumnListener {
    private Closure onColumnInserted
    private Closure onColumnsRemoved
    private Closure onColumnWidthChanged
    private Closure onColumnNameChanged
    private Closure onColumnHeaderDataChanged
    private Closure onColumnWidthLimitsChanged
    private Closure onColumnFilterChanged
    private Closure onColumnCellRendererChanged
 
    TableViewColumnListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onColumnInserted(Closure callback) {
        onColumnInserted = callback
        onColumnInserted.delegate = this
    }

    void onColumnsRemoved(Closure callback) {
        onColumnsRemoved = callback
        onColumnsRemoved.delegate = this
    }

    void onColumnWidthChanged(Closure callback) {
        onColumnWidthChanged = callback
        onColumnWidthChanged.delegate = this
    }

    void onColumnNameChanged(Closure callback) {
        onColumnNameChanged = callback
        onColumnNameChanged.delegate = this
    }

    void onColumnHeaderDataChanged(Closure callback) {
        onColumnHeaderDataChanged = callback
        onColumnHeaderDataChanged.delegate = this
    }

    void onColumnWidthLimitsChanged(Closure callback) {
        onColumnWidthLimitsChanged = callback
        onColumnWidthLimitsChanged.delegate = this
    }

    void onColumnFilterChanged(Closure callback) {
        onColumnFilterChanged = callback
        onColumnFilterChanged.delegate = this
    }

    void onColumnCellRendererChanged(Closure callback) {
        onColumnCellRendererChanged = callback
        onColumnCellRendererChanged.delegate = this
    }

    void columnInserted(TableView arg0, int arg1) {
        if(onColumnInserted) onColumnInserted(arg0, arg1)
    }

    void columnsRemoved(TableView arg0, int arg1, Sequence arg2) {
        if(onColumnsRemoved) onColumnsRemoved(arg0, arg1, arg2)
    }

    void columnWidthChanged(Column arg0, int arg1, boolean arg2) {
        if(onColumnWidthChanged) onColumnWidthChanged(arg0, arg1, arg2)
    }

    void columnNameChanged(Column arg0, String arg1) {
        if(onColumnNameChanged) onColumnNameChanged(arg0, arg1)
    }

    void columnHeaderDataChanged(Column arg0, Object arg1) {
        if(onColumnHeaderDataChanged) onColumnHeaderDataChanged(arg0, arg1)
    }

    void columnWidthLimitsChanged(Column arg0, int arg1, int arg2) {
        if(onColumnWidthLimitsChanged) onColumnWidthLimitsChanged(arg0, arg1, arg2)
    }

    void columnFilterChanged(Column arg0, Object arg1) {
        if(onColumnFilterChanged) onColumnFilterChanged(arg0, arg1)
    }

    void columnCellRendererChanged(Column arg0, CellRenderer arg1) {
        if(onColumnCellRendererChanged) onColumnCellRendererChanged(arg0, arg1)
    }
}