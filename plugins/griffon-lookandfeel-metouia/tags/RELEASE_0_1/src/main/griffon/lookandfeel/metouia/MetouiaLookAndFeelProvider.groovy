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

package griffon.lookandfeel.metouia

import java.awt.Component
import javax.swing.LookAndFeel
import griffon.lookandfeel.DefaultLookAndFeelProvider
import griffon.lookandfeel.DefaultLookAndFeelInfo
import net.sourceforge.mlf.metouia.MetouiaLookAndFeel

/**
 * @author Andres Almiray
 */
class MetouiaLookAndFeelProvider extends DefaultLookAndFeelProvider {
    private static final List<MetouiaLookAndFeelInfo> SUPPORTED_LAFS = [
        new MetouiaLookAndFeelInfo()
    ]

    MetouiaLookAndFeelProvider() {
        super('Metouia')
    }
    
    boolean handles(LookAndFeel lookAndFeel) {
        lookAndFeel?.class.name == MetouiaLookAndFeel.class.name
    }

    boolean handles(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo) {
        lookAndFeelInfo instanceof MetouiaLookAndFeelInfo
    }

    griffon.lookandfeel.LookAndFeelInfo[] getSupportedLookAndFeels() {
        return SUPPORTED_LAFS as griffon.lookandfeel.LookAndFeelInfo[]
    }

    /**
     * @author Andres Almiray
     */
    private static class MetouiaLookAndFeelInfo extends DefaultLookAndFeelInfo {
        MetouiaLookAndFeelInfo() {
            super('metouia-metouia', 'Metouia', new MetouiaLookAndFeel())
        }
    }
}
