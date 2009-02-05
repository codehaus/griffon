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

package griffon.builder.wizard.impl

import org.netbeans.spi.wizard.Wizard
import org.netbeans.spi.wizard.WizardController
import org.netbeans.spi.wizard.WizardBranchController
import org.netbeans.spi.wizard.WizardPanelProvider
import org.netbeans.spi.wizard.WizardPage

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class GriffonBranchingWizard extends WizardBranchController {
   private final FactoryBuilderSupport builder
   private final pages
   private final branchDelegate

   GriffonBranchingWizard( GriffonWizardPage[] pages, branchDelegate, FactoryBuilderSupport builder ) {
      super(pages)
      this.pages = pages
      this.branchDelegate = branchDelegate
      this.builder = builder
   }

   GriffonBranchingWizard( GriffonWizardPanelProvider panelProvider, branchDelegate, FactoryBuilderSupport builder ) {
      super(panelProvider)
      this.branchDelegate = branchDelegate
      this.builder = builder
   }

   public synchronized void init() {
      if( pages ) {
         pages.each{ it.init() }
      }
   }

   protected Wizard getWizardForStep( String stepId, Map settings ) {
      def w = branchDelegate.wizardForStep(stepId,settings)
      try { w?.initPages() }
      catch( MissingMethodException mme ) { /*ignore*/ }
      return w
   }
}