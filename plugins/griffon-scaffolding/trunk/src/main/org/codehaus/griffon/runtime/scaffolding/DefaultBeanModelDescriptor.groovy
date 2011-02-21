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

import java.beans.BeanDescriptor

import griffon.util.GriffonNameUtils
import griffon.plugins.scaffolding.BeanModel
import griffon.plugins.scaffolding.BeanModelDescriptor

/**
 * @author Andres Almiray
 */
class DefaultBeanModelDescriptor<B,T> extends AbstractModelDescriptor<T> implements BeanModelDescriptor<T> {
    private final String baseKey

    DefaultBeanModelDescriptor(BeanModel<B,T> model) {
        super(model)
        baseKey = model.beanClass.name
    }

    BeanDescriptor getBeanDescriptor() {
        model.beanDescriptor
    }

    protected String getBaseKey() {
        baseKey
    }

    protected String i18nFromIntrospection(String propName) {
        GriffonNameUtils.capitalize(beanDescriptor[propName])
    }
}
