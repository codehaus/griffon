/*
 * Copyright 2007-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 */

import java.awt.Font
import java.awt.Robot
import javax.swing.JFileChooser
import javax.swing.JOptionPane
import javax.swing.SwingConstants
import java.awt.event.ActionEvent
import javax.imageio.ImageIO
import groovy.swing.j2d.GraphicsRenderer
import groovy.swing.j2d.GraphicsBuilder
import groovy.swing.j2d.GraphicsOperation
import groovy.swing.j2d.svg.*
import groovy.text.SimpleTemplateEngine
import java.util.prefs.Preferences
import groovy.ui.text.FindReplaceUtility
import javax.xml.parsers.SAXParserFactory
import org.xml.sax.*
import org.codehaus.groovy.runtime.StackTraceUtils

class MainController {
   def model
   def view
   def builder

   private prefs = Preferences.userNodeForPackage(MainController)
   private File currentFileChooserDir = new File(prefs.get('currentFileChooserDir', '.'))
   private File currentSnapshotDir = new File(prefs.get('currentSnapshotDir', '.'))
   private templateEngine = new SimpleTemplateEngine()
   private sampleScript
   private simple_script_source
   private export_script_source
   private GraphicsRenderer gr
   private GraphicsBuilder gb
   private SVGRenderer svgr
   private runThread = null
   private xmlReader = SAXParserFactory.newInstance().newSAXParser().xMLReader
   private URL nodeReferenceURL
   private static int scriptCounter = 0
   private Set factorySet = new TreeSet()

   void mvcGroupInit( Map args ) {
      gr = new GraphicsRenderer()
      gb = gr.gb
      svgr = new SVGRenderer()
      Thread.start {
         simple_script_source = Thread.currentThread().contextClassLoader.
                   getResourceAsStream("simple-script.txt").text
         export_script_source = Thread.currentThread().contextClassLoader.
                   getResourceAsStream("export-script.txt").text
         nodeReferenceURL = Thread.currentThread().contextClassLoader.
                   getResource("node-reference.html")
         sampleScript = Thread.currentThread().contextClassLoader.
                   getResourceAsStream("sample-script.txt").text
      }
   }

   def updateTitle = { ->
      // TODO handle undo!
      if( model.scriptFile ) {
         return model.scriptFile.name + (model.dirty ? " *" : "") + " - GraphicsPad"
      }
      return "GraphicsPad"
   }

   def newDrawing = { evt = null ->
      if( askToSaveFile(evt) ) {
         model.scriptFile = null
         model.dirty = false
         view.editor.textEditor.text = ''
         view.editor.textEditor.requestFocus()
      }
   }

   def open = { evt = null ->
      model.scriptFile = selectFilename()
      if( !model.scriptFile ) return
      doOutside {
         def scriptText = model.scriptFile.readLines().join('\n')
         doLater {
            if( !scriptText ) return
            // need 2-way binding!
            view.editor.textEditor.text = scriptText
            model.dirty = false
            view.editor.textEditor.caretPosition = 0
            view.editor.textEditor.requestFocus()
         }
      }
   }

   def save = { evt = null ->
      if( !model.scriptFile ) return saveAs(evt)
      model.scriptFile.write(model.content)
      model.dirty = false
      return true
   }

   def saveAs = { evt = null ->
      model.scriptFile = selectFilename("Save")
      if( model.scriptFile ) {
         model.scriptFile.write(model.content)
         model.dirty = false
         return true
      }
      return false
   }

   def exit = { evt = null ->
      if( askToSaveFile() ) {
         FindReplaceUtility.dispose()
         app.shutdown()
      }
   }

   def snapshot = { evt ->
      def fc = new JFileChooser(currentSnapshotDir)
      fc.fileSelectionMode = JFileChooser.FILES_ONLY
      fc.acceptAllFileFilterUsed = true
      if (fc.showDialog(app.appFrames[0], "Snapshot") == JFileChooser.APPROVE_OPTION) {
         currentSnapshotDir = fc.currentDirectory
         prefs.put('currentSnapshotDir', currentSnapshotDir.path)
         def frameBounds = app.appFrames[0].bounds
         def capture = new Robot().createScreenCapture(frameBounds)
         def filename = fc.selectedFile.name
         def dot = filename.lastIndexOf(".")
         def ext = "png"
         if( dot > 0 )  {
            ext = filename[dot+1..-1]
         } else {
            filename += ".$ext"
         }
         def target = new File(currentSnapshotDir,filename)
         ImageIO.write( capture, ext, target )
         def pane = builder.optionPane()
         pane.setMessage("Successfully saved snapshot to\n\n${target.absolutePath}")
         def dialog = pane.createDialog(app.appFrames[0], 'Snapshot')
         dialog.show()
      }
   }

