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

import java.awt.Dialog
import java.awt.Frame
import groovy.swing.factory.DialogFactory
import com.jidesoft.dialog.MultiplePageDialog
import com.jidesoft.dialog.PageList

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class MultiplePageDialogFactory extends DialogFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map properties) throws InstantiationException, IllegalAccessException {
      MultiplePageDialog dialog
      if (FactoryBuilderSupport.checkValueIsType(value, name, MultiplePageDialog)) {
          dialog = value
      } else {
          Object owner = properties.remove("owner")
          LinkedList containingWindows = builder.containingWindows
          // if owner not explicit, use the last window type in the list
          if ((owner == null) && !containingWindows.isEmpty()) {
              owner = containingWindows.getLast()
          }
          if (owner instanceof Frame || owner instanceof Dialog) {
              dialog = new MultiplePageDialog(owner)
          } else {
              dialog = new MultiplePageDialog()
          }
      }

      handleRootPaneTasks(builder, dialog, properties)

      return dialog
   }
}