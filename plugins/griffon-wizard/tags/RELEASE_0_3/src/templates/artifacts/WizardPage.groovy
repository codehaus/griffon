@artifact.package@

class @artifact.name@ {
    def stepId = 'step1' // must be unique per WizardPage
    def description = 'Step Description'
    def autoListen = true
 
    def pageContents = {
        // remember to always set a name: property to each input widget
        textField(name: 'tf1', text: 'Add Content Here') // delete me
    }
 
    // Either return a String that indicates a problem
    // or return a null valud indicating no problem
    def onValidate = { component, /*PropertyChangeEvent*/ event ->
        return null // no problem
    }

    // def allowBack = { String stepName, Map settings, /*Wizard*/ wizard ->
    //    // return values may be:
    //    // 'proceed', 'remain on page' or a subclass of WizardPanelNavResult
    // }

    // def allowFinish = { String stepName, Map settings, /*Wizard*/ wizard ->
    //    // return values may be:
    //    // 'proceed', 'remain on page' or a subclass of WizardPanelNavResult
    // }

    // def allowNext = { String stepName, Map settings, /*Wizard*/ wizard ->
    //    // return values may be:
    //    // 'proceed', 'remain on page' or a subclass of WizardPanelNavResult
    // }
}
