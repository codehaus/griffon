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

import ca.odell.glazedlists.gui.AdvancedTableFormat
import ca.odell.glazedlists.gui.WritableTableFormat

/**
 * @author Andres Almiray
 */
class DefaultWritableTableFormat implements WritableTableFormat, AdvancedTableFormat {
    private static final Class DEFAULT_CLASS = Object
    private static final Comparator DEFAULT_COMPARATOR = { a, b -> a <=> b } as Comparator

    protected static final GET_COLUMN_VALUE_STRATEGY = { target, columns, index ->
        def propertyName = columns[index]
        if(propertyName) {
            if(propertyName.size() == 1) propertyName = propertyName.toLowerCase()
            else propertyName = propertyName[0].toLowerCase() + propertyName[1..-1]
            return target[propertyName]
        }
        return null
    }

    protected static final SET_COLUMN_VALUE_STRATEGY = { target, columns, index, editedValue ->
        def propertyName = columns[index]
        if(propertyName) {
            if(propertyName.size() == 1) propertyName = propertyName.toLowerCase()
            else propertyName = propertyName[0].toLowerCase() + propertyName[1..-1]
            target[propertyName] = editedValue
        }
		return target
    }

    protected static final IS_EDITABLE_STRATEGY = { target, columns, index ->
        true
    }

    protected final List columnNames = []
    protected final List readers = []
    protected final List writers = []
    protected final List queries = []
    protected final List classes = []
    protected final List comparators = []

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
        columns.collect(classes) { columndef -> columndef.class ?: DEFAULT_CLASS }
        columns.collect(comparators) { columndef -> columndef.comparator ?: DEFAULT_COMPARATOR }
    }

    int getColumnCount() {
        columnNames.size()
    }

    String getColumnName(int index) {
        columnNames[index]
    }

    Object getColumnValue(baseObject, int index) {
        readers[index](baseObject, columnNames, index)
    }

    boolean isEditable(baseObject, int index) {
        if(queries[index] instanceof Closure)
            queries[index](baseObject, columnNames, index)
        else
            queries[index].asBoolean()
    }

    Object setColumnValue(baseObject, editedValue, int index) {
        writers[index](baseObject, columnNames, index, editedValue)
    }

    Class getColumnClass(int index) {
        if(classes[index] instanceof Closure)
            classes[index](columnNames, index)
        else
            classes[index]
    }

    Comparator getColumnComparator(int index) {
      if(comparators[index] instanceof Closure)
          comparators[index](columnNames, index)
      else
          comparators[index]
    }
}
