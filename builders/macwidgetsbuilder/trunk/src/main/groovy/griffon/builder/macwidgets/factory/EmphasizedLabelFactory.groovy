/*
 * Copyright 2008-2010 the original author or authors.
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

package griffon.builder.macwidgets.factory

import java.awt.Color
import javax.swing.JLabel
import com.explodingpixels.macwidgets.MacWidgetFactory

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class EmphasizedLabelFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( value == null ) {
         if( attributes.containsKey("text") ) {
            value = new JLabel(attributes.remove("text"))
         }
      } else if( value instanceof String) {
         value = new JLabel(value)
      } else if( !(value instanceof JLabel) ) {
         throw new RuntimeException("in $name the value must be either a java.lang.String or a javax.swing.JLabel")
      }

      def focusedColor = attributes.remove("focusedColor")
      def unfocusedColor = attributes.remove("unfocusedColor")
      def emphasisColor = attributes.remove("emphasisColor")

      return focusedColor && unfocusedColor && emphasisColor ? MacWidgetFactory.makeEmphasizedLabel(value,focusedColor,unfocusedColor,emphasisColor) : MacWidgetFactory.makeEmphasizedLabel(value)
   }
}