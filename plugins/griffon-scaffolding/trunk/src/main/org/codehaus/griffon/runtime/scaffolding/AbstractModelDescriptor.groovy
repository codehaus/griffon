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

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener
import java.beans.PropertyDescriptor
import groovy.beans.Bindable

import griffon.util.ApplicationHolder
import griffon.util.GriffonNameUtils

import griffon.plugins.scaffolding.Model
import griffon.plugins.scaffolding.ModelDescriptor

/**
 * @author Andres Almiray
 */
abstract class AbstractModelDescriptor<T> implements ModelDescriptor<T> {
    final Model<T> model
    @Bindable String name
    @Bindable String displayName
    @Bindable String description

    AbstractModelDescriptor(Model<T> model) {
        this.model = model

        ApplicationHolder.application?.locale?.addPropertyChangeListener(i18nUpdater)
        resolveI18n()
    }

    String toString() {
        String desc = [
            'name=', name,
            ', displayName=', displayName,
            ', description=', description
         ].join('')
        '[' + desc + ']'
    }

    private final PropertyChangeListener i18nUpdater = new PropertyChangeListener() {
        void propertyChange(PropertyChangeEvent evt) {
            resolveI18n()
        }
    }

    protected abstract String getBaseKey()

    protected void resolveI18n() {
        resolveI18nFor('name')
        resolveI18nFor('displayName')
        resolveI18nFor('description', 'shortDescription')
    }

    protected void resolveI18nFor(String propName) {
        resolveI18nFor(propName, propName)
    }

    protected void resolveI18nFor(String propName, String alternatePropName) {
        String i18nKey = baseKey + '.' + propName
        String setterName = GriffonNameUtils.getSetterName(propName)

        String value = i18nFromApplication(i18nKey)
        if(!value) value = i18nFromIntrospection(alternatePropName)
        this."$setterName"(value)
    }

    private String i18nFromApplication(String i18nKey) {
        if(ApplicationHolder.application) {
            try {
                String val = ApplicationHolder.application.getMessage(i18nKey)
                if(val && val != i18nKey) {
                    return val
                }
            } catch(MissingMethodException mme) {
                // i18n addon is not installed
                // continue with the next strategy
            }
        }
        return null
    }

    protected abstract String i18nFromIntrospection(String propName)
}
