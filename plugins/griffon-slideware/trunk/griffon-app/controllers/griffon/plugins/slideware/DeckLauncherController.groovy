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

import java.awt.Component
import java.awt.Dimension
import com.lowagie.text.*
import com.lowagie.text.pdf.*

/**
 * @author Andres Almiray
 */
class DeckLauncherController extends AbstractDeckController {
    def model

    void mvcGroupInit(Map<String, Object> args) {
        if(!app.config.presentation) {
            app.config.presentation.fileName = Metadata.current.getApplicationName() + '.pdf'
            app.config.presentation.fullScreen = false
            app.config.presentation.screenWidth = '1024'
            app.config.presentation.screenHeight = '768'
        }
    }

    def configAction = { evt = null ->
        withMVCGroup('DeckConfig') { m, v, c ->
            c.show()
        }
    }

    def playAction = { evt = null ->
        execSync { model.busy = true }
        try {
            def (m, v, c) = createMVCGroup('DeckPlayer')
            app.windowManager.hide('deckLauncherWindow')
            c.show()
        } finally {
            execAsync { model.busy = false }
        }
    }

    def printAction = { evt = null ->
        execSync { model.busy = true }
        def (m, v, c) = createMVCGroup('DeckPlayer')
        
        Map settings = [
            fullScreen: app.config.presentation.fullScreen,
            screenWidth: app.config.presentation.screenWidth,
            screenHeight: app.config.presentation.screenHeight
        ]
        
        try {
            int width = 1024
            int height = 768
            
            v.deck.layout.skipTransitions = true
            app.config.presentation.fullScreen = false
            app.config.presentation.screenWidth = width
            app.config.presentation.screenHeight = height
            c.show()

            Rectangle size = new Rectangle(width, height)
            Document document = new Document(size, 0f, 0f, 0f, 0f)
            PdfWriter.getInstance(document, new FileOutputStream(app.config.presentation.fileName))
            document.open()
            (0..<v.deck.size()).each { i ->
                Slide slide = v.deck[i]
                def imageSet = null
                execSync {imageSet = slide.takeSnapshot() }
                imageSet.each { image ->
                    Image img = Image.getInstance(image, null)
                    img.setDpi(600i, 600i)
                    img.setXYRatio(2.5f)
                    document.add(img)
                }
                execSync { v.deck.layout.next(v.deck) }
            }
            document.close()
        } finally {
            app.config.presentation.fullScreen = settings.fullScreen
            app.config.presentation.screenWidth = settings.screenWidth
            app.config.presentation.screenHeight = settings.screenHeight
            c.hide()
            execAsync { model.busy = false }
        }
    }

    def quitAction = { evt = null -> 
        app.shutdown()    
    }

    def onDeckPlayerClosed = {
        def groupsToDelete = []
        app.groups.each { name, group ->
            if(name != 'DeckLauncher') groupsToDelete << name
        }
        groupsToDelete.each { name ->
            destroyMVCGroup(name)
        }
    }
}
