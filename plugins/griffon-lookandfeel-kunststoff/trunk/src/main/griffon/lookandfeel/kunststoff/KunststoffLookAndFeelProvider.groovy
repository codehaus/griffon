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

package griffon.lookandfeel.kunststoff

import java.awt.Component
import java.awt.Window
import javax.swing.UIManager
import javax.swing.LookAndFeel
import javax.swing.SwingUtilities
import griffon.lookandfeel.LookAndFeelProvider
import griffon.core.GriffonApplication

import com.incors.plaf.kunststoff.KunststoffLookAndFeel
import com.incors.plaf.kunststoff.KunststoffTheme
import com.incors.plaf.kunststoff.themes.*

/**
 * @author Andres Almiray
 */
class KunststoffLookAndFeelProvider extends LookAndFeelProvider {
    private static final List<KunststoffLookAndFeelInfo> SUPPORTED_LAFS = [
        new KunststoffLookAndFeelInfo('Desktop', new KunststoffDesktopTheme()),
        new KunststoffLookAndFeelInfo('Notebook', new KunststoffNotebookTheme()),
        new KunststoffLookAndFeelInfo('Presentation', new KunststoffPresentationTheme())
    ]

    KunststoffLookAndFeelProvider() {
        super('Kunststoff')
    }
    
    boolean handles(LookAndFeel lookAndFeel) {
        KunststoffLookAndFeel.class.name == lookAndFeel?.class?.name
    }

    boolean handles(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo) {
        lookAndFeelInfo instanceof KunststoffLookAndFeelInfo
    }

    griffon.lookandfeel.LookAndFeelInfo[] getSupportedLookAndFeels() {
        return SUPPORTED_LAFS as griffon.lookandfeel.LookAndFeelInfo[]
    }

    void preview(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo, Component component) {
        if(!handles(lookAndFeelInfo)) return
        lookAndFeelInfo.preview(component)
    }

    void apply(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo, GriffonApplication application) {
        if(!handles(lookAndFeelInfo)) return
        SwingUtilities.invokeLater {
            lookAndFeelInfo.install()
            for(Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window)
            }
            application.event('LookAndFeelChanged',[UIManager.lookAndFeel])
        }
    }

    /**
     * @author Andres Almiray
     */
    private static class KunststoffLookAndFeelInfo extends griffon.lookandfeel.LookAndFeelInfo {
        private final KunststoffTheme theme

        KunststoffLookAndFeelInfo(String displayName, KunststoffTheme theme) {
            super('kunststoff-'+displayName.toLowerCase(), displayName)
            this.theme = theme
        }

        void install() {
            KunststoffLookAndFeel kunststoffLnF = new KunststoffLookAndFeel()
            kunststoffLnF.setCurrentTheme(theme)
            UIManager.setLookAndFeel(kunststoffLnF)
        }

        void preview(Component component) {
            SwingUtilities.invokeLater {
                install()
                SwingUtilities.updateComponentTreeUI(component)
            }
        }
    
        boolean isCurrentLookAndFeel() {
            LookAndFeel currentLookAndFeel = UIManager.getLookAndFeel()
            if(currentLookAndFeel == null) return false
            if(currentLookAndFeel.class.name != KunststoffLookAndFeel.class.name) return false
            return KunststoffLookAndFeel.getCurrentTheme() == theme 
        }
    }
}
