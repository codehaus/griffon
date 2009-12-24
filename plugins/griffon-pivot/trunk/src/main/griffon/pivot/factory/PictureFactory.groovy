/*
 * Copyright 2009 the original author or authors.
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

package griffon.pivot.factory

import griffon.pivot.PivotUtils
import java.awt.image.BufferedImage
import org.apache.pivot.wtk.media.Picture

/**
 * @author Andres Almiray
 */
class PictureFactory extends PivotBeanFactory {
    PictureFactory() {
        super(Picture)
    }

    Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
        if(value instanceof Picture) return value
        if(value instanceof BufferedImage) return new Picture(value)
        if(!value && attributes.containsKey('image')) return new Picture(attributes.remove('image'))
        throw new IllegalArgumentException("In $name you must define a value or a property image: of type ${BufferedImage.class.name}")
    }

    boolean onHandleNodeAttributes(FactoryBuilderSupport builder, Object node, Map attributes) {
        attributes.each { property, value ->
            if(!PivotUtils.applyAsEventListener(builder, property, value, node)) {
                PivotUtils.setBeanProperty(property, value, node)
            }
        }
       
        return false
    }
}
