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

import groovy.swing.SwingXBuilder
import org.codehaus.groovy.runtime.StackTraceUtils

/** JNLP Hack **/
PolicyHack.setPolicy()


// allow the full stack traces to bubble up to the root logger
java.util.logging.Logger.getLogger(StackTraceUtils.STACK_LOG_NAME).useParentHandlers = true
// tweak what we filter out to be fairly broad
System.setProperty("groovy.sanitized.stacktraces", """org.codehaus.groovy.runtime.
        org.codehaus.groovy.
        groovy.lang.
        gjdk.groovy.lang.
        sun.
        java.lang.reflect.
        java.lang.Thread
        groovy.ui.Console""")

swing = new SwingXBuilder()

// adjust the look and feel aspects.  These can be moved into the platform specific views
swing.lookAndFeel('system')

// add controller to the swingBuilder bindings
controller = new ConsoleController()
swing.controller = controller

// create the actions
swing.build(ConsoleActions)

// create the view
swing.build(ConsoleView)

controller.bindResults()

// stitch some actions togeather
swing.bind(source:swing.inputEditor.undoAction, sourceProperty:'enabled', target:swing.undoAction, targetProperty:'enabled')
swing.bind(source:swing.inputEditor.redoAction, sourceProperty:'enabled', target:swing.redoAction, targetProperty:'enabled')

swing.consoleFrame.pack()
swing.consoleFrame.show()
controller.installInterceptor()
swing.doLater controller.inputArea.&requestFocus

// other people calling this may want to link in strange things
return controller