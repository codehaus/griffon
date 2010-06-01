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

package griffon.lookandfeel.pagosoft

import java.awt.Component
import java.awt.Window
import javax.swing.UIManager
import javax.swing.LookAndFeel
import javax.swing.SwingUtilities
import griffon.lookandfeel.LookAndFeelProvider
import griffon.core.GriffonApplication

import com.pagosoft.plaf.PlafOptions
import com.pagosoft.plaf.PgsTheme
import com.pagosoft.plaf.themes.ElegantGrayTheme
import com.pagosoft.plaf.themes.JGoodiesThemes
import com.pagosoft.plaf.themes.NativeColorTheme
import com.pagosoft.plaf.themes.SilverTheme
import com.pagosoft.plaf.themes.VistaTheme
import com.pagosoft.plaf.PgsLookAndFeel

/**
 * @author Andres Almiray
 */
class PgsLookAndFeelProvider extends LookAndFeelProvider {
    private static final List<PgsLookAndFeelInfo> SUPPORTED_LAFS = [
        new PgsLookAndFeelInfo('ElegantGray', ElegantGrayTheme.getInstance()),
        new PgsLookAndFeelInfo('JGoodies - BrownSugar', JGoodiesThemes.getBrownSugar()),
        new PgsLookAndFeelInfo('JGoodies - DarkStar', JGoodiesThemes.getDarkStar()),
        new PgsLookAndFeelInfo('JGoodies - DesertBlue', JGoodiesThemes.getDesertBlue()),
        new PgsLookAndFeelInfo('NativeColor', new NativeColorTheme()),
        new PgsLookAndFeelInfo('Silver', new SilverTheme()),
        new PgsLookAndFeelInfo('Vista', new VistaTheme())
    ]

    PgsLookAndFeelProvider() {
        super('Pagosoft')
    }
    
    boolean handles(LookAndFeel lookAndFeel) {
        PgsLookAndFeel.class.name == lookAndFeel?.class?.name
    }

    boolean handles(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo) {
        lookAndFeelInfo instanceof PgsLookAndFeelInfo
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
            PlafOptions.setCurrentTheme(lookAndFeelInfo.theme)
            PlafOptions.setAsLookAndFeel()
            for(Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window)
            }
            application.event('LookAndFeelChanged',[UIManager.lookAndFeel])
        }
    }

    /**
     * @author Andres Almiray
     */
    private static class PgsLookAndFeelInfo extends griffon.lookandfeel.LookAndFeelInfo {
        private final PgsTheme theme

        PgsLookAndFeelInfo(String displayName, PgsTheme theme) {
            super('pagosoft-'+displayName.toLowerCase(), displayName)
            this.theme = theme
        }

        public void preview(Component component) {
            SwingUtilities.invokeLater {
                PlafOptions.setCurrentTheme(theme)
                PlafOptions.setAsLookAndFeel()
                SwingUtilities.updateComponentTreeUI(component)
            }
        }
    
        public boolean isCurrentLookAndFeel() {
            LookAndFeel currentLookAndFeel = UIManager.getLookAndFeel()
            if(currentLookAndFeel == null) return false
            if(currentLookAndFeel.class.name != PgsLookAndFeel.class.name) return false
            return PgsLookAndFeel.getCurrentTheme() == theme 
        }
    }
}
