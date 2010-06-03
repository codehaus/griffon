/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the 'License');
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an 'AS IS' BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.lookandfeel.officelnfs

import griffon.lookandfeel.AbstractLookAndFeelTestCase
import static griffon.util.GriffonApplicationUtils.isWindows

/**
 * @author Andres Almiray
 */
class OfficelnfsLookAndFeelTests extends AbstractLookAndFeelTestCase {
    void testOfficelnfsOffice2003LookAndFeel() {
        if(!isWindows) return
        setAndTestLookAndFeel('Officelnfs', 'Office 2003')
    } 

    void testOfficelnfsOfficeXPLookAndFeel() {
        if(!isWindows) return
        setAndTestLookAndFeel('Officelnfs', 'Office XP')
    }

    void testOfficelnfsVisualStudio2005LookAndFeel() {
        if(!isWindows) return
        setAndTestLookAndFeel('Officelnfs', 'VisualStudio 2005')
    }
}
