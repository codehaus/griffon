@artifact.package@class @artifact.name@ {
    final String pageId = 'step1' // must be unique per WizardPage
    final String pageDescription = 'Step Description'
    final boolean autoListen = true
 
    final Closure contents = {
        // remember to always set a name: property to each input widget
        textField(name: 'tf1', text: 'Add Content Here') // delete me
    }
}
