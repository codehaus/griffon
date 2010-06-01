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
    private static final List<JGoodiesLookAndFeelInfo> SUPPORTED_LAFS = [
        new JGoodiesLookAndFeelInfo('BrownSugar', new BrownSugar()),
        new JGoodiesLookAndFeelInfo('DarkStar', new DarkStar()),
        new JGoodiesLookAndFeelInfo('DesertBlue', new DesertBlue()),
        new JGoodiesLookAndFeelInfo('DesertBluer', new DesertBluer()),
        new JGoodiesLookAndFeelInfo('DesertGreen', new DesertGreen()),
        new JGoodiesLookAndFeelInfo('DesertRed', new DesertRed()),
        new JGoodiesLookAndFeelInfo('DesertYellow', new DesertYellow()),
        new JGoodiesLookAndFeelInfo('ExperienceBlue', new ExperienceBlue()),
        new JGoodiesLookAndFeelInfo('ExperienceGreen', new ExperienceGreen()),
        new JGoodiesLookAndFeelInfo('ExperienceRoyale', new ExperienceRoyale()),
        new JGoodiesLookAndFeelInfo('LightGray', new LightGray()),
        new JGoodiesLookAndFeelInfo('Silver', new Silver()),
        new JGoodiesLookAndFeelInfo('SkyBlue', new SkyBlue()),
        new JGoodiesLookAndFeelInfo('SkyBluer', new SkyBluer()),
        new JGoodiesLookAndFeelInfo('SkyGreen', new SkyGreen()),
        new JGoodiesLookAndFeelInfo('SkyKrupp', new SkyKrupp()),
        new JGoodiesLookAndFeelInfo('SkyPink', new SkyPink()),
        new JGoodiesLookAndFeelInfo('SkyRed', new SkyRed()),
        new JGoodiesLookAndFeelInfo('SkyYellow', new SkyYellow())
    ]

    protected final LookAndFeel lookAndFeel

    AbstractJGoodiesLookAndFeelProvider(String name, LookAndFeel lookAndFeel) {
        super('JGoodies - ' + name)
        this.lookAndFeel = lookAndFeel
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
            LookUtils.setLookAndTheme(lookAndFeel, lookAndFeelInfo.lookAndFeelTheme)
            SwingUtilities.updateComponentTreeUI(component)
        } 
    }

    void apply(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo, GriffonApplication application) {
        if(!handles(lookAndFeelInfo)) return
        SwingUtilities.invokeLater {
            LookUtils.setLookAndTheme(lookAndFeel, lookAndFeelInfo.lookAndFeelTheme)
            for(Window window : Window.getWindows()) {
                SwingUtilities.updateComponentTreeUI(window)
            }
        } 
    }

    /**
     * @author Andres Almiray
     */
    public static class JGoodiesLookAndFeelInfo extends griffon.lookandfeel.LookAndFeelInfo {
        final lookAndFeelTheme

        JGoodiesLookAndFeelInfo(String displayName, lookAndFeelTheme) {
            super('system-'+displayName.toLowerCase(), displayName)
            this.lookAndFeelTheme = lookAndFeelTheme
        }

        boolean isCurrentLookAndFeel() {
            LookAndFeel lookAndFeel = UIManager.lookAndFeel
            if(!(lookAndFeel instanceof PlasticLookAndFeel)) return false
            lookAndFeel.plasticTheme == lookAndFeelTheme
        }
    }
}
