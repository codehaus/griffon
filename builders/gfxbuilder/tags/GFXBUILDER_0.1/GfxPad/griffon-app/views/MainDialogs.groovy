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

import java.awt.Insets
import java.awt.Font
import java.awt.BorderLayout as BL
import javax.swing.event.HyperlinkEvent
import javax.swing.UIManager
import org.apache.commons.httpclient.URI as CommonsURI

import static javax.swing.ScrollPaneConstants.*
import static java.awt.Color.*

def hiperlinkUpdateListener = { evt ->
   if( evt.eventType == HyperlinkEvent.EventType.ACTIVATED ) {
      def uri = new CommonsURI(evt.uRL.toString(),false)
      controller.browseTo( new URL(uri.escapedURI) )
   }
}

dialog(title: 'Groovy executing', id: 'runWaitDialog', modal: true ) {
   vbox(border: emptyBorder(6)) {
       label(text: "Groovy is now executing. Please wait.", alignmentX: 0.5f)
       vstrut()
       button( interruptAction, margin: new Insets(10, 20, 10, 20),
               alignmentX: 0.5f)
    }
}

dialog(title: 'Node Reference (0.6.1)', id: 'nodeReferenceDialog', modal: false, size: [680,480] ) {
   panel {
      borderLayout()
      scrollPane( horizontalScrollBarPolicy: HORIZONTAL_SCROLLBAR_AS_NEEDED,
                  border: emptyBorder(2), constraints: CENTER ) {
         editorPane( id: "reference", border: emptyBorder(2),
                     editable: false, background: WHITE, contentType: "text/html",
                     page: bind { model.currentPage },
                     hyperlinkUpdate: hiperlinkUpdateListener )
      }
      panel( constraints: SOUTH ) {
         glue()
         button( "Dismiss", actionPerformed: { nodeReferenceDialog.hide() } )
      }
   }
}

dialog(title: 'Export as Image', id: 'exportAsImageDialog', modal: true ) {
    panel {
        borderLayout()
        panel( constraints: BL.CENTER ){
            vbox {
                hbox {
                    label( 'File:' )
                    textField( id: 'imageFile', columns: 40 )
                    button( 'Browse...', actionPerformed: {e->
                        def filename = controller.selectFilename('Export as Image')
                        if( filename ) imageFile.text = filename
                    })
                }
                vstrut()
                hbox {
                    label( 'Width:' )
                    textField( id: 'imageWidth', text: "0", columns: 10 )
                    hstrut()
                    label( 'Height:' )
                    textField( id: 'imageHeight', text: "0", columns: 10 )
                }
                vstrut()
                checkBox( id: 'imageBorder', text: 'Add 1 extra pixel to borders', selected: true )
            }
        }
        panel( constraints: BL.SOUTH ) {
            button( 'Cancel', actionPerformed: controller.cancelExportAsImage )
            button( 'Ok', actionPerformed: controller.okExportAsImage )
        }
    }
}

dialog(title: 'Export as SVG', id: 'exportAsSvgDialog', modal: true ) {
    panel {
        borderLayout()
        panel( constraints: BL.CENTER ){
            vbox {
                hbox {
                    label( 'File:' )
                    textField( id: 'svgFile', columns: 40 )
                    button( 'Browse...', actionPerformed: {e->
                        def filename = controller.selectFilename('Export as SVG')
                        if( filename ) svgFile.text = filename
                    })
                }
                vstrut()
                hbox {
                    label( 'Width:' )
                    textField( id: 'svgWidth', text: "0", columns: 10 )
                    hstrut()
                    label( 'Height:' )
                    textField( id: 'svgHeight', text: "0", columns: 10 )
                }
            }
        }
        panel( constraints: BL.SOUTH ) {
            button( 'Cancel', actionPerformed: controller.cancelExportAsSvg )
            button( 'Ok', actionPerformed: controller.okExportAsSvg )
        }
    }
}

dialog(title: 'Import from SVG', id: 'importFromSvgDialog', modal: true ) {
    panel {
        borderLayout()
        panel( constraints: BL.CENTER ){
            vbox {
                hbox {
                    label( 'URL:' )
                    textField( id: 'svgImportFile', columns: 40 )
                    button( 'Browse...', actionPerformed: {e->
                        def filename = controller.selectFilename('Import from SVG') as String
                        if( filename ) svgImportFile.text = new File(filename).toURI().toURL().toString()
                    })
                }
            }
        }
        panel( constraints: BL.SOUTH ) {
            button( 'Cancel', actionPerformed: controller.cancelImportFromSvg )
            button( 'Ok', actionPerformed: controller.okImportFromSvg )
        }
    }
}

def converter = { !it ? 0 : it.toInteger() }

bind(source: imageWidth, sourceProperty: 'text', converter: converter,
     target: model, targetProperty: 'exportAsImageWidth' )
bind(target: model, targetProperty: 'exportAsImageHeight',
     source: imageHeight, sourceProperty: 'text', converter: converter )
bean( model, exportAsImageFile: bind { imageFile.text } )
bean( model, exportAsImageBorder: bind { imageBorder.selected } )

bind(source: svgWidth, sourceProperty: 'text', converter: converter,
     target: model, targetProperty: 'exportAsSvgWidth' )
bind(target: model, targetProperty: 'exportAsSvgHeight',
     source: svgHeight, sourceProperty: 'text', converter: converter )
bean( model, exportAsSvgFile: bind { svgFile.text } )

bean( model, importFromSvgFile: bind { svgImportFile.text } )

Font font = UIManager.getFont("Label.font")
reference.document.styleSheet.addRule( "body { font-family: " + font.family + "; " +
                  "font-size: " + font.size + "pt; }")
