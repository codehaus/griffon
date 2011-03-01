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

package griffon.glazedlists.factory

import griffon.glazedlists.gui.DefaultTableFormat

/**
 * @author Andres Almiray
 */
class DefaultTableFormatFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
            throws InstantiationException, IllegalAccessException {
        if(FactoryBuilderSupport.checkValueIsTypeNotString(value, name, DefaultTableFormat)) {
            return value
        }

        if(attributes.containsKey('columns')) {
            def columns = attributes.remove('columns')
            if(columns instanceof List) {
                return new DefaultTableFormat(columns.name, columns.title, attributes.remove('read'))
            }
            throw new IllegalArgumentException("In $name the value of columns: must be a List with the following format [[name: 'firstName, title: 'First Name'], [name: 'lastName', title: 'Last Name']]")
        }

        if(!attributes.containsKey('columnNames')) {
            throw new IllegalArgumentException("In $name you must define a value for columnNames:, i.e, ['ColumnA', 'ColumnB']")
        }
        List columnNames = attributes.remove('columnNames')
        return new DefaultTableFormat(columnNames, attributes.remove('read'))
    }

    boolean isLeaf() {
        true
    }
}
