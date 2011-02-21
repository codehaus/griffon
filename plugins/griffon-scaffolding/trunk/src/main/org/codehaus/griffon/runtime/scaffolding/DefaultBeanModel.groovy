/*
 * Copyright 2010 the original author or authors.
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

package org.codehaus.griffon.runtime.scaffolding

import griffon.util.GriffonClassUtils
import griffon.util.GriffonNameUtils

import griffon.plugins.scaffolding.Model

/**
 * @author Andres Almiray
 */
class DefaultBeanModel<T> extends AbstractBeanModel<T> {
    private final Map<String, Model<?>> attributes = new LinkedHashMap<String, Model<?>>()
    private boolean realized = false
    private final Object lock = new Object()

    DefaultBeanModel(Class<T> beanClass) {
        this(beanClass, GriffonClassUtils.getPropertyDescriptors(beanClass).name as String[])
    }

    DefaultBeanModel(Class<T> beanClass, String[] propertyNames) {
        super(beanClass)
        if(propertyNames == null || propertyNames.length == 0) {
            throw new IllegalArgumentException("DefaultBeanModel for class '${beanClass.name}' has no properties.")
        }

        for(String propertyName : propertyNames) {
            if(!propertyName || 'class' == propertyName || 'metaClass' == propertyName) continue
            attributes[propertyName] = ModelUtils.makeModelFor(beanClass, this, propertyName)
        }

        if(!attributes) {
            throw new IllegalArgumentException("DefaultBeanModel for class '${beanClass.name}' has no attributes!")
        }
        
        attachToAttributes()
    }

    Map<String, Model<?>> getModelAttributes() {
        Collections.<String, Model<?>>unmodifiableMap(attributes)
    }

    Model<?> getModelAttribute(String name) {
        attributes[name]
    }

    void realize() {
        synchronized(lock) {
            if(realized) return

            attributes.each { name, model ->
                metaClass."${GriffonNameUtils.getGetterName(name)}" = {it}.curry(model)
                metaClass."$name" = model
            }

            realized = true
        }
    }

    def propertyMissing(String name) {
        if(!attributes.containsKey(name)) {
            throw new MissingPropertyException(name, DefaultBeanModel)
        }

        Model<?> model = attributes[name]
        metaClass."${GriffonNameUtils.getGetterName(name)}" = {it}.curry(model)
        metaClass."$name" = model
        model
    }

    def methodMissing(String methodName, args) {
        if(methodName.startsWith('get') && methodName.length() > 3) {
            String name = GriffonNameUtils.uncapitalize(methodName[3..-1])
            Model<?> model = attributes[name]
            if(model) {
                metaClass."$methodName" = {it}.curry(model)
                metaClass."$name" = model
                return model
            }
        }
        
        throw new MissingMethodException(methodName, ArtifactManager, args)
    }
}
