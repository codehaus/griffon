@artifact.package@

import org.netbeans.spi.wizard .WizardPage

class @artifact.name@ implements WizardPage.WizardResultProducer {
   Object finish( Map wizardData ) {
      /*
       * wizardData contains the data entered by the user,
       * each key matches the name property of every widget
       * displayed by the wizard pages
       *
       * You have 3 options as return value
       * - a new Object built based on wizardData
       * - a Summary object that wraps the real object
       *   plus some additional information
       * - a deferred result that performs a long computation
       *   on a separate thread
       */
      return wizardData
   }

   boolean cancel( Map settings ) {
      return true
   }
}