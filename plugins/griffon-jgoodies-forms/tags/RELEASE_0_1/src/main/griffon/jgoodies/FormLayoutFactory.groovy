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

package griffon.jgoodies

import groovy.swing.factory.LayoutFactory
import com.jgoodies.forms.layout.FormLayout
import com.jgoodies.forms.layout.ColumnSpec
import com.jgoodies.forms.layout.RowSpec
import com.jgoodies.forms.layout.CellConstraints

/**
 * @author Andres.Almiray
 */
class FormLayoutFactory extends LayoutFactory {
    FormLayoutFactory() {
        super(FormLayout)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(value instanceof FormLayout) {
            return value
        } else if(value instanceof String || value instanceof GString) {
            return new FormLayout(value.toString())
        } else if(value instanceof String[]) {
            switch(value.size()) {
                case 1: return new FormLayout(value[0])
                case 2: return new FormLayout(value[0], value[1])
                default: throw new IllegalArgumentException("Too many arguments in $name, specify 1 or 2 elements.")
            }
        } else if(value instanceof List) {
            switch(value.size()) {
                case 1: return new FormLayout(value[0]?.toString())
                case 2: return new FormLayout(value[0]?.toString(), value[1]?.toString())
                default: throw new IllegalArgumentException("Too many arguments in $name, specify 1 or 2 elements.")
            }
        } else if(value instanceof ColumnSpec[]) {
            return new FormLayout(value)
        }

        def columns = attributes.remove("columns")
        if(!columns) throw new IllegalArgumentException("In $name you must define a value for columns: at least.")
        switch(columns) {
            case ColumnSpec[]:
            case String: 
                break
            case GSring: 
                columns = columns.toString()
                break
            case List:
                columns = columns.join(",")
            default: throw new IllegalArgumentException("In $name, invalid value for columns: $columns")
        }
        def rows = attributes.remove("rows")
        if(!rows) return new FormLayout(columns)
        switch(rows) {
            case RowSpec[]:
            case String: 
                break
            case GSring: 
                rows = rows.toString()
                break
            case List:
                rows = rows.join(",")
            default: throw new IllegalArgumentException("In $name, invalid value for rows: $rows")
        }
        return new FormLayout(columns, rows)
    }
}

/**
 * @author Andres.Almiray
 */
class CellConstraintsFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        int x = 1i
        int y = 1i
        int w = 1i
        int h = 1i

        if(attributes.containsKey("x")) x = attributes.remove("x") as int
        if(attributes.containsKey("y")) y = attributes.remove("y") as int
        if(attributes.containsKey("w")) w = attributes.remove("w") as int
        if(attributes.containsKey("h")) h = attributes.remove("h") as int

        if(attributes.containsKey("valign") && attributes.containsKey("halign")) {
            return new CellConstraints().xywh(x, y, w, h, attributes.remove("halign").toString(), attributes.remove("valign").toString())
        } else if(attributes.containsKey("align")) {
            return new CellConstraints().xywh(x, y, w, h, attributes.remove("align").toString())
        } else {
            return new CellConstraints().xywh(x, y, w, h)
        }

        return new CellConstraints()
    }
}
