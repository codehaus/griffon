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
import java.beans.PropertyEditor
import java.beans.PropertyEditorManager

import griffon.util.GriffonClassUtils
import griffon.plugins.scaffolding.AttributeModel
import griffon.plugins.scaffolding.ModelDescriptor
import griffon.plugins.scaffolding.ValueObject

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andres Almiray
 */
abstract class AbstractAttributeModel<B,T> extends ValueObject<T> implements AttributeModel<B,T> {
    private static final Logger LOG = LoggerFactory.getLogger(AbstractAttributeModel)

    final Class<B> beanClass
    final String propertyName
    final PropertyDescriptor propertyDescriptor
    final ModelDescriptor modelDescriptor

    AbstractAttributeModel(Class<B> beanClass, String propertyName) {
        this.beanClass = beanClass
        this.propertyName = propertyName

        propertyDescriptor = GriffonClassUtils.getPropertyDescriptor(beanClass, propertyName)
        modelDescriptor = new DefaultAttributeModelDescriptor(this)
        addValueChangeListener(valueChangedUpdater)
    }

    private final PropertyChangeListener valueChangedUpdater = new PropertyChangeListener() {
        void propertyChange(PropertyChangeEvent evt) {
            setValueOnProperty(evt.newValue)
        }
    }

    final void setValueFromProperty(boolean propagate = true) {
        T obj = readPropertyFromBean()
        if(LOG.isTraceEnabled()) LOG.trace("Setting value '$obj' on ${this} after reading property $propertyName [propagate=$propagate]")
        propagate? setValue(obj) : setValueInternal(obj)
    }

    protected void setValueInternal(T value) {
        super.setValueInternal(transformValue(value))
    }

    protected T getValueInternal() {
        transformValue(super.getValueInternal())
    }

    final boolean isSameValue(T val) {
        getValue() == transformValue(val)
    }

    final void setValueOnProperty(T value) {
        writePropertyToBean(transformValue(value))
    }

    protected T transformValue(T val) {
        PropertyEditor editor = val != null ? PropertyEditorManager.findEditor(val.getClass()) : null
        if(editor) {
            editor.value = val
            val = editor.value
        }
        val        
    }

    protected abstract T readPropertyFromBean()

    protected abstract void writePropertyToBean(T val)

    String toString() {
        String desc = [
            'description=', modelDescriptor,
            ', value=', getValue()
         ].join('')
        '[' + desc + ']'
    }
}
