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

package griffon.lookandfeel.system

import java.awt.Component
import javax.swing.UIManager
import javax.swing.LookAndFeel
import groovy.swing.LookAndFeelHelper
import griffon.lookandfeel.DefaultLookAndFeelProvider

import static griffon.util.GriffonApplicationUtils.isWindows

/**
 * @author Andres Almiray
 */
class SystemLookAndFeelProvider extends DefaultLookAndFeelProvider {
    private static final List<SystemLookAndFeelInfo> SUPPORTED_LAFS = [
        new SystemLookAndFeelInfo('Nimbus', LookAndFeelHelper.getNimbusLAFName()),
        new SystemLookAndFeelInfo('Metal', 'javax.swing.plaf.metal.MetalLookAndFeel'),
        new SystemLookAndFeelInfo('Motif', 'com.sun.java.swing.plaf.motif.MotifLookAndFeel'),
        new SystemLookAndFeelInfo('System', UIManager.getSystemLookAndFeelClassName()),
        new SystemLookAndFeelInfo('CrossPlatform', UIManager.getCrossPlatformLookAndFeelClassName())
    ]

    static {
        if(isWindows) {
            SUPPORTED_LAFS << new SystemLookAndFeelInfo('Windows', 'com.sun.java.swing.plaf.windows.WindowsLookAndFeel')
            SUPPORTED_LAFS << new SystemLookAndFeelInfo('Windows Classic', 'com.sun.java.swing.plaf.windows.WindowsClassicLookAndFeel')
        }
    }

    SystemLookAndFeelProvider() {
        super('System')
    }
    
    boolean handles(LookAndFeel lookAndFeel) {
        SUPPORTED_LAFS.find {it.lookAndFeel.class.name == lookAndFeel.class.name} ? true : false
    }

    boolean handles(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo) {
        lookAndFeelInfo instanceof SystemLookAndFeelInfo
    }

    griffon.lookandfeel.LookAndFeelInfo[] getSupportedLookAndFeels() {
        return SUPPORTED_LAFS as griffon.lookandfeel.LookAndFeelInfo[]
    }
}
