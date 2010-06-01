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

package griffon.lookandfeel.substance

import java.awt.Component
import javax.swing.LookAndFeel
import griffon.lookandfeel.DefaultLookAndFeelProvider
import griffon.lookandfeel.DefaultLookAndFeelInfo
import org.pushingpixels.substance.api.SubstanceLookAndFeel
import org.pushingpixels.substance.api.skin.*

/**
 * @author Andres Almiray
 */
class SubstanceLookAndFeelProvider extends DefaultLookAndFeelProvider {
    private static final List<SubstanceLookAndFeelInfo> SUPPORTED_LAFS = [
        new SubstanceLookAndFeelInfo('Autum', new SubstanceAutumnLookAndFeel()),
        new SubstanceLookAndFeelInfo('BusinessBlackSteel', new SubstanceBusinessBlackSteelLookAndFeel()),
        new SubstanceLookAndFeelInfo('BusinessBlueSteel', new SubstanceBusinessBlueSteelLookAndFeel()),
        new SubstanceLookAndFeelInfo('Business', new SubstanceBusinessLookAndFeel()),
        new SubstanceLookAndFeelInfo('ChallengerDeep', new SubstanceChallengerDeepLookAndFeel()),
        new SubstanceLookAndFeelInfo('CremeCoffee', new SubstanceCremeCoffeeLookAndFeel()),
        new SubstanceLookAndFeelInfo('Creme', new SubstanceCremeLookAndFeel()),
        new SubstanceLookAndFeelInfo('DustCoffee', new SubstanceDustCoffeeLookAndFeel()),
        new SubstanceLookAndFeelInfo('DustLook', new SubstanceDustLookAndFeel()),
        new SubstanceLookAndFeelInfo('EmeraldDusk', new SubstanceEmeraldDuskLookAndFeel()),
        new SubstanceLookAndFeelInfo('Gemini', new SubstanceGeminiLookAndFeel()),
        new SubstanceLookAndFeelInfo('GraphiteAqua', new SubstanceGraphiteAquaLookAndFeel()),
        new SubstanceLookAndFeelInfo('GraphiteGlass', new SubstanceGraphiteGlassLookAndFeel()),
        new SubstanceLookAndFeelInfo('Graphite', new SubstanceGraphiteLookAndFeel()),
        new SubstanceLookAndFeelInfo('Magellan', new SubstanceMagellanLookAndFeel()),
        new SubstanceLookAndFeelInfo('MistAqua', new SubstanceMistAquaLookAndFeel()),
        new SubstanceLookAndFeelInfo('MistSilver', new SubstanceMistSilverLookAndFeel()),
        new SubstanceLookAndFeelInfo('Moderate', new SubstanceModerateLookAndFeel()),
        new SubstanceLookAndFeelInfo('NebulaBrickWall', new SubstanceNebulaBrickWallLookAndFeel()),
        new SubstanceLookAndFeelInfo('Nebula', new SubstanceNebulaLookAndFeel()),
        new SubstanceLookAndFeelInfo('OfficeBlue2007', new SubstanceOfficeBlue2007LookAndFeel()),
        new SubstanceLookAndFeelInfo('OfficeSilver2007', new SubstanceOfficeSilver2007LookAndFeel()),
        new SubstanceLookAndFeelInfo('Raven', new SubstanceRavenLookAndFeel()),
        new SubstanceLookAndFeelInfo('Sahara', new SubstanceSaharaLookAndFeel()),
        new SubstanceLookAndFeelInfo('Twilight', new SubstanceTwilightLookAndFeel())
    ]

    SubstanceLookAndFeelProvider() {
        super('Substance')
    }
    
    boolean handles(LookAndFeel lookAndFeel) {
        lookAndFeel?.class.name in SUPPORTED_LAFS.lookAndFeel.class.name
    }

    boolean handles(griffon.lookandfeel.LookAndFeelInfo lookAndFeelInfo) {
        lookAndFeelInfo instanceof SubstanceLookAndFeelInfo
    }

    griffon.lookandfeel.LookAndFeelInfo[] getSupportedLookAndFeels() {
        return SUPPORTED_LAFS as griffon.lookandfeel.LookAndFeelInfo[]
    }

    /**
     * @author Andres Almiray
     */
    private static class SubstanceLookAndFeelInfo extends DefaultLookAndFeelInfo {
        SubstanceLookAndFeelInfo(String displayName, LookAndFeel lookAndFeel) {
            super('substance-'+displayName.toLowerCase(), displayName, lookAndFeel)
        }
    }
}
