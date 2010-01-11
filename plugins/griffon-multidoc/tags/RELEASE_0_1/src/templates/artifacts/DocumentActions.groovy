actions {
    action(
        id: 'newDocumentAction', 
        name: 'New', 
        accelerator: shortcut('N'), 
        closure: model.state.newDocument
    )
    action(
        id: 'openDocumentAction', 
        name: 'Open', 
        accelerator: shortcut('O'), 
        closure: model.state.openDocument
    )
    action(
        id: 'closeDocumentAction', 
        name: 'Close', 
        accelerator: shortcut('W'), 
        closure: model.state.closeDocument, 
		enabled: bind(source: model.state, sourceProperty: 'open')
    )
    action(
        id: 'saveDocumentAction', 
        name: 'Save', 
        accelerator: shortcut('S'), 
        closure: model.state.saveDocument, 
		enabled: bind(source: model.state, sourceProperty: 'dirty')
    )
    action(
		id: 'saveAsDocumentAction', 
		name: 'Save As...', 
		accelerator: shortcut('S', 1), 
		closure: model.state.saveAsDocument, 
		enabled: bind(source: model.state, sourceProperty: 'open')
	)
}