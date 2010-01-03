/*
 * Copyright 2009-2010 the original author or authors.
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
import org.divxdede.swing.busy.*
import javax.swing.JComponent

/**
 * @author Andres Almiray
 */
class JBusyComponentFactory extends ComponentFactory {
   JBusyComponentFactory() {
      super(JBusyComponent)
   }

   void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if(child instanceof JComponent) {
         parent.view = child
      } else if(child instanceof BusyModel) {
         parent.busyModel = child
      } else {
         throw new IllegalArgumentException("You cannot nest ${child?.getClass()?.name} inside JBusyComponent.")
      }
   }
}
