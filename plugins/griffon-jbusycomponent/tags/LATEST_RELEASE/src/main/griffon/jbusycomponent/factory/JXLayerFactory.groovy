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
 */

package griffon.jbusycomponent.factory

import groovy.swing.factory.ComponentFactory
import javax.swing.JComponent
import org.jdesktop.jxlayer.JXLayer
import org.jdesktop.jxlayer.plaf.LayerUI

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class JXLayerFactory extends ComponentFactory {
   JXLayerFactory() {
      super(JXLayer)
   }

   void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if(child instanceof JComponent) {
         parent.view = child
      } else if(child instanceof LayerUI) {
         parent.setUI(child)
      } else {
         throw new IllegalArgumentException("You cannot nest ${child?.getClass()?.name} inside JXLayer.")
      }
   }
}
