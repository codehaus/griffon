@artifact.package@import groovy.beans.Bindable
import griffon.util.GriffonApplicationHelper as GAH
import javax.swing.JFileChooser

class @artifact.name@ {
	// various state
	@Bindable boolean open = false
	@Bindable boolean dirty = false
	@Bindable Map activeDocument = null  
	@Bindable List openDocuments = []
	
	// set from the view
	private JFileChooser fileChooser = new JFileChooser()
	private int counter = 0
	def tabs
	def app
	
	void activeDocumentChanged() {
		// de-activate the existing active document
		if (activeDocument) { 
			activeDocument.controller.deactivate() 
		}
		
		// figure out the new active document
		int index = tabs.selectedIndex
		setOpen(index != -1)
		setActiveDocument(open ? openDocuments[index] : null)
		if (activeDocument) { 
			activeDocument.controller.activate(this) 
		}
	}
	
	def newDocument = { evt = null ->
		def id = "Document" + (counter++)
		def document = GAH.buildMVCGroup(app, [id: id, name: 'Untitled', tabs: tabs], '@document.group@', id)
		if (document.controller.create()) {
			openDocuments.add(document)
			tabs.addTab(document.model.name, document.view.root)
			tabs.selectedIndex = tabs.tabCount - 1
		} else {
			GAH.destroyMVCGroup(app, id)
		}
	}
	
	def openDocument = { evt = null ->
		// pop up a Open File dialog
		int result = fileChooser.showOpenDialog(app.appFrames[0])
		if (result != JFileChooser.APPROVE_OPTION) { return }
		
		// get the selected file
		def file = fileChooser.selectedFile
		def id = file.absolutePath
		def name = file.name
		
		// check to see if the document is already open
		def open = openDocuments.find { it.model.id == id }
		if (open) {
			tabs.selectedIndex = model.openDocuments.indexOf(open)
		} else {
			def document = GAH.buildMVCGroup(app, [id: id, name: name, file: file, tabs: tabs], '@document.group@', id)
			if (document.controller.open()) {
				openDocuments.add(document)
				tabs.addTab(document.model.name, document.view.root)
				tabs.selectedIndex = tabs.tabCount - 1
			} else {
				GAH.destroyMVCGroup(app, id)
			}
		}
	}
	
	def saveDocument = { evt = null ->
		activeDocument.controller.save()
	}
	
	def saveAsDocument = { evt = null ->
		// pop up a Save File dialog
		int result = fileChooser.showSaveDialog(app.appFrames[0])
		if (result != JFileChooser.APPROVE_OPTION) { return false }
		def file = fileChooser.selectedFile
		
		// update the document model properties
		activeDocument.model.file = fileChooser.selectedFile
		activeDocument.model.id = file.absolutePath
		activeDocument.model.name = file.name
		
		// call save
		activeDocument.controller.save()
	}
	
	def closeDocument = { evt = null ->	
		if (activeDocument.controller.close()) {
			int index = openDocuments.indexOf(activeDocument)
			GAH.destroyMVCGroup(app, activeDocument.model.id)
			openDocuments.remove(index)
			tabs.removeTabAt(index)
		}
	}
}
