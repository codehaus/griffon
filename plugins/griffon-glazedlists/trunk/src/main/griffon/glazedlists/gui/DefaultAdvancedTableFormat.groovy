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

/**
 * @author Andres Almiray
 */
class DefaultAdvancedTableFormat extends DefaultTableFormat implements AdvancedTableFormat {
    private static final Class DEFAULT_CLASS = Object
    private static final Comparator DEFAULT_COMPARATOR = { a, b -> a <=> b } as Comparator

    private final List columnClasses = []
    private final List columnComparators = []

    DefaultAdvancedTableFormat(List<Map<String,?>> columns, Closure getColumnValueStrategy = DefaultTableFormat.GET_COLUMN_VALUE_STRATEGY) {
        super(columns.name, columns.title, getColumnValueStrategy ?: DefaultTableFormat.GET_COLUMN_VALUE_STRATEGY)
        columns.collect(columnClasses) { columndef ->
            columndef.class ?: DEFAULT_CLASS
        }
        columns.collect(columnComparators) { columndef ->
            columndef.comparator ?: DEFAULT_COMPARATOR
        }
    }

    Class getColumnClass(int index) {
        columnClasses[index]
    }

    Comparator getColumnComparator(int index) {
        columnComparators[index]
    }
}
