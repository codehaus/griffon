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

package griffon.lookandfeel.looks

import griffon.lookandfeel.AbstractLookAndFeelTestCase

/**
 * @author Andres Almiray
 */
abstract class AbstractLooksLookAndFeelTestCase extends AbstractLookAndFeelTestCase {
    abstract String getLookAndFeelProvider()

    void testBrownSugarTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'BrownSugar')
    }

    void testDarkStarTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'DarkStar')
    }

    void testDesertBlueTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'DesertBlue')
    }

    void testDesertBluerTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'DesertBluer')
    }

    void testDesertGreenTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'DesertGreen')
    }

    void testDesertRedTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'DesertRed')
    }

    void testDesertYellowTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'DesertYellow')
    }

    void testExperienceBlueTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'ExperienceBlue')
    }

    void testExperienceGreenTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'ExperienceGreen')
    }

    void testExperienceRoyaleTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'ExperienceRoyale')
    }

    void testLightGrayTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'LightGray')
    }

    void testSilverTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'Silver')
    }

    void testSkyBlueTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'SkyBlue')
    }

    void testSkyBluerTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'SkyBluer')
    }

    void testSkyGreenTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'SkyGreen')
    }

    void testSkyKruppTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'SkyKrupp')
    }

    void testSkyPinkTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'SkyPink')
    }

    void testSkyRedTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'SkyRed')
    }

    void testSkyYellowTheme() {
        setAndTestLookAndFeel(getLookAndFeelProvider(), 'SkyYellow')
    }
}
