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

package griffon.tango

import groovy.swing.factory.ImageIconFactory

/**
 * @author Andres.Almiray
 */
class TangoIconFactory extends ImageIconFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        String size = attributes.remove('size') ?: '16'
        String category = attributes.remove('category') ?: 'actions'
        String icon = attributes.remove('icon') ?: value 
  
        if(!icon) throw new IllegalArgumentException("In $name you must define a node value or icon:")

        size = parseSize(size)
        if(icon.endsWith('.png')) icon -= '.png'

        value = "/org/freedesktop/tango/${size}/${category}/${icon}.png"
        super.newInstance(builder, name, value, [:])
    }

    private String parseSize(String size) {
        switch(size) {
            case '16':
            case '22':
            case '32':
                return size +"x"+ size
        }
        return '16x16'
    }
}
