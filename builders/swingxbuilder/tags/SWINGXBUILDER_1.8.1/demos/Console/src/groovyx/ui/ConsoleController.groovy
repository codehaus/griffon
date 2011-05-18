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

import groovy.inspect.swingui.ObjectBrowser
import groovy.swing.SwingXBuilder
import groovy.ui.ConsoleTextEditor
import groovy.ui.SystemOutputInterceptor
import groovy.ui.text.FindReplaceUtility
import java.awt.Component
import java.awt.EventQueue
import java.awt.Font
import java.awt.Toolkit
import java.awt.event.ActionEvent
import java.util.prefs.Preferences
import javax.swing.*
import javax.swing.event.CaretEvent
import javax.swing.event.CaretListener
import javax.swing.text.Element
import javax.swing.text.Style
import org.codehaus.groovy.runtime.InvokerHelper
import org.codehaus.groovy.runtime.StackTraceUtils

/**
 * Groovy Swing console.
 *
 * Allows user to interactively enter and execute Groovy.
 *
 * @version $Id: Console.groovy 8776 2007-10-23 22:00:21Z shemnon $
 * @author Danno Ferrin
 * @author Dierk Koenig, changed Layout, included Selection sensitivity, included ObjectBrowser
 * @author Alan Green more features: history, System.out capture, bind result to _
 */
class ConsoleController implements CaretListener {

    private prefs = Preferences.userNodeForPackage(ConsoleController)

    // Whether or not std output should be captured to the console
    boolean captureStdOut = prefs.getBoolean('captureStdOut', true)

    boolean fullStackTraces = prefs.getBoolean('fullStackTraces',
        Boolean.valueOf(System.getProperty("groovy.full.stacktrace", "false")))
    Action fullStackTracesAction

    boolean showToolbar = prefs.getBoolean('showToolbar', true)
    Component toolbar
    Action showToolbarAction

    // Maximum size of history
    int maxHistory = 10

    // Maximum number of characters to show on console at any time
    int maxOutputChars = 20000

    // UI
    JFrame frame
    ConsoleTextEditor inputEditor
    JTextPane inputArea
    JTextPane outputArea
    JLabel statusLabel
    JDialog runWaitDialog
    JLabel rowNumAndColNum

    // row info
    Element rootElement
    int cursorPos
    int rowNum
    int colNum

    // Styles for output area
    Style promptStyle
    Style commandStyle
    Style outputStyle
    Style resultStyle

    // Internal history
    List history = []
    int historyIndex = 1 // valid values are 0..history.length()
    HistoryRecord pendingRecord = new HistoryRecord( allText: "", selectionStart: 0, selectionEnd: 0)
    Action prevHistoryAction
    Action nextHistoryAction

    // Current editor state
    boolean dirty
    Action saveAction
    int textSelectionStart  // keep track of selections in inputArea
    int textSelectionEnd
    def scriptFile

    // Running scripts
    GroovyShell shell
    int scriptNameCounter = 0
    SystemOutputInterceptor systemOutInterceptor
    def runThread = null
    Closure beforeExecution
    Closure afterExecution


    ConsoleController() {
        this(new Binding())
    }

    ConsoleController(Binding binding) {
        this(null, binding)
    }

    ConsoleController(ClassLoader parent, Binding binding) {
        shell = new GroovyShell(parent,binding)
        try {
            System.setProperty("groovy.full.stacktrace",
                Boolean.toString(Boolean.valueOf(System.getProperty("groovy.full.stacktrace", "false"))))
        } catch (SecurityException se) {
            fullStackTracesAction.enabled = false;
        }
    }

    public void installInterceptor() {
        systemOutInterceptor = new SystemOutputInterceptor(this.&notifySystemOut)
        systemOutInterceptor.start()
    }

    void addToHistory(record) {
        history.add(record)
        // history.size here just retrieves method closure
        if (history.size() > maxHistory) {
            history.remove(0)
        }
        // history.size doesn't work here either
        historyIndex = history.size()
        updateHistoryActions()
    }

    // Append a string to the output area
    void appendOutput(text, style){
        def doc = outputArea.styledDocument
        doc.insertString(doc.length, text, style)

        // Ensure we don't have too much in console (takes too much memory)
        if (doc.length > maxOutputChars) {
            doc.remove(0, doc.length - maxOutputChars)
        }
    }

