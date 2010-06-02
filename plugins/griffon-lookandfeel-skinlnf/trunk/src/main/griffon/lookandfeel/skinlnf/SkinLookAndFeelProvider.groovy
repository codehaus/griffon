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

package griffon.lookandfeel.skinlnf

import java.awt.Component
import java.awt.Window
import javax.swing.UIManager
import javax.swing.LookAndFeel
import javax.swing.SwingUtilities
import griffon.lookandfeel.LookAndFeelProvider
import griffon.core.GriffonApplication

import com.l2fprod.gui.plaf.skin.Skin
import com.l2fprod.gui.plaf.skin.SkinLookAndFeel

/**
 * @author Andres Almiray
 */
class SkinLookAndFeelProvider extends LookAndFeelProvider {
    private static final List<SkinLookAndFeelInfo> SUPPORTED_LAFS = [
        new SkinLookAndFeelInfo('BeOS', loadSkin('BeOSthemepack')),
        new SkinLookAndFeelInfo('Amarach', loadSkin('amarachthemepack')),
        new SkinLookAndFeelInfo('Architect Blue', loadSkin('architectBluethemepack')),
        new SkinLookAndFeelInfo('Architect Olive', loadSkin('architectOlivethemepack')),
        new SkinLookAndFeelInfo('b0sumi Ergo', loadSkin('b0sumiErgothemepack')),
        new SkinLookAndFeelInfo('b0sumi', loadSkin('b0sumithemepack')),
        new SkinLookAndFeelInfo('Blue Metal', loadSkin('blueMetalthemepack')),
        new SkinLookAndFeelInfo('Blue Turquesa', loadSkin('blueTurquesathemepack')),
        new SkinLookAndFeelInfo('ChaNinja Blue', loadSkin('chaNinja-Bluethemepack')),
        new SkinLookAndFeelInfo('CoronaH', loadSkin('coronaHthemepack')),
        new SkinLookAndFeelInfo('Cougar', loadSkin('cougarthemepack')),
        new SkinLookAndFeelInfo('Crystal2', loadSkin('crystal2themepack')),
        new SkinLookAndFeelInfo('Default', loadSkin('default-themepack')),
        new SkinLookAndFeelInfo('FatalE', loadSkin('fatalEthemepack')),
        new SkinLookAndFeelInfo('Gfx Oasis', loadSkin('gfxOasisthemepack')),
        new SkinLookAndFeelInfo('Gorilla', loadSkin('gorillathemepack')),
        new SkinLookAndFeelInfo('Hmm XP Blue', loadSkin('hmmXPBluethemepack')),
        new SkinLookAndFeelInfo('Hmm XP Mono Blue', loadSkin('hmmXPMonoBluethemepack')),
        new SkinLookAndFeelInfo('iBar', loadSkin('iBarthemepack')),
        new SkinLookAndFeelInfo('Midnight', loadSkin('midnightthemepack')),
        new SkinLookAndFeelInfo('MakkiX and MagraX', loadSkin('mmMagra-Xthemepack')),
        new SkinLookAndFeelInfo('Olive Green Luna XP', loadSkin('oliveGreenLunaXPthemepack')),
        new SkinLookAndFeelInfo('Opus Luna Silver', loadSkin('opusLunaSilverthemepack')),
        new SkinLookAndFeelInfo('Opus OS Blue', loadSkin('opusOSBluethemepack')),
        new SkinLookAndFeelInfo('Opus OS Deep', loadSkin('opusOSDeepthemepack')),
        new SkinLookAndFeelInfo('Opus OS Olive', loadSkin('opusOSOlivethemepack')),
        new SkinLookAndFeelInfo('QuickSilverR', loadSkin('quickSilverRthemepack')),
        new SkinLookAndFeelInfo('Roue Blue', loadSkin('roueBluethemepack')),
        new SkinLookAndFeelInfo('Roue Brown', loadSkin('roueBrownthemepack')),
        new SkinLookAndFeelInfo('Roue Green', loadSkin('roueGreenthemepack')),
        new SkinLookAndFeelInfo('Royal Inspirat', loadSkin('royalInspiratthemepack')),
        new SkinLookAndFeelInfo('Silver Luna XP', loadSkin('silverLunaXPthemepack')),
        new SkinLookAndFeelInfo('SolunaR', loadSkin('solunaRthemepack')),
        new SkinLookAndFeelInfo('Tiger Graphite', loadSkin('tigerGraphitethemepack')),
        new SkinLookAndFeelInfo('Tiger', loadSkin('tigerthemepack')),
        new SkinLookAndFeelInfo('Underling', loadSkin('underlingthemepack'))
    ]

    private static Skin loadSkin(String name) {
        URL url = SkinLookAndFeelProvider.class.getResource("/skinlnf/themepacks/${name}.zip")
        SkinLookAndFeel.loadThemePack(url)
    }

    SkinLookAndFeelProvider() {
        super('Skin')
    }
    
    boolean handles(LookAndFeel lookAndFeel) {
        SkinLookAndFeel.class.name == lookAndFeel?.class?.name
    }

    boolean handles(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo) {
        lookAndFeelInfo instanceof SkinLookAndFeelInfo
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
            SkinLookAndFeel.setSkin(lookAndFeelInfo.skin)
            UIManager.setLookAndFeel(new SkinLookAndFeel())
            for(Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window)
            }
            application.event('LookAndFeelChanged',[UIManager.lookAndFeel])
        }
    }

    /**
     * @author Andres Almiray
     */
    private static class SkinLookAndFeelInfo extends griffon.lookandfeel.LookAndFeelInfo {
        private final Skin skin

        SkinLookAndFeelInfo(String displayName, Skin skin) {
            super('skinlnf-'+displayName.toLowerCase(), displayName)
            this.skin = skin
        }

        public void preview(Component component) {
            SwingUtilities.invokeLater {
                SkinLookAndFeel.setSkin(skin)
                UIManager.setLookAndFeel(new SkinLookAndFeel())
                SwingUtilities.updateComponentTreeUI(component)
            }
        }
    
        public boolean isCurrentLookAndFeel() {
            LookAndFeel currentLookAndFeel = UIManager.getLookAndFeel()
            if(currentLookAndFeel == null) return false
            if(currentLookAndFeel.class.name != SkinLookAndFeel.class.name) return false
            return SkinLookAndFeel.skin == skin 
        }
    }
}
