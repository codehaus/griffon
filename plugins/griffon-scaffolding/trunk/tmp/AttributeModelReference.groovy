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

package griffon.plugins.scaffolding

import java.beans.PropertyChangeEvent
import java.beans.PropertyChangeListener

import griffon.util.ApplicationHolder
import griffon.util.GriffonClassUtils
import griffon.util.GriffonNameUtils

/**
 * @author Andres Almiray
 */
class AttributeModelReference<B,T> extends AbstractAttributeModel<B,T> implements ModelReference<T> {
    private ValueObject<B> bean
    private AttributeModel<B,T> attributeDelegate
    private boolean resolved

    AttributeModelReference(Class<B> beanClass, String propertyName) {
        super(beanClass, propertyName)

        setupListeners()
    }

    ValueObject<B> getBean() {
        !isReferenceResolved() ? null : bean
    }

    void setBean(ValueObject<B> bean) {
        if(isReferenceResolved()) {
            throw new IllegalStateException("Cannot change bean value as reference has been resolved already! [${beanClass.name}.${propertyName}]")
        }
        this.bean = bean
    }

    synchronized boolean isReferenceResolved() {
        resolved
    }

    void resolve() {
        synchronized(this) {
            if(resolved) {
                throw new IllegalStateException("Reference has been resolved already! [${beanClass.name}.${propertyName}]")
            }
            if(!bean) {
                throw new IllegalStateException("No bean value has been set, cannot resolve! [${beanClass.name}.${propertyName}]")
            }

            attributeDelegate = ModelUtils.makeModelFor(beanClass, bean, propertyName)
            attributeDelegate.addPropertyChangeListener(new PropertyChangeListener() {
                void propertyChange(PropertyChangeEvent evt) {
                    if(evt.propertyName == ValueObject.VALUE_PROPERTY) return
                    String setterName = GriffonNameUtils.getSetterName(evt.propertyName)
                    try {
                        this."$setterName"(evt.newValue) 
                    } catch(Exception x) {
                        // ignore
                    }
                }
            })
            ApplicationHolder.application?.locale?.removePropertyChangeListener(i18nUpdater)

            resolved = true
        }
    }

    protected T getValueInternal() {
        !isReferenceResolved() ? null : attributeDelegate.getValue()
    }

    protected void setValueInternal(T val) {
        if(isReferenceResolved()) attributeDelegate.setValue(val)
    }

    protected void setupListeners() {
        ApplicationHolder.application?.locale?.addPropertyChangeListener(i18nUpdater)
    }

    protected T readPropertyFromBean() {
        !isReferenceResolved() ? null : attributeDelegate.readPropertyFromBean()
    }

    protected void writePropertyToBean(T val) {
        if(isReferenceResolved()) attributeDelegate.writePropertyToBean(val)
    }

    String toString() {
        String desc = [
            'name=', modelName,
            ', description=', modelDescription,
            ', beanClass=', beanClass,
            ', resolved=', isReferenceResolved()
         ].join('')
        '[' + desc + ']'
    }
}