    // Append a string to the output area on a new line
    void appendOutputNl(text, style){
        def doc = outputArea.styledDocument
        def len = doc.length
        if (len > 0 && doc.getText(len - 1, 1) != "\n") {
            appendOutput("\n", style)
        }
        appendOutput(text, style)
    }

    // Return false if use elected to cancel
    boolean askToSaveFile() {
        if (scriptFile == null || !dirty) {
            return true
        }
        switch (JOptionPane.showConfirmDialog(frame,
            "Save changes to " + scriptFile.name + "?",
            "GroovyConsole", JOptionPane.YES_NO_CANCEL_OPTION))
        {
            case JOptionPane.YES_OPTION:
                return fileSave()
            case JOptionPane.NO_OPTION:
                return true
            default:
                return false
        }
    }

    void beep() {
        Toolkit.defaultToolkit.beep()
    }

    // Binds the "_" and "__" variables in the shell
    void bindResults() {
        shell.setVariable("_", getLastResult()) // lastResult doesn't seem to work
        shell.setVariable("__", history.collect {it.result})
    }

    // Handles menu event
    void captureStdOut(EventObject evt) {
        captureStdOut = evt.source.selected
        prefs.putBoolean('captureStdOut', captureStdOut)
    }

    void fullStackTraces(EventObject evt) {
        fullStackTraces = evt.source.selected
        System.setProperty("groovy.full.stacktrace",
            Boolean.toString(fullStackTraces))
        prefs.putBoolean('fullStackTraces', fullStackTraces)
    }

    void showToolbar(EventObject evt) {
        showToolbar = evt.source.selected
        prefs.putBoolean('showToolbar', showToolbar)
        toolbar.visible = showToolbar
    }

    void caretUpdate(CaretEvent e){
        textSelectionStart = Math.min(e.dot,e.mark)
        textSelectionEnd = Math.max(e.dot,e.mark)

        setRowNumAndColNum()
    }

    void clearOutput(EventObject evt = null) {
        outputArea.setText('')
    }

    // Confirm whether to interrupt the running thread
    void confirmRunInterrupt(EventObject evt) {
        def rc = JOptionPane.showConfirmDialog(frame, "Attempt to interrupt script?",
            "GroovyConsole", JOptionPane.YES_NO_OPTION)
        if (rc == JOptionPane.YES_OPTION && runThread != null) {
            runThread.interrupt()
        }
    }

    void exit(EventObject evt = null) {
        if (askToSaveFile()) {
            frame.hide()
            frame.dispose()
            FindReplaceUtility.dispose()
        }

        systemOutInterceptor.stop()
    }

    void fileNewFile(EventObject evt = null) {
        if (askToSaveFile()) {
            scriptFile = null
            setDirty(false)
            inputArea.text = ''
        }
    }

    // Start a new window with a copy of current variables
    void fileNewWindow(EventObject evt = null) {
        ConsoleController consoleController = new ConsoleController(
            new Binding(
                new HashMap(shell.context.variables)))
        consoleController.systemOutInterceptor = systemOutInterceptor
        SwingXBuilder swing = new SwingXBuilder()
        swing.controller = consoleController
        swing.build(ConsoleActions)
        swing.build(ConsoleView)
        installInterceptor()
        swing.consoleFrame.pack()
        swing.consoleFrame.show()
    }

    void fileOpen(EventObject evt = null) {
        scriptFile = selectFilename()
        if (scriptFile != null) {
            inputArea.text = scriptFile.readLines().join('\n')
            setDirty(false)
            inputArea.caretPosition = 0
        }
    }

    // Save file - return false if user cancelled save
    boolean fileSave(EventObject evt = null) {
        if (scriptFile == null) {
            return fileSaveAs(evt)
        } else {
            scriptFile.write(inputArea.text)
            setDirty(false)
            return true
        }
    }

    // Save file - return false if user cancelled save
    boolean fileSaveAs(EventObject evt = null) {
        scriptFile = selectFilename("Save")
        if (scriptFile != null) {
            scriptFile.write(inputArea.text)
            setDirty(false)
            return true
        } else {
            return false
        }
    }

    def finishException(Throwable t) {
        statusLabel.text = 'Execution terminated with exception.'
        history[-1].exception = t

        appendOutputNl("Exception thrown: ", promptStyle)
        appendOutput(t.toString(), resultStyle)

        StringWriter sw = new StringWriter()
        new PrintWriter(sw).withWriter { pw -> StackTraceUtils.deepSanitize(t).printStackTrace(pw) }

        appendOutputNl("\n${sw.buffer}\n", outputStyle)
        bindResults()
    }

