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
import org.apache.pivot.wtk.TableView
import org.apache.pivot.wtk.TableViewRowListener

/**
 * @author Andres Almiray
 */
class TableViewRowListenerAdapter extends BuilderDelegate implements TableViewRowListener {
    private Closure onRowInserted
    private Closure onRowUpdated
    private Closure onRowsCleared
    private Closure onRowsSorted
    private Closure onRowsRemoved
 
    TableViewRowListenerAdapter(FactoryBuilderSupport builder) {
        super(builder)
    }

    void onRowInserted(Closure callback) {
        onRowInserted = callback
        onRowInserted.delegate = this
    }

    void onRowUpdated(Closure callback) {
        onRowUpdated = callback
        onRowUpdated.delegate = this
    }

    void onRowsCleared(Closure callback) {
        onRowsCleared = callback
        onRowsCleared.delegate = this
    }

    void onRowsSorted(Closure callback) {
        onRowsSorted = callback
        onRowsSorted.delegate = this
    }

    void onRowsRemoved(Closure callback) {
        onRowsRemoved = callback
        onRowsRemoved.delegate = this
    }

    void rowInserted(TableView arg0, int arg1) {
        if(onRowInserted) onRowInserted(arg0, arg1)
    }

    void rowUpdated(TableView arg0, int arg1) {
        if(onRowUpdated) onRowUpdated(arg0, arg1)
    }

    void rowsCleared(TableView arg0) {
        if(onRowsCleared) onRowsCleared(arg0)
    }

    void rowsSorted(TableView arg0) {
        if(onRowsSorted) onRowsSorted(arg0)
    }

    void rowsRemoved(TableView arg0, int arg1, int arg2) {
        if(onRowsRemoved) onRowsRemoved(arg0, arg1, arg2)
    }
}