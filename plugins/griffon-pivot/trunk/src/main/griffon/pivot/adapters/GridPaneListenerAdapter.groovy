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
import org.apache.pivot.wtk.GridPane
import org.apache.pivot.wtk.GridPane.Row
import org.apache.pivot.collections.Sequence
import org.apache.pivot.wtk.GridPaneListener

/**
 * @author Andres Almiray
 */
class GridPaneListenerAdapter extends BuilderDelegate implements GridPaneListener {
    private Closure onRowInserted
    private Closure onCellInserted
    private Closure onCellsRemoved
    private Closure onCellUpdated
    private Closure onColumnCountChanged
    private Closure onRowsRemoved
 
    GridPaneListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onRowInserted(Closure callback) {
        onRowInserted = callback
        onRowInserted.delegate = this
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

    void onColumnCountChanged(Closure callback) {
        onColumnCountChanged = callback
        onColumnCountChanged.delegate = this
    }

    void onRowsRemoved(Closure callback) {
        onRowsRemoved = callback
        onRowsRemoved.delegate = this
    }

    void rowInserted(GridPane arg0, int arg1) {
        if(onRowInserted) onRowInserted(arg0, arg1)
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

    void columnCountChanged(GridPane arg0, int arg1) {
        if(onColumnCountChanged) onColumnCountChanged(arg0, arg1)
    }

    void rowsRemoved(GridPane arg0, int arg1, Sequence arg2) {
        if(onRowsRemoved) onRowsRemoved(arg0, arg1, arg2)
    }
}