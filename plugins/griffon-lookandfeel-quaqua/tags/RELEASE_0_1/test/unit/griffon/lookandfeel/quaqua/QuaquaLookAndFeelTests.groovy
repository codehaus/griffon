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

import griffon.lookandfeel.AbstractLookAndFeelTestCase

/**
 * @author Andres Almiray
 */
class QuaquaLookAndFeelTests extends AbstractLookAndFeelTestCase {
    void testQuaquaQuaquaLookAndFeel() {
        setAndTestLookAndFeel('Quaqua', 'Quaqua')
    }
}
