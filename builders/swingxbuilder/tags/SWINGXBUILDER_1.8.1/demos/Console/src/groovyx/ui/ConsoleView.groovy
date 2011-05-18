/*
 * Copyright 2003-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package groovyx.ui

import groovyx.ui.view.*
import javax.swing.UIManager
import static javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE

switch (UIManager.getSystemLookAndFeelClassName()) {
    case 'com.sun.java.swing.plaf.windows.WindowsLookAndFeel':
    case 'com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel':
        build(WindowsDefaults)
        break

    case 'apple.laf.AquaLookAndFeel':
        build(MacOSXDefaults)
        break

    case 'com.sun.java.swing.plaf.gtk.GTKLookAndFeel':
        build(GTKDefaults)
        break

    default:
        build(Defaults)
        break
}

frame(
    title: 'GroovyConsole',
    location: [100,100], // in groovy 2.0 use platform default location
    iconImage: imageIcon("/groovy/ui/ConsoleIcon.png").image,
    defaultCloseOperation: DO_NOTHING_ON_CLOSE,
    id:'consoleFrame'
) {
    build(menuBarClass)

    build(contentPaneClass)

    build(toolBarClass)

    build(statusBarClass)

    dialog(title: 'Groovy executing',
        modal: true,
        id:'runWaitDialog',
        pack:true
    ) {
        vbox(border: emptyBorder(6)) {
            label(text: "Groovy is now executing. Please wait.", alignmentX: 0.5f)
            vstrut()
            button(interruptAction,
                margin: [10, 20, 10, 20],
                alignmentX: 0.5f
            )
        }
    }
}


controller.promptStyle = promptStyle
controller.commandStyle = commandStyle
controller.outputStyle = outputStyle
controller.resultStyle = resultStyle

// add the window close handler
consoleFrame.windowClosing = controller.&exit

// link in references to the controller
controller.inputEditor = inputEditor
controller.inputArea = inputEditor.textEditor
controller.outputArea = outputArea
controller.statusLabel = status
controller.frame = consoleFrame
controller.runWaitDialog = runWaitDialog
controller.rowNumAndColNum = rowNumAndColNum
controller.toolbar = toolbar

// link actions
controller.saveAction = saveAction
controller.prevHistoryAction = historyPrevAction
controller.nextHistoryAction = historyNextAction
controller.fullStackTracesAction = fullStackTracesAction
controller.showToolbarAction = showToolbarAction

// some more UI linkage
controller.inputArea.addCaretListener(controller)
controller.inputArea.document.undoableEditHappened = { controller.dirty = true }
controller.rootElement = inputArea.document.defaultRootElement



// don't send any return value from the view, all items should be referenced via the bindings
return null
