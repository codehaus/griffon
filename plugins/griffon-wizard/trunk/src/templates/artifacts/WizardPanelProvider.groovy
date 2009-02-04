@artifact.package@

import griffon.builder.wizard.GriffonWizardPanelProvider

class @artifact.name@ extends GriffonWizardPanelProvider {
   @artifact.name@() {
      super(
         "title",
         ["step1", "step2"] as String[],
         ["Step Description 1", "Step Description 2"] as String[]
      )
   }

   /*
    * Must create a closure per step, its name must match stepId
    */

   def step1 = { settings, controller ->
      textField( id: "tf1", text: "Add Content Here" )
      bean(settings, tf1: bind{ tf1.text })
   }

   def step2 = { settings, controller ->
      textField( id: "tf2", text: "Add Content Here" )
      bean(settings, tf2: bind{ tf2.text })
   }

   /*
    * Default implementation will return the settings Map
    *
   def onFinish = { settings ->
      return settings
   }
    */

   /*
    * Default implementation will cancel automatically
    *
   def onCancel = { settings ->
      return true
   }
    */
}