   private void invokeTextAction( evt, closure ) {
      if( evt.source ) closure(view.editor.textEditor)
   }

   def cut = { evt = null -> invokeTextAction(evt, { source -> source.cut() }) }
   def copy = { evt = null -> invokeTextAction(evt, { source -> source.copy() }) }
   def paste = { evt = null -> invokeTextAction(evt, { source -> source.paste() }) }
   def selectAll = { evt = null -> invokeTextAction(evt, { source -> source.selectAll() }) }

   // TODO yet unconnected!!
   def find = { evt = null -> FindReplaceUtility.showDialog() }
   def findNext = { evt = null -> FindReplaceUtility.FIND_ACTION.actionPerformed(evt) }
   def findPrevious = { evt = null -> 
      def reverseEvt = new ActionEvent( evt.source, evt.iD, 
         evt.actionCommand, evt.when,
         ActionEvent.SHIFT_MASK) //reverse
      FindReplaceUtility.FIND_ACTION.actionPerformed(reverseEvt)
   }
   def replace = { evt = null -> FindReplaceUtility.showDialog(true) }

   def largerFont = { evt = null ->
      modifyFont(view.editor.textEditor, {it > 40}, +2)
      modifyFont(view.errors, {it > 40}, +2)
   }

   def smallerFont = { evt = null ->
      modifyFont(view.editor.textEditor, {it < 5}, -2)
      modifyFont(view.errors, {it < 5}, -2)
   }

   def showRulers = { evt = null ->
      def rh = evt?.source?.state ? view.rowHeader : view.emptyRowHeader
      def ch = evt?.source?.state ? view.columnHeader : view.emptyColumnHeader
      if( view.scroller.rowHeader.view != rh ) {
         view.scroller.rowHeaderView = rh
         view.scroller.columnHeaderView = ch
         view.scroller.repaint()
      }
   }

   def runScript = { evt = null ->
      runThread = Thread.start {
         try {
            doLater {
               model.status = "Running Script ..."
               if( !model.errors ) {
                  model.errors = ""
                  model.caretPosition = 0
               }
               view.canvas.removeAll()
               showDialog( "runWaitDialog" )
            }
            executeScript( model.content )
         } catch( Throwable t ) {
            doLater { finishWithException(t) }
         } finally {
            doLater {
               hideDialog( "runWaitDialog" )
               runThread = null
            }
         }
      }
   }
   
   def runSampleScript = { evt = null ->
      if( !model.errors ) {
         model.errors = ""
         model.caretPosition = 0
      }
      view.tabs.selectedIndex = 0 // sourceTab
      view.editor.textEditor.text = sampleScript
      runScript(evt)
   }

   def showToolbar = { evt = null ->
      def showToolbar = evt.source.selected
      prefs.putBoolean('showToolbar', showToolbar)
      view.toolbar.visible = showToolbar
   }

   def suggestNodeName = { evt = null ->
      if( !model.content ) return

      def editor = view.editor.textEditor
      def caret = editor.caretPosition
      if( !caret ) return

      def document = editor.document
      def target = ""
      def ch = document.getText(--caret,1)
      while( ch =~ /[a-zA-Z]/ ) {
         target = ch + target
         if( caret ) ch = document.getText(--caret,1)
         else break
      }
      if( target.size() != document.length ) caret++

      if( !factorySet ) populateFactorySet()
      def suggestions = factorySet.findAll{ it.startsWith(target) }
      if( !suggestions ) return
      if( suggestions.size() == 1 ) {
         model.suggestion = [
            start: caret,
            end: caret + target.size(),
            offset: target.size(),
            text: suggestions.iterator().next()
         ]
         writeSuggestion()
      } else {
         model.suggestion = [
            start: caret,
            end: caret + target.size(),
            offset: target.size()
         ]
         model.suggestions.clear()
         model.suggestions.addAll(suggestions)
         view.popup.showPopup(SwingConstants.CENTER, app.appFrames[0])
         view.suggestionList.selectedIndex = 0
      }
   }

   def codeComplete = { evt ->
      model.suggestion.text = model.suggestions[view.suggestionList.selectedIndex]
      view.popup.hidePopup(true)
      writeSuggestion()
   }

   private writeSuggestion() {
      if( !model.suggestion ) return

      def editor = view.editor.textEditor
      def document = editor.document
      def s = model.suggestion
      def text = s.text.substring(s.offset)
      document.insertString(s.start+s.offset, text, null)
      editor.requestFocus()

      // clear it!
      model.suggestion = [:]
   }

