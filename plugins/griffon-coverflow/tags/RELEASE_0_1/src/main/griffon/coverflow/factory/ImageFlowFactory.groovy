/*
 * Copyright 2009 the original author or authors.
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

package griffon.coverflow.factory

import groovy.swing.factory.ComponentFactory
import griffon.coverflow.ui.*
import javax.swing.JComponent
import javax.swing.ListModel

/**
 * @author Andres.Almiray
 */
class ImageFlowFactory extends ComponentFactory {
    ImageFlowFactory() {
        super(ImageFlow)
    }

    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        FactoryBuilderSupport.checkValueIsType(value, name, ImageFlow)
        Object items = attributes.get("items")
        if (items instanceof List ||
            items instanceof ImageFlow[] ||
            items instanceof ListModel) {
            return new ImageFlow(attributes.remove("items"))
        } else if (value instanceof ImageFlow) {
            return value
        } else {
            return new ImageFlow()
        }
    }

	void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
		if(child instanceof ImageFlowItem) {
			// will bomb if model is immutable
			parent.model << child
		} else {
			super.setChild(builder, parent, child)
		}
	}
}

/**
 * @author Andres.Almiray
 */
class ImageFlowItemFactory extends AbstractFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(value instanceof ImageFlowItem) return value
        String label = attributes.remove("label") ?: ""
        if(!value) {
            if(attributes.containsKey("file")) {
                def file = attributes.remove("file")
                if(file instanceof File) {
                    value = file
                } else {
                    value = file.toString()
                }
            } else if(attributes.containsKey("url")) {
                def url = attributes.remove("url")
                if(url instanceof URL) {
                    value = url
                } else {
                    value = url.toString().toURL()
                }
            } else if(attributes.containsKey("inputStream")) {
                value = attributes.remove("inputStream")
            } else if(attributes.containsKey("image")) {
                value = attributes.remove("image")
            } else if(attributes.containsKey("resource")) {
                def resource = attributes.remove("resource").toString()
                value = new ImageFlowItem.Resource(resource, attributes.remove("class") ?: ImageFlowItem)
            }
        }
        if(!value) {
            throw new IllegalArgumentException("In $name you must define one of file:, url:, inputStream:, image: or resource:")
        }
        return new ImageFlowItem(value, label)
    }
}