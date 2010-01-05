/*
 * Copyright 2009-2010 the original author or authors.
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

package griffon.gtk.factory

import org.gnome.gtk.ActionGroup
import org.gnome.gtk.Action

/**
 * @author Andres Almiray
 */
class ActionGroupFactory extends GtkBeanFactory {
    ActionGroupFactory() {
        super(ActionGroup)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(value instanceof GString) value = value.toString()
        if(value instanceof String) return new ActionGroup(value)
        if(FactoryBuilderSupport.checkValueIsTypeNotString(value, name, beanClass)) {
            return value
        }

        def groupName = attributes.remove('name') ?: ''
        if(!groupName) {
            throw new IllegalArgumentException("In $name you must define a value or name: property of type String")
        }
 
        new ActionGroup(groupName)
    }

    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if(child instanceof Action) parent.addAction(child)
    }
}
