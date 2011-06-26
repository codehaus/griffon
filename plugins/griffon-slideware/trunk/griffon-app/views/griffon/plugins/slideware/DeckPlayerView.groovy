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

/**
 * @author Andres Almiray
 */

package griffon.plugins.slideware

import griffon.lookandfeel.LookAndFeelManager
import com.bric.image.transition.Transition2D
import com.bric.image.transition.spunk.SwivelTransition2D
import javax.swing.KeyStroke

createFooter = { idx ->
    def footer
    noparent {
        footer = panel {
            migLayout(layoutConstraints: 'fill', columnConstraints: '[right]1%', rowConstraints: '[bottom]')
            label(idx.toString(), cssClass: 'footer')
        }
    }
    footer
}

createHeader = { str ->
    def header
    noparent {
        if(str == null) {
            header = panel()
        } else {
            header = panel {
                migLayout(layoutConstraints: 'fill', columnConstraints: '2%[center]2%', rowConstraints: '[center]')
                label(text: str, cssClass: 'header')
            }
        }
    }
    header
}

try {
    build(getClass().classLoader.loadClass('SlideConfig'))
} catch(ClassNotFoundException cnfe) {
    // ignore
}

actions {
    action(id: 'previousAction',
        closure: {deck.layout.previous(deck)})
    action(id: 'nextAction',
        closure: {deck.layout.next(deck)})
    action(id: 'closeAction',
        closure: controller.hide)
    action(id: 'helpAction',
        closure: controller.helpAction)
    action(id: 'toggleFullScreenAction',
        closure: controller.toggleFullScreenAction)
    action(id: 'lookupAction',
        closure: {
            input.text = ''
            jumpPopup.showPopup(SwingConstants.SOUTH_EAST, deckPlayerWindow)
        })
    action(id: 'jumpAction',
        closure: {
            jumpPopup.hidePopup(true)
            def page = "page${input.text}"
            input.text = ''
            deck.layout.show(deck, page)
        })
    action(id: 'showLafsAction',
        closure: {
            lafsPopup.showPopup(SwingConstants.SOUTH_EAST, deckPlayerWindow)
        })
    action(id: 'lafAction',
        closure: {
            lafsPopup.hidePopup(true)
            if(lafList.selectedValue) LookAndFeelManager.instance.apply(model.lafs[lafList.selectedValue], app)
        })  
}

handleMouseEvent = { evt ->
    if(evt.button == MouseEvent.BUTTON1) {
        deck.layout.next(deck)
    } else if(evt.button == MouseEvent.BUTTON3) {
        deck.layout.previous(deck)
    }
}

jidePopup(id: 'jumpPopup', movable: false, resizable: false, border: emptyBorder(0),
    popupMenuWillBecomeInvisible: {evt -> doLater{deckPlayerWindow.requestFocus()} } ) {
    panel {
        borderLayout()
        textField(id: 'input', columns: 2, border: emptyBorder(0),
            font: new Font("SansSerif", Font.BOLD, 64))
    }
    keyStrokeAction(component: input,
        keyStroke: 'ENTER',
        action: jumpAction)
}
jumpPopup.setDefaultFocusComponent(input)

jidePopup(id: 'lafsPopup', movable: false, resizable: false, border: emptyBorder(0),
    popupMenuWillBecomeInvisible: {evt -> doLater{deckPlayerWindow.requestFocus()} } ) {
    scrollPane {
        list(id: 'lafList', visibleRowCount: 10, border: emptyBorder(0),
            font: new Font('SansSerif', Font.BOLD, 24),
            listData: model.lafs.keySet() as Object[])
    }
    keyStrokeAction(component: lafList,
        keyStroke: 'ENTER',
        action: lafAction)
}
lafsPopup.setDefaultFocusComponent(lafList)

application(id: 'deckPlayerWindow',
    name: 'deckPlayerWindow',
    resizable: false,
    title: GriffonNameUtils.capitalize(app.getMessage('application.title', app.config.application.title)),
    iconImage: imageIcon('/griffon-icon-48x48.png').image,
    iconImages: [imageIcon('/griffon-icon-48x48.png').image,
                 imageIcon('/griffon-icon-32x32.png').image,
                 imageIcon('/griffon-icon-16x16.png').image]) {
    borderLayout()
    panel(id: 'deck', constraints: CENTER) {
        transitionLayout(defaultDuration: 1500L,
                         defaultTransition: new SwivelTransition2D(Transition2D.CLOCKWISE))
        pageNumber = 1
        pageCount = app.artifactManager.slideClasses.size()
    }

    keyStrokeAction(component: deck,
        keyStroke: [shortcut('LEFT'), KeyStroke.getKeyStroke(33, 0)],
        condition: 'in focused window',
        action: previousAction)
    keyStrokeAction(component: deck,
        keyStroke: [shortcut('RIGHT'), KeyStroke.getKeyStroke(34, 0)],
        condition: 'in focused window',
        action: nextAction)
    keyStrokeAction(component: deck,
        keyStroke: 'ESCAPE',
        condition: 'in focused window',
        action: closeAction)
    keyStrokeAction(component: deck,
        keyStroke: shortcut('shift H'),
        condition: 'in focused window',
        action: helpAction)
    keyStrokeAction(component: deck,
        keyStroke: shortcut('UP'),
        condition: 'in focused window',
        action: lookupAction)
    keyStrokeAction(component: deck,
        keyStroke: shortcut("shift K"),
        condition: 'in focused window',
        action: showLafsAction)
    keyStrokeAction(component: deck,
        keyStroke: shortcut("shift F"),
        condition: 'in focused window',
        action: toggleFullScreenAction)

    swingRepaintTimeline(target: deck, loop: true)
}
