package org.codehaus.griffon.macmenus

import java.awt.event.WindowEvent

class MacAboutDialogController {
    // this will be injected by Griffon
    def mvcName

    public void closeAbout(WindowEvent evt) {
        destroyMVCGroup(mvcName)
    }
}