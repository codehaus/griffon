/*
 * Copyright 2009 the original author or authors.
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

package griffon.builder.wizard

import java.awt.Component
import org.netbeans.spi.wizard.WizardController
import org.netbeans.spi.wizard.WizardPage

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class GriffonWizardPage extends WizardPage {
   def builder
   private boolean initialized

   GriffonWizardPage( String stepId, String stepDescription, boolean autoListen ) {
      super(stepId, stepDescription, autoListen)
   }

   public synchronized void init() {
      if( !initialized ) {
         builder.container(this,pageContents)
         initialized = true
      }
   }

   protected String validateContents( Component component, Object event) {
      try {
         if(component && validator) return validator(component,event)
      } catch( MissingPropertyException mpe ) {
         // ignore
      }
      return super.validateContents(component,event)
   }
}