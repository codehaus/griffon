@artifact.package@

import griffon.builder.wizard.GriffonWizardPage

class @artifact.name@ extends GriffonWizardPage {
   @artifact.name@() {
      super("stepId", "Step Description", true)
   }

   def pageContents = {
      // remember to always set a name: property to each input widget
      textField( name: "tf1", text: "Add Content Here" ) // delete me
   }

   /*
   def validator = { component, event ->

   }
   */
}