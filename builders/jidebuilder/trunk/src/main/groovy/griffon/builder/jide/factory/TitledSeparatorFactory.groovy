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

import javax.swing.JComponent
import javax.swing.SwingConstants
import com.jidesoft.swing.TitledSeparator

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class TitledSeparatorFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) throws InstantiationException, IllegalAccessException {
      String text = attributes.remove("text")
      JComponent labelComponent = attributes.remove("labelComponent")

      if( value == null ) {
         if( text != null ) value = text
         if( labelComponent ) value = labelComponent
      } else if( !(value instanceof String) && !(value instanceof JComponent) ) {
         throw new RuntimeException("In $name the value argument '$value' does not refer to a String or a JComponent")
      }

      def type = attributes.remove("type")
      def textAlignment = attributes.remove("textAlignment")

      if( type instanceof String ) {
         switch(type) {
            case ~/(?i:line)/:
            case ~/(?i:partial[_| ]line)/:
               type = TitledSeparator.TYPE_PARTIAL_LINE
               break
            case ~/(?i:gradient)/:
            case ~/(?i:partial[_| ]gradient[_| ]line)/:
               type = TitledSeparator.TYPE_PARTIAL_GRADIENT_LINE
               break
//             case ~/(?i:etched)/:
//             case ~/(?i:partial[_| ]etched)/:
            default:
               type = TitledSeparator.TYPE_PARTIAL_ETCHED
         }
      }
      if( textAlignment instanceof String ) {
         switch(textAlignment) {
            case ~/(?i:leading)/:
               textAlignment = SwingConstants.LEADING
               break
            case ~/(?i:trailing)/:
               textAlignment = SwingConstants.TRAILING
               break
            case ~/(?i:right)/:
               textAlignment = SwingConstants.RIGHT
               break
            case ~/(?i:left)/:
            default:
               textAlignment = SwingConstants.LEFT
         }
      }

      if( type == null ) type = TYPE_PARTIAL_ETCHED
      if( textAlignment == null ) textAlignment = SwingConstants.LEFT

      return new TitledSeparator(value, type, textAlignment)
   }
}