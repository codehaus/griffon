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

package griffon.lookandfeel.a03

import java.awt.Component
import javax.swing.LookAndFeel
import griffon.lookandfeel.DefaultLookAndFeelProvider
import griffon.lookandfeel.DefaultLookAndFeelInfo
import net.sourceforge.a03laf.A03LookAndFeel
import a03.swing.plaf.A03LookAndFeel

/**
 * @author Andres Almiray
 */
class A03LookAndFeelProvider extends DefaultLookAndFeelProvider {
    private static final List<A03LookAndFeelInfo> SUPPORTED_LAFS = [
        new A03LookAndFeelInfo()
    ]

    A03LookAndFeelProvider() {
        super('A03')
    }
    
    boolean handles(LookAndFeel lookAndFeel) {
        lookAndFeel?.class.name == A03LookAndFeel.class.name
    }

    boolean handles(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo) {
        lookAndFeelInfo instanceof A03LookAndFeelInfo
    }

    griffon.lookandfeel.LookAndFeelInfo[] getSupportedLookAndFeels() {
        return SUPPORTED_LAFS as griffon.lookandfeel.LookAndFeelInfo[]
    }

    /**
     * @author Andres Almiray
     */
    private static class A03LookAndFeelInfo extends DefaultLookAndFeelInfo {
        A03LookAndFeelInfo() {
            super('a03-a03', 'A03', new A03LookAndFeel())
        }
    }
}
