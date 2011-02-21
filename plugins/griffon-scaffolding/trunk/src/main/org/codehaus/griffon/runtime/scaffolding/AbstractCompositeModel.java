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

package org.codehaus.griffon.runtime.scaffolding;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import griffon.plugins.scaffolding.Model;
import griffon.plugins.scaffolding.ModelDescriptor;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

import org.codehaus.groovy.runtime.typehandling.DefaultTypeTransformation;

/**
 * @author Andres Almiray
 */
public abstract class AbstractCompositeModel<T> implements Model<T> {
    private T value;
    private boolean initialized;
    
    private final Object lock = new Object();
    private final List<Model<T>> models = new ArrayList<Model<T>>();
    private final List<PropertyChangeListener> valueChangeListeners = new ArrayList<PropertyChangeListener>();
    private final ModelDescriptor<T> modelDescriptor;

    public AbstractCompositeModel(List<Model<T>> models) {
        if(models == null || models.isEmpty()) {
            throw new IllegalArgumentException(getClass().getName() + " cannot be empty!");
        }

        this.models.addAll(models);
        for(Model<T> model : models) model.addValueChangeListener(valueChangedUpdater);

        modelDescriptor = new ModelDescriptor<T>() {
            public Model<T> getModel() { return AbstractCompositeModel.this; }
            public String getName() { return ""; }
            public String getDisplayName() { return ""; }
            public String getDescription() { return ""; }
        };
    }

    public List<Model<T>> getModels() {
        return Collections.unmodifiableList(models);
    }

    public T getValue() {
        synchronized(lock) {
            if(!initialized) {
                value = calculateValue();
                initialized = true;
            }
        }
        return value;
    }

    public void setValue(T value) {
        throw new UnsupportedOperationException("Instances of "+ getClass().getName() + " are read-only!");
    }

    public void addValueChangeListener(PropertyChangeListener listener) {
        if(listener == null || valueChangeListeners.contains(listener)) return;
        valueChangeListeners.add(listener);
    }

    public void removeValueChangeListener(PropertyChangeListener listener) {
        if(listener == null) return;
        valueChangeListeners.remove(listener);
    }

    public PropertyChangeListener[] getValueChangeListeners() {
        return valueChangeListeners.toArray(new PropertyChangeListener[valueChangeListeners.size()]);
    }

    public ModelDescriptor<T> getModelDescriptor() {
        return modelDescriptor;
    }

    private final PropertyChangeListener valueChangedUpdater = new PropertyChangeListener() {
        public void propertyChange(PropertyChangeEvent evt) {
            fireValueChange(value, value = calculateValue());
        }
    };

    protected void fireValueChange(T oldValue, T newValue) {
        if(oldValue == newValue || (oldValue != null && oldValue.equals(newValue))) return;
        PropertyChangeEvent event = new PropertyChangeEvent(this, "value", oldValue, newValue);
        for(PropertyChangeListener listener : valueChangeListeners) {
            listener.propertyChange(event);
        }
    }

    public final boolean isSameValue(T value) {
        return DefaultTypeTransformation.compareEqual(getValue(), value);
    }

    protected abstract T calculateValue();
}
