/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http:..www.apache.org.licenses.LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.lookandfeel.synthetica

import java.awt.Component
import java.awt.Window
import javax.swing.UIManager
import javax.swing.LookAndFeel
import javax.swing.SwingUtilities
import griffon.lookandfeel.DefaultLookAndFeelProvider
import griffon.lookandfeel.DefaultLookAndFeelInfo
import griffon.lookandfeel.LookAndFeelManager
import griffon.core.GriffonApplication

import de.javasoft.plaf.synthetica.SyntheticaLookAndFeel

/**
 * @author Andres Almiray
 */
class SyntheticaLookAndFeelProvider extends DefaultLookAndFeelProvider {
    private static final List<SyntheticaLookAndFeelInfo> SUPPORTED_LAFS = [
        new SyntheticaLookAndFeelInfo('Synthetica', SyntheticaLookAndFeel.class.name),
        new SyntheticaLookAndFeelInfo('Black Eye', 'de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel'),
        new SyntheticaLookAndFeelInfo('Black Moon', 'de.javasoft.plaf.synthetica.SyntheticaBlackMoonLookAndFeel'),
        new SyntheticaLookAndFeelInfo('Black Star', 'de.javasoft.plaf.synthetica.SyntheticaBlackStarLookAndFeel'),
        new SyntheticaLookAndFeelInfo('Blue Ice', 'de.javasoft.plaf.synthetica.SyntheticaBlueIceLookAndFeel'),
        new SyntheticaLookAndFeelInfo('Blue Moon', 'de.javasoft.plaf.synthetica.SyntheticaBlueMoonLookAndFeel'),
        new SyntheticaLookAndFeelInfo('Blue Steel', 'de.javasoft.plaf.synthetica.SyntheticaBlueSteelLookAndFeel'),
        new SyntheticaLookAndFeelInfo('Classy', 'de.javasoft.plaf.synthetica.SyntheticaClassyLookAndFeel'),
        new SyntheticaLookAndFeelInfo('Green Dream', 'de.javasoft.plaf.synthetica.SyntheticaGreenDreamLookAndFeel'),
        new SyntheticaLookAndFeelInfo('Mauve Metallic', 'de.javasoft.plaf.synthetica.SyntheticaMauveMetallicLookAndFeel'),
        new SyntheticaLookAndFeelInfo('Orange Metallic', 'de.javasoft.plaf.synthetica.SyntheticaOrangeMetallicLookAndFeel'),
        new SyntheticaLookAndFeelInfo('Silver Moon', 'de.javasoft.plaf.synthetica.SyntheticaSilverMoonLookAndFeel'),
        new SyntheticaLookAndFeelInfo('Simple2D', 'de.javasoft.plaf.synthetica.SyntheticaSimple2DLookAndFeel'),
        new SyntheticaLookAndFeelInfo('Sky Metallic', 'de.javasoft.plaf.synthetica.SyntheticaSkyMetallicLookAndFeel')
    ]

    SyntheticaLookAndFeelProvider() {
        super('Synthetica')
    }
    
    boolean handles(LookAndFeel lookAndFeel) {
        lookAndFeel?.class.name in SUPPORTED_LAFS.lookAndFeelClassName
    }

    boolean handles(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo) {
        lookAndFeelInfo instanceof SyntheticaLookAndFeelInfo
    }

    griffon.lookandfeel.LookAndFeelInfo[] getSupportedLookAndFeels() {
        return SUPPORTED_LAFS as griffon.lookandfeel.LookAndFeelInfo[]
    }

    void apply(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo, GriffonApplication application) {
        if(!handles(lookAndFeelInfo)) return
        SwingUtilities.invokeLater {
            lookAndFeelInfo.install()
            for(Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window)
            }
            application.event('LookAndFeelChanged', [
                LookAndFeelManager.instance.getCurrentLookAndFeelProvider(),
                LookAndFeelManager.instance.getCurrentLookAndFeelInfo(),
                UIManager.lookAndFeel])
        }
    }

    /**
     * @author Andres Almiray
     */
    private static class SyntheticaLookAndFeelInfo extends griffon.lookandfeel.LookAndFeelInfo {
        final String lookAndFeelClassName

        SyntheticaLookAndFeelInfo(String displayName, String lookAndFeelClassName) {
            super('synthetica-'+displayName.toLowerCase(), displayName)
            this.lookAndFeelClassName = lookAndFeelClassName
        }

        public void install() {
            try {
                SyntheticaLookAndFeel.setLookAndFeel(lookAndFeelClassName)
            } catch(Exception e) {
                // ignore
            }
        }
    
        public void preview(Component component) {
            install()
            SwingUtilities.updateComponentTreeUI(component)
        }
    
        public boolean isCurrentLookAndFeel() {
            LookAndFeel currentLookAndFeel = UIManager.getLookAndFeel()
            if(currentLookAndFeel == null) return false
            lookAndFeelClassName == currentLookAndFeel.class.name
        }
    }
}