    def finishNormal(Object result) {
        // Take down the wait/cancel dialog
        history[-1].result = result
        if (result != null) {
            statusLabel.text = 'Execution complete.'
            appendOutputNl("Result: ", promptStyle)
            appendOutput("${InvokerHelper.inspect(result)}", resultStyle)
        } else {
            statusLabel.text = 'Execution complete. Result was null.'
        }
        bindResults()
    }

    // Gets the last, non-null result
    def getLastResult() {
        // runtime bugs in here history.reverse produces odd lookup
        // return history.reverse.find {it != null}
        if (!history) {
            return
        }
        for (i in (history.size() - 1)..0) {
            if (history[i].result != null) {
                return history[i].result
            }
        }
        return null
    }

    // Allow access to shell from outside console
    // (useful for configuring shell before startup)
    GroovyShell getShell() {
        return shell
    }

    void historyNext(EventObject evt = null) {
        if (historyIndex < history.size()) {
            setInputTextFromHistory(historyIndex + 1)
        } else {
            statusLabel.text = "Can't go past end of history (time travel not allowed)"
            beep()
        }
    }

    void historyPrev(EventObject evt = null) {
        if (historyIndex > 0) {
            setInputTextFromHistory(historyIndex - 1)
        } else {
            statusLabel.text = "Can't go past start of history"
            beep()
        }
    }

    void inspectLast(EventObject evt = null){
        if (null == lastResult) {
            JOptionPane.showMessageDialog(frame, "The last result is null.",
                "Cannot Inspect", JOptionPane.INFORMATION_MESSAGE)
            return
        }
        ObjectBrowser.inspect(lastResult)
    }

    void inspectVariables(EventObject evt = null) {
        ObjectBrowser.inspect(shell.context.variables)
    }

    void largerFont(EventObject evt = null) {
        if (inputArea.font.size > 40) return
        def newFont = new Font('Monospaced', Font.PLAIN, inputArea.font.size + 1)
        inputArea.font = newFont
        outputArea.font = newFont
    }

    Boolean notifySystemOut(String str) {
        if (!captureStdOut) {
            // Output as normal
            return true
        }

        // Put onto GUI
        if (EventQueue.isDispatchThread()) {
            appendOutput(str, outputStyle)
        }
        else {
            SwingUtilities.invokeLater {
                appendOutput(str, outputStyle)
            }
        }
        return false
    }

    // actually run the script

    void runScript(EventObject evt = null) {
        runScriptImpl(false)
    }

    void runSelectedScript(EventObject evt = null) {
        runScriptImpl(true)
    }

    private void runScriptImpl(boolean selected) {
        def endLine = System.getProperty('line.separator')
        def record = new HistoryRecord( allText: inputArea.getText().replaceAll(endLine, '\n'),
            selectionStart: textSelectionStart, selectionEnd: textSelectionEnd)
        addToHistory(record)
        pendingRecord = new HistoryRecord(allText:'', selectionStart:0, selectionEnd:0)

        // Print the input text
        for (line in record.getTextToRun(selected).tokenize("\n")) {
            appendOutputNl('groovy> ', promptStyle)
            appendOutput(line, commandStyle)
        }

        //appendOutputNl("") - with wrong number of args, causes StackOverFlowError
        appendOutputNl("\n", promptStyle)

        // Kick off a new thread to do the evaluation
        statusLabel.text = 'Running Script...'

        // Run in separate thread, so that System.out can be captured
        runThread = Thread.start {
            try {
                SwingUtilities.invokeLater { showRunWaitDialog() }
                String name = "Script${scriptNameCounter++}"
                if(beforeExecution) {
                    beforeExecution()
                }
                def result = shell.evaluate(record.getTextToRun(selected), name)
                if(afterExecution) {
                    afterExecution()
                }
                SwingUtilities.invokeLater { finishNormal(result) }
            } catch (Throwable t) {
                SwingUtilities.invokeLater { finishException(t) }
            } finally {
                SwingUtilities.invokeLater {
                    runWaitDialog.hide()
                    runThread = null
                }
            }
        }
    }

    def selectFilename(name = "Open") {
        def fc = new JFileChooser()
        fc.fileSelectionMode = JFileChooser.FILES_ONLY
        fc.acceptAllFileFilterUsed = true
        if (fc.showDialog(frame, name) == JFileChooser.APPROVE_OPTION) {
            return fc.selectedFile
        } else {
            return null
        }
    }

