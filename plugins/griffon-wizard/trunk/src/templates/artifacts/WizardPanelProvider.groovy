@artifact.package@

class @artifact.name@ {
   def title = "title"
   def steps = ["step1", "step2"]
   def descriptions = ["Step Description 1", "Step Description 2"]

   /*
    * You must follow these rules:
    * - one closure per step, its name must match the stepId
    * - bind widget vlaues to settings explicitely
    * - use wizardController.problem to give feedback when something
    *   failed to be validated, a null value indicates no error,
    *   otherwise use a String
    */

   def step1 = { settings, wizardController ->
      textField( id: "tf1", text: "Add Content Here" )
      bean(settings, tf1: bind{ tf1.text })
   }

   def step2 = { settings, wizardController ->
      textField( id: "tf2", text: "Add Content Here" )
      bean(settings, tf2: bind{ tf2.text })
   }

   /*
    * Default implementation returns the settings Map
    */
   def onFinish = { settings ->
      return settings
   }

   /*
    * Default implementation cancels automatically
    */
   def onCancel = { settings ->
      return true
   }
}