/*
 * Copyright 2010 the original author or authors.
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

package griffon.lookandfeel.jtattoo

import java.awt.Component
import javax.swing.LookAndFeel
import griffon.lookandfeel.DefaultLookAndFeelProvider
import griffon.lookandfeel.DefaultLookAndFeelInfo

/**
 * @author Andres Almiray
 */
class JTattooLookAndFeelProvider extends DefaultLookAndFeelProvider {
    private static final List<JTattooLookAndFeelInfo> SUPPORTED_LAFS = [
        new JTattooLookAndFeelInfo('Acrylic', new com.jtattoo.plaf.acryl.AcrylLookAndFeel()),
        new JTattooLookAndFeelInfo('Aero', new com.jtattoo.plaf.aero.AeroLookAndFeel()),
        new JTattooLookAndFeelInfo('Aluminium', new com.jtattoo.plaf.aluminium.AluminiumLookAndFeel()),
        new JTattooLookAndFeelInfo('Bernstein', new com.jtattoo.plaf.bernstein.BernsteinLookAndFeel()),
        new JTattooLookAndFeelInfo('Fast', new com.jtattoo.plaf.fast.FastLookAndFeel()),
        new JTattooLookAndFeelInfo('Graphite', new com.jtattoo.plaf.graphite.GraphiteLookAndFeel()),
        new JTattooLookAndFeelInfo('HiFi', new com.jtattoo.plaf.hifi.HiFiLookAndFeel()),
        new JTattooLookAndFeelInfo('Luna', new com.jtattoo.plaf.luna.LunaLookAndFeel()),
        new JTattooLookAndFeelInfo('McWin', new com.jtattoo.plaf.mcwin.McWinLookAndFeel()),
        new JTattooLookAndFeelInfo('Mint', new com.jtattoo.plaf.mint.MintLookAndFeel()),
        new JTattooLookAndFeelInfo('Noire', new com.jtattoo.plaf.noire.NoireLookAndFeel()),
        new JTattooLookAndFeelInfo('Smart', new com.jtattoo.plaf.smart.SmartLookAndFeel())
    ]

    JTattooLookAndFeelProvider() {
        super('JTattoo')
    }
    
    boolean handles(LookAndFeel lookAndFeel) {
        lookAndFeel?.class.name in SUPPORTED_LAFS.lookAndFeel*.getClass()*.getName()
    }

    boolean handles(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo) {
        lookAndFeelInfo instanceof JTattooLookAndFeelInfo
    }

    griffon.lookandfeel.LookAndFeelInfo[] getSupportedLookAndFeels() {
        return SUPPORTED_LAFS as griffon.lookandfeel.LookAndFeelInfo[]
    }

    /**
     * @author Andres Almiray
     */
    private static class JTattooLookAndFeelInfo extends DefaultLookAndFeelInfo {
        JTattooLookAndFeelInfo(String displayName, LookAndFeel lookAndFeel) {
            super('jtattoo-'+displayName.toLowerCase(), displayName, lookAndFeel)
        }
    }
}
