/*
 * Copyright 2009-2010 the original author or authors.
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
 */

package griffon.jxlayer.factory

import groovy.swing.factory.ComponentFactory
import javax.swing.JComponent
// import org.jdesktop.jxlayer.JXLayer
// import org.jdesktop.jxlayer.plaf.LayerUI

/**
 * @author Andres Almiray
 */
class JXLayerFactory extends ComponentFactory {
    JXLayerFactory() {
        super(Object)
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if (value instanceof GString) value = value as String
        Class jxlayerClass = JXLayerUtil.findJXLayerClass()
        if (FactoryBuilderSupport.checkValueIsTypeNotString(value, name, jxlayerClass)) {
            return value
        }
        Object bean = jxlayerClass.newInstance()
        if (value instanceof String) {
            try {
                bean.text = value
            } catch (MissingPropertyException mpe) {
                throw new RuntimeException("In $name value argument of type String cannot be applied to property text:");
            }
        }
        return bean
    }

    void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
        if(child instanceof JComponent) {
            parent.view = child
        } else if(JXLayerUtil.isLayerUI(child)) {
             parent.setUI(child)
        } else {
            throw new IllegalArgumentException("You cannot nest ${child?.getClass()?.name} inside JXLayer.")
        }
    }
}
