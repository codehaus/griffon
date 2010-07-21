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

package griffon.lookandfeel.nimrod

import java.awt.Component
import javax.swing.LookAndFeel
import griffon.lookandfeel.DefaultLookAndFeelProvider
import griffon.lookandfeel.DefaultLookAndFeelInfo
import com.nilo.plaf.nimrod.NimRODLookAndFeel

/**
 * @author Andres Almiray
 */
class NimrodLookAndFeelProvider extends DefaultLookAndFeelProvider {
    private static final List<NimrodLookAndFeelInfo> SUPPORTED_LAFS = [
        new NimrodLookAndFeelInfo()
    ]

    NimrodLookAndFeelProvider() {
        super('Nimrod')
    }
    
    boolean handles(LookAndFeel lookAndFeel) {
        lookAndFeel?.class.name == NimRODLookAndFeel.class.name
    }

    boolean handles(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo) {
        lookAndFeelInfo instanceof NimrodLookAndFeelInfo
    }

    griffon.lookandfeel.LookAndFeelInfo[] getSupportedLookAndFeels() {
        return SUPPORTED_LAFS as griffon.lookandfeel.LookAndFeelInfo[]
    }

    /**
     * @author Andres Almiray
     */
    private static class NimrodLookAndFeelInfo extends DefaultLookAndFeelInfo {
        NimrodLookAndFeelInfo() {
            super('nimrod-nimrod', 'NimROD', new NimRODLookAndFeel())
        }
    }
}
