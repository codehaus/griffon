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

import java.awt.Toolkit
import java.awt.Dimension
import griffon.builder.css.CSSDecorator
import griffon.builder.css.CSSBindings
import static griffon.swing.SwingUtils.centerOnScreen

import griffon.transform.Threading

/**
 * @author Andres Almiray
 */
class DeckPlayerController extends AbstractDeckController {
    DeckPlayerView view
    def builder

    void mvcGroupInit(Map<String, Object> args) {
        CSSBindings.instance.initDefaults()
        Map pages = [:]
        app.artifactManager.slideClasses.each { slideClass ->
            int index = ((slideClass.shortName =~ /Page(\d+)Slide/)[0][1]).toInteger()
            pages[index] = slideClass
        }
        
        pages.keySet().sort().each { index ->
            GriffonClass slideClass = pages[index]
            Script slideInstance = slideClass.newInstance()
            slideInstance.binding = view.binding
            slideInstance.builder = view.builder
            def slide = view.builder.build(slideInstance)
            builder.container(view.deck) {
                widget(slide, constraints: [name: 'page' + view.pageNumber,
                    transition: slide.transition],
                    mouseClicked: view.handleMouseEvent,
                    header: slide.header ?: view.createHeader(slide.title),
                    footer: slide.footer ?: view.createFooter(view.pageNumber))
            }
            view.pageNumber++
        }
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    def show = { evt = null ->
        if(app.config.presentation.fullScreen) {
            makeFullScreen()
        } else {
            makeNormalSize()
        }
        view.deck.layout.first(view.deck)
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    def toggleFullScreenAction = { evt = null ->
        app.config.presentation.fullScreen = !app.config.presentation.fullScreen
        if(app.config.presentation.fullScreen) {
            makeFullScreen()
        } else {
            makeNormalSize()
        }
    }

    private void makeFullScreen() {
        Dimension screen = Toolkit.defaultToolkit.screenSize
        resize(screen.width, screen.height, true)
    }

    private void makeNormalSize() {
        resize(app.config.presentation.screenWidth, app.config.presentation.screenHeight, false)
    }

    private void resize(width, height, boolean undecorated) {
        CSSBindings.instance.screenWidth = width
        CSSBindings.instance.screenHeight = height

        app.windowManager.hide('deckPlayerWindow')
        view.deckPlayerWindow.undecorated = undecorated
        view.deckPlayerWindow.preferredSize = [
            width as int,
            height as int
        ]
        view.deckPlayerWindow.pack()
        CSSDecorator.decorate('style', view.deckPlayerWindow)
        centerOnScreen(view.deckPlayerWindow) 
        app.windowManager.show('deckPlayerWindow') 
    }

    @Threading(Threading.Policy.INSIDE_UITHREAD_SYNC)
    def hide = { evt = null ->
        app.windowManager.hide('deckPlayerWindow')
        app.windowManager.show('deckLauncherWindow')
        app.eventAsync('DeckPlayerClosed')
    }
}
