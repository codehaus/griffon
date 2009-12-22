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

package griffon.pivot.factory

import org.apache.pivot.wtkx.XMLSerializer
import java.lang.reflect.Field

/**
 * @author Andres Almiray
 */
class WTKXFactory extends AbstractFactory {
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(value instanceof GString) value = value.toString()
        if(!value) {
            if(attributes.containsKey('src')) {
                value = attributes.remove('src').toString()
            } else {
                throw new IllegalArgumentException("In $name you must define a value for src: or a node value that points to a wtkx resource.")
            }
        }
        if(!value.startsWith('/')) value = '/' + value

        def wtkx = new WTKXSerializer()
        def root = wtkx.readObject(builder.app, value)
        def rootId = attributes['id'] ? attributes['id'] + '.' : ''

        Field nameObjectsField = WTKXSerializer.class.getDeclaredField('namedObjects')
        namedObjectsField.setAccesible(true)
        namedObjectsField.get(wtkx).each { id, widget ->
            builder.setVariable(rootId + id, widget)
        }

        return root
    }
}
