/*
 * Copyright 2009-2010 the original author or authors.
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

package griffon.builder.wizard.impl

import java.beans.PropertyChangeListener
import javax.swing.JComponent
import javax.swing.JPanel
import org.netbeans.spi.wizard.WizardController
import org.netbeans.spi.wizard.WizardException
import org.netbeans.spi.wizard.WizardPanelProvider

/**
 * @author Andres Almiray
 */
class GriffonWizardPanelProvider extends WizardPanelProvider {
   private final FactoryBuilderSupport builder
   private final Map panels = [:]
   private final panelDelegate
   WizardResultProducerImpl resultProducer

   GriffonWizardPanelProvider( panelDelegate, FactoryBuilderSupport builder ) {
      super(
         fetchTitle(panelDelegate),
         fetchSteps(panelDelegate),
         fetchDescriptions(panelDelegate)
      )
      if( panelDelegate.steps.size() != panelDelegate.descriptions.size() ) {
         throw new RuntimeException("In ${panelDelegate.class} properties 'steps' and 'descriptions' should have the same size.")
      }
      panelDelegate.steps.each {
         // let it fail with MPE eraly
         panelDelegate."$it"
      }
      this.builder = builder
      this.panelDelegate = panelDelegate
      this.panelDelegate.metaClass.propertyMissing = { String name ->
         if( builder ) return builder[name]
         throw new MissingPropertyException(name, panelDelegate.class)
      }
   }

   protected JComponent createPanel( final WizardController controller, String id, final Map settings ) {
      def panel = panels[id]
      if( !panel ) {
         def buildClosure = panelDelegate."$id"
         buildClosure.delegate = builder
         panel = builder.panel(){ buildClosure(settings, controller) }
         panels[id] = panel
      }
      return panel
   }

   protected Object finish(Map settings) throws WizardException {
      if( resultProducer ) return resultProducer.finish(settings)
      def mp = panelDelegate.metaClass.getMetaProperty("onFinish")
      if( mp ) return mp.getProperty(panelDelegate).call(settings)
      return settings
   }

   public boolean cancel(Map settings) {
      if( resultProducer ) return resultProducer.cancel(settings)
      def mp = panelDelegate.metaClass.getMetaProperty("onCancel")
      if( mp ) return mp.getProperty(panelDelegate).call(settings)
      return true
   }

   private static String fetchTitle( source ) {
      try {
         return source.title
      } catch( MissingPropertyException mpe ) {
         throw new RuntimeException("No 'title' property has been defined for ${source.class}")
      }
   }

   private static String[] fetchSteps( source ) {
      try {
         return source.steps as String[]
      } catch( MissingPropertyException mpe ) {
         throw new RuntimeException("No 'steps' property has been defined for ${source.class}")
      }
   }

   private static String[] fetchDescriptions( source ) {
      try {
         return source.descriptions as String[]
      } catch( MissingPropertyException mpe ) {
         throw new RuntimeException("No 'descriptions' property has been defined for ${source.class}")
      }
   }
}
