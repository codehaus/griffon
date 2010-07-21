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

package griffon.glazedlists.gui

import ca.odell.glazedlists.gui.WritableTableFormat

/**
 * @author Andres Almiray
 */
class DefaultWritableTableFormat implements WritableTableFormat {
    protected static final GET_COLUMN_VALUE_STRATEGY = { target, propertyName, index ->
        if(propertyName) {
            if(propertyName.size() == 1) propertyName = propertyName.toLowerCase()
            else propertyName = propertyName[0].toLowerCase() + propertyName[1..-1]
            return target[propertyName]
        }
        return null
    }

    protected static final SET_COLUMN_VALUE_STRATEGY = { target, propertyName, index, editedValue ->
        editedValue
    }

    protected static final IS_EDITABLE_STRATEGY = { target, propertyName, index ->
        true
    }

    protected final List columnNames = []
    protected final List readers = []
    protected final List writers = []
    protected final List queries = []

    DefaultWritableTableFormat(List columns, Closure getColumnValueStrategy = GET_COLUMN_VALUE_STRATEGY,
                               Closure setColumnValueStrategy = SET_COLUMN_VALUE_STRATEGY,
                               Closure isEditableStrategy = IS_EDITABLE_STRATEGY) {
        this.columnNames.addAll(columns.name)
        def read = getColumnValueStrategy ?: GET_COLUMN_VALUE_STRATEGY
        def write = setColumnValueStrategy ?: SET_COLUMN_VALUE_STRATEGY
        def query = isEditableStrategy ?: IS_EDITABLE_STRATEGY

        columns.collect(readers) { columndef -> columndef.read ?: read }
        columns.collect(writers) { columndef -> columndef.write ?: write }
        columns.collect(queries) { columndef -> columndef.editable ?: query }
    }

    int getColumnCount() {
        columnNames.size()
    }

    String getColumnName(int index) {
        columnNames[index]
    }

    Object getColumnValue(baseObject, int index) {
        readers[index](baseObject, columnNames[index], index)
    } 

    boolean isEditable(baseObject, int index) {
        queries[index](baseObject, columnNames[index], index)
    } 

    Object setColumnValue(baseObject, editedValue, int index) {
        qriters[index](baseObject, columnNames[index], index, editedValue)
    } 
}
