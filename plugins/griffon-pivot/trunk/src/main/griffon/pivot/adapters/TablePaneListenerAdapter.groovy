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
import org.apache.pivot.wtk.Component
import org.apache.pivot.wtk.TablePane
import org.apache.pivot.wtk.TablePane.Column
import org.apache.pivot.wtk.TablePane.Row
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk.TablePaneListener

/**
 * @author Andres Almiray
 */
class TablePaneListenerAdapter extends BuilderDelegate implements TablePaneListener {
    private Closure onRowInserted
    private Closure onRowHeightChanged
    private Closure onRowHighlightedChanged
    private Closure onColumnInserted
    private Closure onColumnsRemoved
    private Closure onColumnWidthChanged
    private Closure onColumnHighlightedChanged
    private Closure onCellInserted
    private Closure onCellsRemoved
    private Closure onCellUpdated
    private Closure onRowsRemoved
 
    TablePaneListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onRowInserted(Closure callback) {
        onRowInserted = callback
        onRowInserted.delegate = this
    }

    void onRowHeightChanged(Closure callback) {
        onRowHeightChanged = callback
        onRowHeightChanged.delegate = this
    }

    void onRowHighlightedChanged(Closure callback) {
        onRowHighlightedChanged = callback
        onRowHighlightedChanged.delegate = this
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

    void onColumnHighlightedChanged(Closure callback) {
        onColumnHighlightedChanged = callback
        onColumnHighlightedChanged.delegate = this
    }

    void onCellInserted(Closure callback) {
        onCellInserted = callback
        onCellInserted.delegate = this
    }

    void onCellsRemoved(Closure callback) {
        onCellsRemoved = callback
        onCellsRemoved.delegate = this
    }

    void onCellUpdated(Closure callback) {
        onCellUpdated = callback
        onCellUpdated.delegate = this
    }

    void onRowsRemoved(Closure callback) {
        onRowsRemoved = callback
        onRowsRemoved.delegate = this
    }

    void rowInserted(TablePane arg0, int arg1) {
        if(onRowInserted) onRowInserted(arg0, arg1)
    }

    void rowHeightChanged(Row arg0, int arg1, boolean arg2) {
        if(onRowHeightChanged) onRowHeightChanged(arg0, arg1, arg2)
    }

    void rowHighlightedChanged(Row arg0) {
        if(onRowHighlightedChanged) onRowHighlightedChanged(arg0)
    }

    void columnInserted(TablePane arg0, int arg1) {
        if(onColumnInserted) onColumnInserted(arg0, arg1)
    }

    void columnsRemoved(TablePane arg0, int arg1, Sequence arg2) {
        if(onColumnsRemoved) onColumnsRemoved(arg0, arg1, arg2)
    }

    void columnWidthChanged(Column arg0, int arg1, boolean arg2) {
        if(onColumnWidthChanged) onColumnWidthChanged(arg0, arg1, arg2)
    }

    void columnHighlightedChanged(Column arg0) {
        if(onColumnHighlightedChanged) onColumnHighlightedChanged(arg0)
    }

    void cellInserted(Row arg0, int arg1) {
        if(onCellInserted) onCellInserted(arg0, arg1)
    }

    void cellsRemoved(Row arg0, int arg1, Sequence arg2) {
        if(onCellsRemoved) onCellsRemoved(arg0, arg1, arg2)
    }

    void cellUpdated(Row arg0, int arg1, Component arg2) {
        if(onCellUpdated) onCellUpdated(arg0, arg1, arg2)
    }

    void rowsRemoved(TablePane arg0, int arg1, Sequence arg2) {
        if(onRowsRemoved) onRowsRemoved(arg0, arg1, arg2)
    }
}