/* --------------------------------------------------------------------
   Lookandfeel Lipstiklf
   Copyright (C) 2011 Andres Almiray

   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License as
   published by the Free Software Foundation; either version 2 of the
   License, or (at your option) any later version.

   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this library; if not, see <http://www.gnu.org/licenses/>.
   ---------------------------------------------------------------------
*/

package griffon.lookandfeel.lipstiklf

import java.awt.Component
import javax.swing.LookAndFeel
import griffon.lookandfeel.DefaultLookAndFeelProvider
import griffon.lookandfeel.DefaultLookAndFeelInfo
import com.lipstikLF.LipstikLookAndFeel

/**
 * @author Andres Almiray
 */
class LipstiklfLookAndFeelProvider extends DefaultLookAndFeelProvider {
    private static final List<LipstiklfLookAndFeelInfo> SUPPORTED_LAFS = [
        new LipstiklfLookAndFeelInfo()
    ]

    LipstiklfLookAndFeelProvider() {
        super('Lipstik')
    }
    
    boolean handles(LookAndFeel lookAndFeel) {
        lookAndFeel?.class.name == LipstikLookAndFeel.class.name
    }

    boolean handles(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo) {
        lookAndFeelInfo instanceof LipstiklfLookAndFeelInfo
    }

    griffon.lookandfeel.LookAndFeelInfo[] getSupportedLookAndFeels() {
        return SUPPORTED_LAFS as griffon.lookandfeel.LookAndFeelInfo[]
    }

    /**
     * @author Andres Almiray
     */
    private static class LipstiklfLookAndFeelInfo extends DefaultLookAndFeelInfo {
        LipstiklfLookAndFeelInfo() {
            super('lipsticklf-lipsticklf', 'Lipstik', new LipstikLookAndFeel())
        }
    }
}
