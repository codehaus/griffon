/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.plugins.metawidget.factory

import org.metawidget.swing.*
import static griffon.util.GriffonNameUtils.isBlank

/**
 * @author Andres Almiray
 */
class StubFactory extends AbstractFactory {
    static final String NAME = 'stub'
    
    final boolean leaf = true
    
    static create(FactoryBuilderSupport builder, Object value = null, Map attributes = [:]) {
        newInstance(builder, NAME, value, attributes)
    }
    
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if(value instanceof Stub) return value
        if(value != null) value = value.toString()
        String stubName = attributes.remove('name') ?: value
        if(isBlank(stubName)) {
            throw new IllegalArgumentException("In $name you must specify a value for name: or the node.")
        }
        new Stub(stubName)
    }
}
