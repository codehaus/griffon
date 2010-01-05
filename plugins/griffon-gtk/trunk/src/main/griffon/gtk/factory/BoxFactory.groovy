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

/**
 * @author Andres Almiray
 */
class BoxFactory extends ContainerFactory {
    private static final Class[] PARAMS = [Boolean.TYPE, Integer.TYPE] as Class[]

    BoxFactory(Class beanClass) {
        super(beanClass)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes)
             throws InstantiationException, IllegalAccessException {
        if(value && adapterClass.isAssignableFrom(value.getClass())) return value
        boolean homogeneous = toBoolean(attributes.remove('homogeneous') ?: false)
        int spacing = toInteger(attributes.remove('spacing') ?: 0)
        beanClass.getDeclaredConstructor(PARAMS).newInstance([homogeneous, spacing] as Object[])
    }
}