   def exportAsImage = { evt = null -> 
       showDialog("exportAsImageDialog")
   }
   def cancelExportAsImage = { evt = null ->
      view.imageWidth.text = "0"
      view.imageHeight.text = "0"
      view.imageFile.text = ""
      hideDialog( "exportAsImageDialog" )
   }
   def okExportAsImage = { evt = null ->
      def w = model.exportAsImageWidth
      def h = model.exportAsImageHeight
      def f = model.exportAsImageFile

      if( !w ){
         showAlert("Export as Image","Please type a valid width")
         return
      }
      if( !h ){
         showAlert("Export as Image","Please type a valid height")
         return
      }
      if( !f ){
         showAlert("Export as Image","Please select a file")
         return
      }

      w += model.exportAsImageBorder ? 1 : 0
      h += model.exportAsImageBorder ? 1 : 0

      cancelExportAsImage(evt)
      doOutside {
         def binding = [source:model.content]
         def template = templateEngine.createTemplate(simple_script_source).make(binding)
         def script = template.toString()
         def go = gb.group( [bc:'black'], new GroovyShell().evaluate(script) )
         if( go.operations.size() ){
            gr.renderToFile( f, w, h, go )
            showMessage("Export as Image", "Succesfully exported image to\n$f")
         }
      }
   }

   def exportAsScript = { evt = null -> 
      model.scriptFile = selectFilename("Export as Script")
      if( model.scriptFile ) {
         def binding = [source:model.content, title: model.scriptFile.name - ".groovy"]
         def template = templateEngine.createTemplate(export_script_source).make(binding)
         def script = template.toString()
         model.scriptFile.write(script)
         model.dirty = false
         return true
      }
      return false
   }

   def exportAsSvg = { evt = null -> 
       showDialog("exportAsSvgDialog")
   }
   def cancelExportAsSvg = { evt = null ->
      view.svgWidth.text = "0"
      view.svgHeight.text = "0"
      view.svgFile.text = ""
      hideDialog( "exportAsSvgDialog" )
   }
   def okExportAsSvg = { evt = null ->
      def w = model.exportAsSvgWidth
      def h = model.exportAsSvgHeight
      def f = model.exportAsSvgFile

      if( !w ){
         showAlert("Export as SVG","Please type a valid width")
         return
      }
      if( !h ){
         showAlert("Export as SVG","Please type a valid height")
         return
      }
      if( !f ){
         showAlert("Export as SVG","Please select a file")
         return
      }

      cancelExportAsSvg(evt)
      doOutside {
         def binding = [source:model.content]
         def template = templateEngine.createTemplate(simple_script_source).make(binding)
         def script = template.toString()
         def go = gb.group( [bc:'black'], new GroovyShell().evaluate(script) )
         if( go.operations.size() ){
            svgr.renderToFile( f, w, h, go )
            showMessage("Export as SVG", "Succesfully exported to\n$f")
         }
      }
   }

   def importFromSvg = { evt = null -> 
       showDialog("importFromSvgDialog")
   }
   def cancelImportFromSvg = { evt = null ->
      view.svgImportFile.text = ""
      hideDialog( "importFromSvgDialog" )
   }
   def okImportFromSvg = { evt = null ->
      def f = model.importFromSvgFile

      if( !f ){
         showAlert("Import from SVG","Please select a file")
         return
      }

      cancelImportFromSvg(evt)
      runThread = Thread.start {
         try{
            doLater {
               model.status = "Importing SVG ..."
               model.errors = ""
               view.canvas.removeAll()
               showDialog( "runWaitDialog" )
            }
            def svg = f.toURL().text
            model.status = "Converting SVG ..."
            def gfxCode = convertSvg( svg )
            model.status = "Running Script ..."
            doLater { view.editor.textEditor.text = gfxCode }
            executeScript( gfxCode )
         } catch (Throwable t) {
            doLater { finishWithException(t) }
         } finally {
            doLater {
               hideDialog( "runWaitDialog" )
               runThread = null
            }
         }
      }
   }

   def about = { evt = null ->
      def pane = builder.optionPane()
       // work around GROOVY-1048
      pane.setMessage('Welcome to the Groovy GraphicsPad')
      def dialog = pane.createDialog(app.appFrames[0], 'About GraphicsPad')
      dialog.show()
   }

   def showNodeReference = { evt = null ->
      showDialog( "nodeReferenceDialog", false )
      browseTo nodeReferenceURL
   }

