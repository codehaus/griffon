/*
 * Copyright 2003-2009 the original author or authors.
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

package griffon.pivot.factory

/**
 * Instantiates a bean class, optionally setting its text: property
 * if the supplied value is a String or a GString.<p>
 *
 * Copied from SwingBuilder to avoid dependency on Swing module.
 *
 * @author <a href="mailto:shemnon@yahoo.com">Danno Ferrin</a>
 * @version $Revision: 7953 $
 * @since Groovy 1.1
 */
class BeanFactory extends AbstractFactory {
    final Class beanClass
    final protected boolean leaf

    BeanFactory(Class beanClass) {
        this(beanClass, false)
    }

    BeanFactory(Class beanClass, boolean leaf) {
        this.beanClass = beanClass
        this.leaf = leaf
    }

    boolean isLeaf() {
        return leaf
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (value instanceof GString) value = value as String
        if (FactoryBuilderSupport.checkValueIsTypeNotString(value, name, beanClass)) {
            return value
        }
        beanClass.newInstance()
    }
}
