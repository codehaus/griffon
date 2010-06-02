/*
 * Copyright 2010 the original author or authors.
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

import griffon.lookandfeel.LookAndFeelInfo
import griffon.lookandfeel.LookAndFeelManager
import griffon.lookandfeel.LookAndFeelProvider

import java.awt.Toolkit
import java.awt.AWTEvent
import java.awt.event.KeyEvent
import java.awt.event.AWTEventListener
import javax.swing.KeyStroke

import static griffon.util.GriffonApplicationUtils.isMacOSX

/**
 * @author Andres Almiray
 */
class LookandfeelGriffonAddon {
    def addonInit(app) {
        LookAndFeelManager.instance.loadLookAndFeelProviders()

        String provider = app.config.lookandfeel.lookAndFeel ?: 'System'
        String theme = app.config.lookandfeel.theme ?: (isMacOSX? 'System': 'Nimbus')

        LookAndFeelManager.instance.with {
            LookAndFeelProvider lafProvider = getLookAndFeelProvider(provider)
            LookAndFeelInfo lafInfo = getLookAndFeelInfo(lafProvider, theme)
            if(!lafInfo) {
                lafProvider = getLookAndFeelProvider('System')
                lafInfo = getLookAndFeelInfo(lafProvider, (isMacOSX? 'System': ' Nimbus'))
            }
            apply(lafInfo, app)
        }
        app.config.lookandfeel.props.each { key, value ->
            javax.swing.UIManager.put(key, value)
        }

/*
        String keyStrokeText = app.config?.lookandfeel?.keystroke
        if(!keyStrokeText || keyStrokeText.toLowerCase() == 'none') return
        KeyStroke keyStroke = KeyStroke.getKeyStroke(keyStrokeText)

        AWTEventListener listener = null
        listener = { event ->
            if(event.id != KeyEvent.KEY_PRESSED) return
            KeyStroke pressed = KeyStroke.getKeyStrokeForEvent(event)
            if(keyStroke == pressed) {
                execAsync {
                    LookAndFeelManager.instance.showLafDialog(app)
                    Toolkit.defaultToolkit.removeAWTEventListener(listener)
                    Toolkit.defaultToolkit.addAWTEventListener(listener, AWTEvent.KEY_EVENT_MASK)
                }
            }
        } as AWTEventListener
        Toolkit.defaultToolkit.addAWTEventListener(listener, AWTEvent.KEY_EVENT_MASK)
*/
    }

    def mvcGroups = [
        'LookAndFeelSelector': [
            model: 'LookAndFeelSelectorModel',
            view: 'LookAndFeelSelectorView',
            controller: 'LookAndFeelSelectorController'
        ]
    ]
}
