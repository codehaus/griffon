package groovyx.ui.view

toolBar(id:'toolbar', rollover:true, visible:controller.showToolbar) {
    button(newFileAction, text:null)
    button(openAction, text:null)
    button(saveAction, text:null)
    separator()
    button(undoAction, text:null)
    button(redoAction, text:null)
    separator()
    button(cutAction, text:null)
    button(copyAction, text:null)
    button(pasteAction, text:null)
    separator()
    button(findAction, text:null)
    button(replaceAction, text:null)
    separator()
    button(historyPrevAction, text:null)
    button(historyNextAction, text:null)
    separator()
    button(runAction, text:null)
}