    void setDirty(boolean newDirty) {
        //TODO when @BoundProperty is live, this should be handled via listeners
        dirty = newDirty
        saveAction.enabled = newDirty
        updateTitle()
    }

    private void setInputTextFromHistory(newIndex) {
        def endLine = System.getProperty('line.separator')
        if (historyIndex >= history.size()) {
            pendingRecord = new HistoryRecord( allText: inputArea.getText().replaceAll(endLine, '\n'),
                selectionStart: textSelectionStart, selectionEnd: textSelectionEnd)
        }
        historyIndex = newIndex
        def record
        if (historyIndex < history.size()) {
            record = history[historyIndex]
            statusLabel.text = "command history ${history.size() - historyIndex}"
        } else {
            record = pendingRecord
            statusLabel.text = 'at end of history'
        }
        inputArea.text = record.allText
        inputArea.selectionStart = record.selectionStart
        inputArea.selectionEnd = record.selectionEnd
        setDirty(true) // Should calculate dirty flag properly (hash last saved/read text in each file)
        updateHistoryActions()
    }

    private void updateHistoryActions() {
        nextHistoryAction.enabled = historyIndex < history.size()
        prevHistoryAction.enabled = historyIndex > 0
    }


    // Adds a variable to the binding
    // Useful for adding variables before openning the console
    void setVariable(String name, Object value) {
        shell.context.setVariable(name, value)
    }

    void showAbout(EventObject evt = null) {
        def version = InvokerHelper.getVersion()

        SwingXBuilder swing = new SwingXBuilder()
        def pane = swing.optionPane()
         // work around GROOVY-1048
        pane.setMessage('Welcome to the Groovy Console for evaluating Groovy scripts\nVersion ' + version)
        def dialog = pane.createDialog(frame, 'About GroovyConsole')
        dialog.show()
    }

    void find(EventObject evt = null) {
        FindReplaceUtility.showDialog()
    }

    void findNext(EventObject evt = null) {
        FindReplaceUtility.FIND_ACTION.actionPerformed(evt)
    }

    void findPrevious(EventObject evt = null) {
        def reverseEvt = new ActionEvent(
            evt.getSource(), evt.getID(),
            evt.getActionCommand(), evt.getWhen(),
            ActionEvent.SHIFT_MASK) //reverse
        FindReplaceUtility.FIND_ACTION.actionPerformed(reverseEvt)
    }

    void replace(EventObject evt = null) {
        FindReplaceUtility.showDialog(true)
    }


    // Shows the 'wait' dialog
    void showRunWaitDialog() {
        runWaitDialog.pack()
        int x = frame.x + (frame.width - runWaitDialog.width) / 2
        int y = frame.y + (frame.height - runWaitDialog.height) / 2
        runWaitDialog.setLocation(x, y)
        runWaitDialog.show()
    }

    void smallerFont(EventObject evt = null){
        if (inputArea.font.size < 5) return
        def newFont = new Font('Monospaced', Font.PLAIN, inputArea.font.size - 1)
        inputArea.font = newFont
        outputArea.font = newFont
    }

    void updateTitle() {
        if (scriptFile != null) {
            frame.title = scriptFile.name + (dirty?" * ":"") + " - GroovyConsole"
        } else {
            frame.title = "GroovyConsole"
        }
    }

    void invokeTextAction(evt, closure) {
        def source = evt.getSource()
        if (source != null) {
            closure(inputArea)
        }
    }

    void cut(EventObject evt = null) {
        invokeTextAction(evt, { source -> source.cut() })
    }

    void copy(EventObject evt = null) {
        invokeTextAction(evt, { source -> source.copy() })
    }

    void paste(EventObject evt = null) {
        invokeTextAction(evt, { source -> source.paste() })
    }

    void selectAll(EventObject evt = null) {
        invokeTextAction(evt, { source -> source.selectAll() })
    }

    void setRowNumAndColNum() {
        cursorPos = inputArea.getCaretPosition()
        rowNum = rootElement.getElementIndex(cursorPos) + 1

        def rowElement = rootElement.getElement(rowNum - 1)
        colNum = cursorPos - rowElement.getStartOffset() + 1

        rowNumAndColNum.setText("$rowNum:$colNum")
    }

    void print(EventObject evt = null) {
        inputEditor.printAction.actionPerformed(evt)
    }

    void undo(EventObject evt = null) {
        inputEditor.undoAction.actionPerformed(evt)
    }

    void redo(EventObject evt = null) {
        inputEditor.redoAction.actionPerformed(evt)
    }


}