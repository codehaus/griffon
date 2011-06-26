/*
 * Copyright 2009-2011 the original author or authors.
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

package griffon.plugins.slideware

import java.awt.Dimension
import javax.swing.text.AttributeSet
import javax.swing.text.Element
import javax.swing.text.SimpleAttributeSet
import javax.swing.text.Style
import javax.swing.text.StyleConstants
import javax.swing.text.html.HTML

import groovy.ui.SystemOutputInterceptor

import org.codehaus.groovy.runtime.InvokerHelper
import org.codehaus.groovy.control.ErrorCollector
import org.codehaus.groovy.control.MultipleCompilationErrorsException
import org.codehaus.groovy.control.messages.SyntaxErrorMessage
import org.codehaus.groovy.control.messages.ExceptionMessage
import org.codehaus.groovy.syntax.SyntaxException

import griffon.transform.Threading

/**
 * @author Andres Almiray
 */
class GroovyCodeEditorController {
    def model
    def view

    private GroovyShell shell = new GroovyShell()
    private int scriptCount = 0
    private int maxOutputChars = 20000
    private SystemOutputInterceptor systemOutInterceptor

    void mvcGroupInit(Map<String, Object> args){
        systemOutInterceptor = new SystemOutputInterceptor({ String str ->
            appendOutput(str, model.styles.outputStyle)
            false
        })
    }

    def runAction = { evt = null ->
        if(!model.code) return
        def scriptText = model.code
        clearOutput()
        systemOutInterceptor.start()
        try {
            def result = shell.run(scriptText, "Script"+(scriptCount++), [])
            finishNormal(result)
        } catch(Throwable t) {
            finishException(t)
        } finally {
            systemOutInterceptor.stop()
        }
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    private void finishNormal(result) {
        ensureNoDocLengthOverflow(model.document)
        if(model.document.length) popOutputWindow()
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    private void finishException(t) {
        if (t instanceof MultipleCompilationErrorsException) {
            MultipleCompilationErrorsException mcee = t
            ErrorCollector collector = mcee.errorCollector
            int count = collector.errorCount
            appendOutputNl("${count} compilation error${count > 1 ? 's' : ''}:\n\n", model.styles.commandStyle)

            collector.errors.each { error ->
                if (error instanceof SyntaxErrorMessage) {
                    SyntaxException se = error.cause
                    int errorLine = se.line
                    String message = se.originalMessage
                    def doc = model.document
                    doc.insertString(doc.length, message + " at ", model.styles.stacktraceStyle)
                    doc.insertString(doc.length, "line: ${se.line}, column: ${se.column}\n\n", model.styles.stacktraceStyle)
                } else if (error instanceof Throwable) {
                    reportException(error)
                } else if (error instanceof ExceptionMessage) {
                    reportException(error.cause)
                }
            }
        } else {
            reportException(t)
        }
        popOutputWindow()
    }

    private void reportException(Throwable t) {
        appendOutputNl("Exception thrown: ", model.styles.commandStyle)
        appendOutput(t.message + '\n', model.styles.stacktraceStyle)

        StringWriter sw = new StringWriter()
        new PrintWriter(sw).withWriter {pw -> GriffonExceptionHandler.sanitize(t).printStackTrace(pw) }
        appendStacktrace("\n${sw.buffer}\n")
    }

    // Append a string to the output area
    private void appendOutput(String text, AttributeSet style){
        def doc = model.document
        doc.insertString(doc.length, text, style)
        ensureNoDocLengthOverflow(doc)
    }

    private void appendOutput(Object object, AttributeSet style) {
        appendOutput(object.toString(), style)
    }

    private void appendStacktrace(text) {
        def doc = model.document

        // split lines by new line separator
        def lines = text.split(/(\n|\r|\r\n|\u0085|\u2028|\u2029)/)

        // Java Identifier regex
        def ji = /([\p{Alnum}_\$][\p{Alnum}_\$]*)/

        // stacktrace line regex
        def stacktracePattern = /\tat $ji(\.$ji)+\((($ji(\.(java|groovy))?):(\d+))\)/

        lines.each { line ->
            int initialLength = doc.length
            doc.insertString(initialLength, line + '\n', model.styles.stacktraceStyle)
        }

        ensureNoDocLengthOverflow(doc)
    }

    // Append a string to the output area on a new line
    private void appendOutputNl(text, style){
        def doc = model.document
        def len = doc.length
        if (len > 0 && doc.getText(len - 1, 1) != "\n") {
            appendOutput("\n", style)
        }
        appendOutput(text, style)
    }

    private void clearOutput() {
        if(model.document.length) model.document.remove(0, model.document.length)
    }

    private void ensureNoDocLengthOverflow(doc) {
        if (doc.length > maxOutputChars) {
            doc.remove(0, doc.length - maxOutputChars)
        }
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_ASYNC)
    private void popOutputWindow() {
        view.outputWindow.pack()
        def es = view.groovyEditorContainer.size
        view.outputWindow.size = es
        view.outputWindow.visible = true
        view.outputWindow.locationRelativeTo = view.groovyEditorContainer
        view.outputWindow.requestFocus()
    }
}
