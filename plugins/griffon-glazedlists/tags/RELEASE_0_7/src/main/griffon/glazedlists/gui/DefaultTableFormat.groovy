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

import griffon.util.GriffonNameUtils
import ca.odell.glazedlists.gui.TableFormat

/**
 * @author Andres Almiray
 */
class DefaultTableFormat implements TableFormat {
    protected static final GET_COLUMN_VALUE_STRATEGY = { target, columns, index ->
        def propertyName = columns[index]
        if(propertyName) {
            if(propertyName.size() == 1) propertyName = propertyName.toLowerCase()
            else propertyName = propertyName[0].toLowerCase() + propertyName[1..-1]
            return target[propertyName]
        }
        return null
    }

    protected final List<String> columnNames = []
    protected final List<String> columnTitles = []
    protected final Closure getColumnValueStrategy

    DefaultTableFormat(List<String> columnNames, Closure getColumnValueStrategy = GET_COLUMN_VALUE_STRATEGY) {
        this(columnNames, columnNames, getColumnValueStrategy)
    }   
    
    DefaultTableFormat(List<String> columnNames, List<String> columnTitles, Closure getColumnValueStrategy = GET_COLUMN_VALUE_STRATEGY) {
        if(!columnTitles) columnTitles = columnNames
        assert columnNames.size() == columnTitles.size()
        columnNames.collect(this.columnNames) { GriffonNameUtils.getPropertyName(it) }
        for(int i = 0; i < columnNames.size(); i++) {
            String title = columnTitles[i]
            this.columnTitles << (title != null ? title : GriffonNameUtils.getNaturalName(columnNames[i]))
        }
        this.getColumnValueStrategy = getColumnValueStrategy ?: GET_COLUMN_VALUE_STRATEGY
    }

    int getColumnCount() {
        columnNames.size()
    }

    String getColumnName(int index) {
        columnTitles[index]
    }

    Object getColumnValue(baseObject, int index) {
        getColumnValueStrategy(baseObject, columnNames, index)
    } 
}
