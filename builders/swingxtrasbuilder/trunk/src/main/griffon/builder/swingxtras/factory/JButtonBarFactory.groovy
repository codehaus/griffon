/*
 * Copyright 2008 the original author or authors.
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

package griffon.builder.swingxtras.factory

import com.l2fprod.common.swing.JButtonBar
import com.l2fprod.common.swing.plaf.blue.BlueishButtonBarUI
import com.l2fprod.common.swing.plaf.misc.IconPackagerButtonBarUI
import groovy.swing.factory.ComponentFactory

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class JButtonBarFactory extends ComponentFactory {
   JButtonBarFactory() {
      super(JButtonBar,false)
   }

   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      def ui = attributes.remove("ui")
      if( ui == "blue" || ui == "mozilla" ) {
         ui = new BlueishButtonBarUI()
      } else if( ui == "icon packager" ) {
         ui = new IconPackagerButtonBarUI()
      } else {
         ui = null
      }

      if( value instanceof JButtonBar) {
         if( ui ) value.setUI(ui)
         return value
      }
      def buttonBar = new JButtonBar()
      if( ui ) buttonBar.setUI(ui)
      return buttonBar
   }
}