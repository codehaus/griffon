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

import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JPanel
import org.netbeans.spi.wizard.WizardController
import org.netbeans.spi.wizard.WizardException
import org.netbeans.spi.wizard.WizardPanelProvider

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class GriffonWizardPanelProvider extends WizardPanelProvider {
   def builder
   private Map panels = [:]

   GriffonWizardPanelProvider( String title, String[] steps, String[] descriptions ) {
      super(title, steps, descriptions)
   }

   protected JComponent createPanel( final WizardController controller, String id, final Map settings ) {
      def panel = panels[id]
      if( !panel ) {
         def buildClosure = this."$id"
         buildClosure.delegate = builder
         panel = builder.panel(){ buildClosure(settings, controller) }
         panels[id] = panel
      }
      return panel
   }

   def getProperty( String name ) {
      def mp = getClass().metaClass.getMetaProperty(name)
      if( mp ) return mp.getProperty(this)
      return builder[name]
   }

   protected Object finish(Map settings) throws WizardException {
      def mp = getClass().metaClass.getMetaProperty("onFinish")
      if( mp ) return mp.getProperty(this).call(settings)
      return settings
   }

   public boolean cancel(Map settings) {
      def mp = getClass().metaClass.getMetaProperty("onCancel")
      if( mp ) return mp.getProperty(this).call(settings)
      return true
   }
}