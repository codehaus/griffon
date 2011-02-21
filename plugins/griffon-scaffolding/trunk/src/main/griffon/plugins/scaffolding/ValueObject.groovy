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

import java.beans.PropertyChangeListener
import groovy.beans.Bindable
import groovy.beans.Vetoable

/**
 * @author Andres Almiray
 */
@Vetoable
class ValueObject<T> {
    static final String VALUE_PROPERTY = 'value'

    @Bindable T value

    void setValue(T val) {
        setValueInternal(val)
    }

    T getValue() {
        getValueInternal()
    }
   
    protected void setValueInternal(T value) {
        this.@value = value
    }

    protected T getValueInternal() {
        this.@value
    }

    final void addValueChangeListener(PropertyChangeListener listener) {
        addPropertyChangeListener(VALUE_PROPERTY, listener)
    }

    final void removeValueChangeListener(PropertyChangeListener listener) {
        removePropertyChangeListener(VALUE_PROPERTY, listener)
    }

    final PropertyChangeListener[] getValueChangeListeners() {
        getPropertyChangeListeners(VALUE_PROPERTY)
    }

    String toString() {
        '[value=' + value + ']'
    }
}
