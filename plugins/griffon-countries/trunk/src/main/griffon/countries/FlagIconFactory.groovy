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

package griffon.countries

import groovy.swing.factory.ImageIconFactory

/**
 * @author Andres.Almiray
 */
class FlagIconFactory extends ImageIconFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        String code = attributes.remove('code') ?: value 
  
        if(!code) throw new IllegalArgumentException("In $name you must define a node value or code:")

        if(code.endsWith('.png')) code -= '.png'
        code = code.toLowerCase()
        Country.resolveByCode(code)

        value = "/com/famfamfam/flags/icons/${code}.png"
        super.newInstance(builder, name, value, [:])
    }
}
