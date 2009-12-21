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

import griffon.pivot.PivotUtils
import org.apache.pivot.wtk.*

/**
 * @author Andres Almiray
 */
class PivotBeanFactory extends BeanFactory {
    PivotBeanFactory(Class beanClass) {
        super(beanClass, false)
    }

    PivotBeanFactory(Class beanClass, boolean leaf) {
        super(beanClass, leaf)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        Object bean = super.newInstance(builder, name, value, attributes)
        if (value instanceof String) {
            try {
                bean.text = value
            } catch (MissingPropertyException mpe) {
                throw new RuntimeException("In $name value argument of type String cannot be applied to property text:");
            }
        }
        return bean
    }

    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        attributes.each { property, value ->
            if(!PivotUtils.applyAsEventListener(property, value, node)) {
                PivotUtils.setBeanProperty(property, value, node)
            }
        }
       
        return false
    }
}
