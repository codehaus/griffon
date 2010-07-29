/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import griffon.core.GriffonApplication

import org.jdesktop.swingx.jexplose.JExplose
import org.jdesktop.swingx.jexplose.Explosable
import org.jdesktop.swingx.jexplose.ExplosableDesktop
import org.jdesktop.swingx.jexplose.LayoutStrategy
import org.jdesktop.swingx.jexplose.AnimationStrategy

import javax.swing.JDesktopPane
import javax.swing.KeyStroke
import java.awt.KeyEventDispatcher
import java.awt.KeyboardFocusManager
import java.awt.event.KeyEvent

/**
 * @author Andres Almiray
 */
class JexploseGriffonAddon {
    void addonInit(GriffonApplication app) {
        JExplose.metaClass.setLayoutStrategy = { LayoutStrategy ls ->
            AnimationStrategy anim = JExplose.instance.animationStrategy
            JExplose.instance = new JExplose(ls, anim) 
        }
        JExplose.metaClass.setAnimationStrategy = { AnimationStrategy anim ->
            LayoutStrategy ls = JExplose.instance.layoutStrategy
            JExplose.instance = new JExplose(ls, anim) 
        }
    }

    def factories = [
        explosableDesktop: ExplosableDesktop
    ]

    def methods = [
        explose: { target ->
            JExplose.instance.explose(target)
        },
        registerExploseHotKey: { target, key -> 
            registerHotKey(target, key)
        }
    ]

    private static registerHotKey(target, key) {
        if(key instanceof GString) key = key.toString()
        KeyStroke trigger = null
        if(key instanceof KeyStroke) trigger = key
        else if(key instanceof String) trigger = KeyStroke.getKeyStroke(key)
        else if(key instanceof Number) trigger = KeyStroke.getKeyStroke(key.intValue(), 0i)

        KeyboardFocusManager.currentKeyboardFocusManager.addKeyEventDispatcher(new KeyEventDispatcher() {
            public boolean dispatchKeyEvent(KeyEvent e) {
                KeyStroke k = KeyStroke.getKeyStrokeForEvent(e)
                if(e.getID() == KeyEvent.KEY_RELEASED &&
                   k.keyCode == trigger.keyCode &&
                   k.modifiers == trigger.modifiers) {
                    JExplose.instance.explose(target)
                    return true
                }
                return false
            }
        })
    }
}
