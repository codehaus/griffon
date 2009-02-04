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

import java.awt.Rectangle
import javax.swing.Action
import groovy.swing.SwingBuilder
import griffon.builder.wizard.factory.*
import org.netbeans.api.wizard.WizardDisplayer
import org.netbeans.spi.wizard.Wizard

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class WizardBuilder extends SwingBuilder {
   public WizardBuilder( boolean init = true ) {
      super( init )
   }

   public void registerWizard() {
      registerFactory("wizard", new WizardFactory())
      registerExplicitMethod("showWizard", this.&showWizard)
   }

   public def showWizard( Wizard wizard, Map params ) {
      return showWizard( wizard, params.helpAction, params.initialProperties, params.location )
   }

   public def showWizard( Wizard wizard, Action helpAction = null, Map initialProperties = null, def location = null ) {
       if( location instanceof List ) {
          if( location.size() != 4 ||
              location.any { !(it instanceof Number) } ) {
             throw new RuntimeException("location must be a List with 4 numbers as elements")
          }
          location = new Rectangle(
             location[0] as int,
             location[1] as int,
             location[2] as int,
             location[3] as int
          )
       }
       wizard.initPages()
       return WizardDisplayer.showWizard(wizard, location, helpAction, initialProperties)
   }
}