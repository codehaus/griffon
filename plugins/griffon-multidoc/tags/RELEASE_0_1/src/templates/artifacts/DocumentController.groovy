import javax.swing.JOptionPane
	
@artifact.package@class @artifact.name@ {
	def model
	def view
	def tabs

	void mvcGroupInit(Map args) {
		tabs = args.tabs
	}

    /**
     * Called when the tab with this document gains focus.
     */
    void activate(state) {
        // save the state object so we can signal the outer controller
        model.state = state
        // TODO: sync the model and document state
        state.dirty = model.dirty
    }

    /**
     * Called when the tab with this document loses focus.
     */
    void deactivate() {
        // forget the state object
        model.state = null
    }
	
	boolean create() {
		// TODO: perform any creating tasks
		doLater {
			view.contents.text = ''
		}
		return true
	}

    /**
     * Called when the document is initially opened.
     */
    boolean open() {
        // TODO: perform any opening tasks
		doOutside {
			def text = model?.file?.text ?: ''
			doLater {
				view.contents.text = text
			}
		}
        return true 
    }

    /**
     * Called when the document is closed.
     */
    boolean close() {
        if (model.dirty) {
			doLater {
	            switch (JOptionPane.showConfirmDialog(app.appFrames[0], 
	                    "Save changes to '${model.name}'?", app.appName, 
	                    JOptionPane.YES_NO_CANCEL_OPTION)){
	                case JOptionPane.YES_OPTION: return save()
	                case JOptionPane.NO_OPTION: return true
	                case JOptionPane.CANCEL_OPTION: return false
	            }
			}
        }
        return true
    }

    /**
     * Called when the document is saved.
     */
    boolean save() {
        // TODO: perform any saving tasks
		if (model.file) {
			doOutside {
				model.file.write(view.contents.text)
				markClean()
			}
		}
        return true
    }

    /**
     * Marks this document as 'dirty'.
     */
    void markDirty() {
        model.dirty = true
        if (model.state) { model.state.dirty = true }
        // TODO: update any other model/state properties
        setTitle(model.name + "*")
    }

    /**
     * Marks this document as 'clean'.
     */
    void markClean() {
        model.dirty = false
        if (model.state) { model.state.dirty = false }
        // TODO: update any other model/state properties
        setTitle(model.name)
    }

    /**
     * Sets this document's tab title.
     */
    void setTitle(title) {
        int index = tabs.indexOfComponent(view.root)
        if (index != -1) {
            doLater { tabs.setTitleAt(index, title) }
        }
    }
}
