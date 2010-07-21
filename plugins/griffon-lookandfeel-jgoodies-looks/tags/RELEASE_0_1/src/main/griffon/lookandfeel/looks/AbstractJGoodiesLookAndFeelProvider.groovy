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

package griffon.lookandfeel.looks

import java.awt.Component
import java.awt.Window
import javax.swing.UIManager
import javax.swing.LookAndFeel
import javax.swing.SwingUtilities
import griffon.core.GriffonApplication
import griffon.lookandfeel.LookAndFeelProvider

import com.jgoodies.looks.LookUtils
import com.jgoodies.looks.plastic.PlasticLookAndFeel
import com.jgoodies.looks.plastic.theme.*

/**
 * @author Andres Almiray
 */
abstract class AbstractJGoodiesLookAndFeelProvider extends LookAndFeelProvider {
    private final List<JGoodiesLookAndFeelInfo> SUPPORTED_LAFS = []

    protected final LookAndFeel lookAndFeel

    AbstractJGoodiesLookAndFeelProvider(String name, LookAndFeel lookAndFeel) {
        super('JGoodies - ' + name)
        this.lookAndFeel = lookAndFeel
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('BrownSugar', new BrownSugar())
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('DarkStar', new DarkStar())
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('DesertBlue', new DesertBlue())
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('DesertBluer', new DesertBluer())
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('DesertGreen', new DesertGreen())
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('DesertRed', new DesertRed())
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('DesertYellow', new DesertYellow())
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('ExperienceBlue', new ExperienceBlue())
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('ExperienceGreen', new ExperienceGreen())
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('ExperienceRoyale', new ExperienceRoyale())
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('LightGray', new LightGray())
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('Silver', new Silver())
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('SkyBlue', new SkyBlue())
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('SkyBluer', new SkyBluer())
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('SkyGreen', new SkyGreen())
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('SkyKrupp', new SkyKrupp())
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('SkyPink', new SkyPink())
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('SkyRed', new SkyRed())
        SUPPORTED_LAFS << new JGoodiesLookAndFeelInfo('SkyYellow', new SkyYellow())
    }
    
    boolean handles(LookAndFeel lookAndFeel) {
        this.lookAndFeel.class.name == lookAndFeel?.class?.name
    }

    boolean handles(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo) {
        lookAndFeelInfo instanceof JGoodiesLookAndFeelInfo
    }

    griffon.lookandfeel.LookAndFeelInfo[] getSupportedLookAndFeels() {
        return SUPPORTED_LAFS as griffon.lookandfeel.LookAndFeelInfo[]
    }

    void preview(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo, Component component) {
        if(!handles(lookAndFeelInfo)) return
        SwingUtilities.invokeLater {
            lookAndFeelInfo.install()
            SwingUtilities.updateComponentTreeUI(component)
        } 
    }

    void apply(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo, GriffonApplication application) {
        if(!handles(lookAndFeelInfo)) return
        SwingUtilities.invokeLater {
            lookAndFeelInfo.install()
            for(Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window)
            }
        } 
    }

    /**
     * @author Andres Almiray
     */
    public class JGoodiesLookAndFeelInfo extends griffon.lookandfeel.LookAndFeelInfo {
        final lookAndFeelTheme

        JGoodiesLookAndFeelInfo(String displayName, lookAndFeelTheme) {
            super('system-'+displayName.toLowerCase(), displayName)
            this.lookAndFeelTheme = lookAndFeelTheme
        }
       
        void install() {
            PlasticLookAndFeel.setPlasticTheme(lookAndFeelTheme)
            UIManager.setLookAndFeel(lookAndFeel)
        }

        boolean isCurrentLookAndFeel() {
            LookAndFeel laf = UIManager.lookAndFeel
println laf
            if(!(laf instanceof PlasticLookAndFeel)) return false
println "${laf.plasticTheme} $lookAndFeelTheme"
            laf.plasticTheme.class.name == lookAndFeelTheme.class.name
        }
    }
}
