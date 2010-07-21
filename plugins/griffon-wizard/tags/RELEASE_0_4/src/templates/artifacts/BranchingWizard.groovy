@artifact.package@

class @artifact.name@ {
   def builder // do not delete!

   // either define pages or a panel provider
   // initialPanelProvider will take precedence over initialPages
   def initialPages // =  ["One","Two"]
   // def initialPanelProvider = "First"

   def wizardForStep = { String stepId, Map settings ->
      /*
       * It is recommended that you cache any Wizards
       * created here to improve performance as this
       * closure may be called several times
       *
       * Return null to stay on the current wizard
       *
       * Use builder.wizard() to create new wizards
       *
       * WARNING - all stepIds must be unique, including
       *           those provided by subwizards
       *
       */
      def wizard = null
      switch(stepId) {
         /*
         case "step1": ...
         case "step2": ...
           ...
         */
      }

      return wizard
   }
}