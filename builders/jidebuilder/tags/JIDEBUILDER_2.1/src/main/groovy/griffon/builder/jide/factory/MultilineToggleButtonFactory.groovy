/*
 * Copyright 2007-2008 the original author or authors.
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

package griffon.builder.jide.factory

import com.jidesoft.swing.MultilineToggleButton

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class MultilineToggleButtonFactory extends AbstractJideComponentFactory {
   public MultilineToggleButtonFactory() {
      super( MultilineToggleButton )
   }

   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map properties) throws InstantiationException, IllegalAccessException {
      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, MultilineToggleButton )) {
         return value
      }

      def type = properties.remove("type") ?: -1
      def text = properties.remove("text") ?: (value instanceof String?value:"")
      if( type instanceof String ) {
         switch(type) {
            case ~/(?i:checkbox)/: type = MultilineToggleButton.CHECKBOX_TYPE; break;
            case ~/(?i:radioButton)/:
            case ~/(?i:radio)/: type = MultilineToggleButton.RADIOBUTTON_TYPE; break;
            default: type = -1
         }
      } else if( !(type instanceof Number) ) {
         type = -1
      }

      return new MultilineToggleButton(type,text)
   }
}