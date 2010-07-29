/*
 * griffon-lookandfeel-quaqua: Quaqua Look&Feel for Griffon
 * Copyright 2010 and beyond, Andres Almiray
 *
 * SmartGWT is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  SmartGWT is also
 * available under typical commercial license terms - see
 * smartclient.com/license.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

package griffon.lookandfeel.quaqua

import java.awt.Component
import javax.swing.LookAndFeel
import griffon.lookandfeel.DefaultLookAndFeelProvider
import griffon.lookandfeel.DefaultLookAndFeelInfo
import ch.randelshofer.quaqua.QuaquaManager

/**
 * @author Andres Almiray
 */
class QuaquaLookAndFeelProvider extends DefaultLookAndFeelProvider {
    private static final List<QuaquaLookAndFeelInfo> SUPPORTED_LAFS = [
        new QuaquaLookAndFeelInfo()
    ]

    QuaquaLookAndFeelProvider() {
        super('Quaqua')
    }
    
    boolean handles(LookAndFeel lookAndFeel) {
        if(!lookAndFeel) return false
        lookAndFeel.class.name.startsWith('Quaqua')
    }

    boolean handles(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo) {
        lookAndFeelInfo instanceof QuaquaLookAndFeelInfo
    }

    griffon.lookandfeel.LookAndFeelInfo[] getSupportedLookAndFeels() {
        return SUPPORTED_LAFS as griffon.lookandfeel.LookAndFeelInfo[]
    }

    /**
     * @author Andres Almiray
     */
    private static class QuaquaLookAndFeelInfo extends DefaultLookAndFeelInfo {
        QuaquaLookAndFeelInfo() {
            super('quaqua-quaqua', 'Quaqua', QuaquaManager.getLookAndFeel())
        }
    }
}
