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

import org.apache.pivot.wtk.*

/**
 * @author Andres Almiray
 */
class ButtonFactory extends PivotBeanFactory {
    ButtonFactory(Class beanClass) {
        super(beanClass, false)
    }

    ButtonFactory(Class beanClass, boolean leaf) {
        super(beanClass, leaf)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(value instanceof GString) value = value as String
        if(FactoryBuilderSupport.checkValueIsTypeNotString(value, name, beanClass)) {
            return value
        }
        Object bean = beanClass.newInstance()
        if (value instanceof String) {
            try {
                bean.buttonData = value
            } catch (MissingPropertyException mpe) {
                throw new RuntimeException("In $name value argument of type String cannot be applied to property buttonData:");
            }
        }

        if(attributes.containsKey('actionId')) {
            bean.setAction((String) attributes.remove('actionId').toString())
        }

        return bean
    }
}
