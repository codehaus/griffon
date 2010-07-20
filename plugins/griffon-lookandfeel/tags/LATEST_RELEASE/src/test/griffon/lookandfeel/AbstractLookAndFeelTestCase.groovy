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

package griffon.lookandfeel

import java.awt.Component
import java.awt.Window
import javax.swing.UIManager
import javax.swing.LookAndFeel
import javax.swing.SwingUtilities
import griffon.core.GriffonApplication
import griffon.test.AbstractSwingTestCase

/**
 * @author Andres Almiray
 */
abstract class AbstractLookAndFeelTestCase extends AbstractSwingTestCase {
    void setUp() {
        if(headless) return
        LookAndFeelManager.instance.getLookAndFeelProviders()
        execSync {
            resetLookAndFeel()
        }
    }

    void setAndTestLookAndFeel(String provider, String theme) {
        if(headless) return
        execSync {
            setLookAndFeel(provider, theme)
            assertCurrentLookAndFeelIs(provider, theme)
        }
    }

    void assertCurrentLookAndFeelIs(String lookAndFeel, String theme) {
        LookAndFeelProvider provider = LookAndFeelManager.instance.getLookAndFeelProvider(lookAndFeel)
        assert provider
        assertCurrentLookAndFeelIs(LookAndFeelManager.instance.getLookAndFeelInfo(provider, theme))
    }

    void assertCurrentLookAndFeelIs(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo) {
        assert lookAndFeelInfo
        assert lookAndFeelInfo.isCurrentLookAndFeel()
    }

    void setLookAndFeel(String lookAndFeel, String theme) {
        LookAndFeelProvider provider = LookAndFeelManager.instance.getLookAndFeelProvider(lookAndFeel)
        assert provider
        griffon.lookandfeel.LookAndFeelInfo laf = LookAndFeelManager.instance.getLookAndFeelInfo(provider, theme)
        assert laf
        laf.install()
    }

    void resetLookAndFeel() {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())    
    }
}