   def confirmRunInterrupt = { evt = null ->
      def rc = JOptionPane.showConfirmDialog( app.appFrames[0], "Attempt to interrupt script?",
            "GraphicsPad", JOptionPane.YES_NO_OPTION)
      if( rc == JOptionPane.YES_OPTION && runThread ) {
          runThread.interrupt()
      }
   }

   def browseTo = { url ->
      if( model.currentPage?.toString() == url.toString() ) return
      doLater { model.currentPage = url instanceof URL ? url : new URL(url) }
   }

   private void finishNormal( go ) {
      if( go instanceof GraphicsOperation ){
         view.canvas.@graphicsOperation = null
         view.canvas.graphicsOperation = go
         model.status = 'Execution complete.'
      }
   }

   private void finishWithException( Throwable t ) {
      model.status = 'Execution terminated with exception.'
      StackTraceUtils.deepSanitize(t)
      t.printStackTrace()
      def baos = new ByteArrayOutputStream()
      t.printStackTrace(new PrintStream(baos))
      doLater {
         view.canvas.removeAll()
         view.canvas.repaint()
         view.tabs.selectedIndex = 1 // errorsTab
         model.errors = baos.toString()
         model.caretPosition = 0
      }
   }

   private void displayError( String message ) {
      doLater {
         view.canvas.removeAll()
         view.canvas.repaint()
         view.tabs.selectedIndex = 1 // errorsTab
         model.errors = message
         model.caretPosition = 0
      }
   }

   private void showAlert(title, message) {
      doLater {
         JOptionPane.showMessageDialog(app.appFrames[0], message,
               title, JOptionPane.WARNING_MESSAGE)
      }
   }

   private void showMessage(title, message) {
      doLater {
         JOptionPane.showMessageDialog(app.appFrames[0], message,
               title, JOptionPane.INFORMATION_MESSAGE)
      }
   }

   private void showDialog( dialogName, pack = true ) {
      def dialog = view."$dialogName"
      if( pack ) dialog.pack()
      int x = app.appFrames[0].x + (app.appFrames[0].width - dialog.width) / 2
      int y = app.appFrames[0].y + (app.appFrames[0].height - dialog.height) / 2
      dialog.setLocation(x, y)
      dialog.show()
   }

   private void hideDialog( dialogName ) {
      def dialog = view."$dialogName"
      dialog.hide()
   }

   private selectFilename( name = "Open" ) {
      // should use builder.fileChooser() ?
      def fc = new JFileChooser(currentFileChooserDir)
      fc.fileSelectionMode = JFileChooser.FILES_ONLY
      fc.acceptAllFileFilterUsed = true
      if( fc.showDialog(app.appFrames[0], name ) == JFileChooser.APPROVE_OPTION ) {
         currentFileChooserDir = fc.currentDirectory
         Preferences.userNodeForPackage(MainController).put(
            'currentFileChooserDir', currentFileChooserDir.path)
         return fc.selectedFile
      }
      return null
   }

   private boolean askToSaveFile(evt) {
      if( !model.scriptFile || !model.dirty ) return true
      switch( JOptionPane.showConfirmDialog( app.appFrames[0],
                 "Save changes to " + model.scriptFile.name + "?",
                 "GraphicsPad", JOptionPane.YES_NO_CANCEL_OPTION)){
         case JOptionPane.YES_OPTION: return save(evt)
         case JOptionPane.NO_OPTION: return true
      }
      return false
   }

   private void executeScript( codeSource ) {
      try {
         def binding = [source:codeSource]
         def template = templateEngine.createTemplate(simple_script_source).make(binding)
         def script = template.toString()
         binding = new Binding()
         def output = new GroovyShell(binding).evaluate(script)
         def go = gb.group( output )
         if( !go.operations.size() ){
            throw new RuntimeException("An operation is not recognized. Please check your code.")
         }
         doLater { finishNormal(go) }
      } catch( Throwable t ) {
         doLater { finishWithException(t) }
      }
   }

   private convertSvg( svgText ) {
      def writer = new StringWriter()
      def handler = new Svg2GroovyHandler(writer)
      xmlReader.contentHandler = handler
      xmlReader.parse( new InputSource(new StringReader(svgText)) )
      return writer.toString()
   }

   private modifyFont( target, sizeFilter, sizeMod ) {
      def currentFont = target.font
      if( sizeFilter(currentFont.size) ) return
      target.font = new Font( 'Monospaced', currentFont.style, currentFont.size + sizeMod )
   }

   private populateFactorySet() {
      factorySet.clear()
      factorySet.addAll(gb.factories.keySet())
   }
}
