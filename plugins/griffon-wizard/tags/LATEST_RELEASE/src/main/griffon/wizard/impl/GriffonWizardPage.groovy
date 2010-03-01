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

package griffon.wizard.impl

import java.awt.Component
import org.netbeans.spi.wizard.Wizard
import org.netbeans.spi.wizard.WizardPage
import org.netbeans.spi.wizard.WizardController
import org.netbeans.spi.wizard.WizardPanelNavResult

/**
 * @author Andres Almiray
 */
class GriffonWizardPage extends WizardPage {
   private final FactoryBuilderSupport builder
   private boolean initialized
   private final pageDelegate

   GriffonWizardPage(pageDelegate, FactoryBuilderSupport builder) {
      super(
         fetchStepId(pageDelegate),
         fetchDescription(pageDelegate),
         fetchAutoListen(pageDelegate)
      )
      this.builder = builder
      this.pageDelegate = pageDelegate
      this.pageDelegate.metaClass.propertyMissing = { String name
         if(builder) return builder[name]
         throw new MissingPropertyException(name, pageDelegate.class)
      }
   }

   public synchronized void init() {
      if(!initialized) {
         builder.container(this, pageDelegate.pageContents)
         initialized = true
      }
   }

   protected String validateContents(Component component, Object event) {
      def mp = pageDelegate.metaClass.getMetaProperty('onValidate')
      if(component && mp) return pageDelegate.onValidate(component,event)
      return super.validateContents(component,event)
   }

   WizardPanelNavResult allowBack(String stepName, Map settings, Wizard wizard) {
      if(hasMetaProperty('allowBack')) return handleWizardPanelNavResult(pageDelegate.allowBack(stepName, settings, wizard))
      return super.allowBack(stepName, settings, wizard)
   }

   WizardPanelNavResult allowFinish(String stepName, Map settings, Wizard wizard) {
      if(hasMetaProperty('allowFinish')) return handleWizardPanelNavResult(pageDelegate.allowFinish(stepName, settings, wizard))
      return super.allowFinish(stepName, settings, wizard)
   }

   WizardPanelNavResult allowNext(String stepName, Map settings, Wizard wizard) {
      if(hasMetaProperty('allowNext')) return handleWizardPanelNavResult(pageDelegate.allowNext(stepName, settings, wizard))
      return super.allowNext(stepName, settings, wizard)
   }

   private boolean hasMetaProperty(String propertyName) {
      return pageDelegate.metaClass.getMetaProperty(propertyName) != null
   }

   private WizardPanelNavResult handleWizardPanelNavResult(object) {
      if(object instanceof WizardPanelNavResult) return object
      if('proceed'.equalsIgnoreCase(object?.toString())) return WizardPanelNavResult.PROCEED
      if('remain_on_page'.equalsIgnoreCase(object?.toString())) return WizardPanelNavResult.REMAIN_ON_PAGE
      if('remain on page'.equalsIgnoreCase(object?.toString())) return WizardPanelNavResult.REMAIN_ON_PAGE
      return WizardPanelNavResult.PROCEED
   }

   private static String fetchStepId(source) {
      try {
         return source.stepId
      } catch( MissingPropertyException mpe ) {
         throw new RuntimeException("No 'stepId' property has been defined for ${source.class}")
      }
   }

   private static String fetchDescription(source) {
      try {
         return source.description
      } catch( MissingPropertyException mpe ) {
         throw new RuntimeException("No 'description' property has been defined for ${source.class}")
      }
   }

   private static boolean fetchAutoListen(source) {
      try {
         return source.autoListen
      } catch( MissingPropertyException mpe ) {
         return true
      }
   }
}
