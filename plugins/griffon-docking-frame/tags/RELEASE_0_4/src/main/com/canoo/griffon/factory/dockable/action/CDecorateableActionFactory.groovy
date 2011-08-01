/*
 * Copyright 2009-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.canoo.griffon.factory.dockable.action

import groovy.swing.factory.BeanFactory
import javax.swing.KeyStroke
import bibliothek.gui.dock.common.intern.DefaultCDockable

/**
 * @author Alexander Klein
 */
abstract class CDecorateableActionFactory extends BeanFactory {
    CDecorateableActionFactory(Class beanClass, boolean leaf = true) {
        super(beanClass, leaf)
    }

    abstract def createInstance(Map attributes)

    @Override
    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        if ((value == null) || !FactoryBuilderSupport.checkValueIsType(value, name, beanClass))
            value = createInstance(attributes)

        Object accel = attributes.remove("accelerator")
        if (accel != null) {
            KeyStroke stroke = null
            if (accel instanceof KeyStroke) {
                stroke = (KeyStroke) accel
            } else {
                stroke = KeyStroke.getKeyStroke(accel.toString())
            }
            value.accelerator = stroke
        }
        return super.newInstance(builder, name, value, attributes)
    }

    @Override
    void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
        if (parent instanceof DefaultCDockable)
            parent.addAction(child)
    }
}
