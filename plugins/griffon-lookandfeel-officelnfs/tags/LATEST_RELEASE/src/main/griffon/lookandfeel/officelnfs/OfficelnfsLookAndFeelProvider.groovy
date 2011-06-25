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

package griffon.lookandfeel.officelnfs

import java.awt.Component
import javax.swing.LookAndFeel
import griffon.lookandfeel.DefaultLookAndFeelProvider
import griffon.lookandfeel.DefaultLookAndFeelInfo

/**
 * @author Andres Almiray
 */
class OfficelnfsLookAndFeelProvider extends DefaultLookAndFeelProvider {
    private static final List<OfficelnfsLookAndFeelInfo> SUPPORTED_LAFS = [
        new OfficelnfsLookAndFeelInfo('Office 2003', 'org.fife.plaf.Office2003.Office2003LookAndFeel'),
        new OfficelnfsLookAndFeelInfo('Office XP', 'org.fife.plaf.OfficeXP.OfficeXPLookAndFeel'),
        new OfficelnfsLookAndFeelInfo('VisualStudio 2005', 'org.fife.plaf.VisualStudio2005.VisualStudio2005LookAndFeel')
    ]

    OfficelnfsLookAndFeelProvider() {
        super('Officelnfs')
    }
    
    boolean handles(LookAndFeel lookAndFeel) {
        SUPPORTED_LAFS.find {it.lookAndFeel.class.name == lookAndFeel.class.name} ? true : false
    }

    boolean handles(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo) {
        lookAndFeelInfo instanceof OfficelnfsLookAndFeelInfo
    }

    griffon.lookandfeel.LookAndFeelInfo[] getSupportedLookAndFeels() {
        return SUPPORTED_LAFS as griffon.lookandfeel.LookAndFeelInfo[]
    }

    /**
     * @author Andres Almiray
     */
    private static class OfficelnfsLookAndFeelInfo extends DefaultLookAndFeelInfo {
        OfficelnfsLookAndFeelInfo(String displayName, String className) {
            super('officelnfs-'+displayName.toLowerCase(), displayName, className.asType(Class).newInstance())
        }
    }
}
