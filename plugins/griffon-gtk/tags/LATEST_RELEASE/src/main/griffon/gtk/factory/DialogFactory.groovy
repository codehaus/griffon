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

import org.gnome.gtk.Dialog
import org.gnome.gtk.Window

/**
 * @author Andres Almiray
 */
class DialogFactory extends ContainerFactory {
    DialogFactory() {
        super(Dialog)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(FactoryBuilderSupport.checkValueIsTypeNotString(value, name, beanClass)) {
            return value
        }

        def title = attributes.remove('title') ?: ''
        Window parent = attributes.remove('parent') ?: null
        def modal = attributes.remove('modal')

        if(modal != null && modal && !parent) {
            throw new IllegalArgumentException("In $name it is highly recommended that you set a parent window on a modal dialog")
        }
 
        new Dialog(title, parent, modal ?: false)
    }
}
