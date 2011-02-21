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

import java.beans.Introspector
import java.beans.BeanDescriptor
import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

import griffon.util.GriffonClassUtils
import griffon.plugins.scaffolding.Model
import griffon.plugins.scaffolding.BeanModel
import griffon.plugins.scaffolding.ModelDescriptor
import griffon.plugins.scaffolding.ValueObject
import griffon.plugins.scaffolding.AttributeChangeEvent

/**
 * @author Andres Almiray
 */
abstract class AbstractBeanModel<T> extends ValueObject<T> implements BeanModel<T> {
    final Class<T> beanClass
    final BeanDescriptor beanDescriptor
    final ModelDescriptor modelDescriptor
    protected final List<PropertyChangeListener> attributesChangeListeners = new ArrayList<PropertyChangeListener>()

    AbstractBeanModel(Class<T> beanClass) {
        this.beanClass = beanClass

        beanDescriptor = Introspector.getBeanInfo(beanClass).beanDescriptor
        modelDescriptor = new DefaultBeanModelDescriptor(this)
    }

    final boolean isSameValue(T val) {
        value == val
    }

    void addAttributesChangeListener(PropertyChangeListener listener) {
        if(listener == null || attributesChangeListeners.contains(listener)) return;
        attributesChangeListeners.add(listener);
    }

    void removeAttributesChangeListener(PropertyChangeListener listener) {
        if(listener == null) return;
        attributesChangeListeners.remove(listener);
    }

    PropertyChangeListener[] getAttributesChangeListeners() {
        return attributesChangeListeners.toArray(new PropertyChangeListener[attributesChangeListeners.size()]);
    }

    protected void attachToAttributes() {
        for(Model<?> model : getModelAttributes().values()) {
            model.addValueChangeListener(attributesChangeForwarder)
        }
    }

    private final PropertyChangeListener attributesChangeForwarder = new PropertyChangeListener() {
        void propertyChange(PropertyChangeEvent e) {
            String attributeName = null
            for(Map.Entry attributeEntry : AbstractBeanModel.this.getModelAttributes()) {
                if(attributeEntry.value == e.source) {
                    attributeName = attributeEntry.key
                    break
                }
            }
            AttributeChangeEvent event = new AttributeChangeEvent(AbstractBeanModel.this, e.source, attributeName, e.oldValue, e.newValue)
            for(PropertyChangeListener listener : attributesChangeListeners) {
                listener.propertyChange(event)
            }
        }
    }

    String toString() {
        String desc = [
            'description=', modelDescriptor,
            ', value=', value
         ].join('')
        '[' + desc + ']'
    }
}
