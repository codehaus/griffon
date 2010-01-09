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

package griffon.builder.css

import java.awt.GraphicsEnvironment
import java.awt.GraphicsDevice
import java.awt.GraphicsConfiguration
import java.awt.Dimension
import java.beans.PropertyChangeSupport
import java.beans.PropertyChangeListener
import java.beans.PropertyChangeEvent

/**
 * @author Andres Almiray
 */
// @Singleton
// @Bindable
class CSSBindings extends Binding {
    private static final instance
    static {
        instance = new CSSBindings()
    }

    private PropertyChangeSupport pcs

    private CSSBindings() {
        pcs = new PropertyChangeSupport(this)
    }

    static final CSSBindings getInstance() {
        return instance
    }

    void setVariable(String name, Object value) {
        def oldValue = variables[name]
        super.setVariable(name, value)
        if(oldValue != value) pcs.firePropertyChange(name, oldValue, value)
    }

    void initDefaults() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment()
        GraphicsDevice gd = ge.getDefaultScreenDevice()
        GraphicsConfiguration gc = gd.getDefaultConfiguration()
        setVariable("screen", gc.getBounds())
    }

    void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener)
    }

    void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener)
    }

    void removePropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener)
    }

    void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener)
    }

    PropertyChangeListener[] getPropertyChangeListeners() {
        return pcs.getPropertyChangeListeners()
    }

    PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        return pcs.getPropertyChangeListeners(propertyName)
    }
}
