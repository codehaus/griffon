/*
 * $Id:  $
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
 *
 */

package groovy.swing.factory

import org.jdesktop.swingx.decorator.HighlighterFactory

public class HighlighterFactoryHelper extends AbstractFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map properties) throws InstantiationException, IllegalAccessException {
        def highlighter
        if (name.equals("altStripingHighlighter")) {

            def baseBackground = properties.remove("baseBackground")
            def alternateBackground = properties.remove("alternateBackground")
            def linesPerStripe = properties.remove("linesPerStripe")

            if (baseBackground != null && alternateBackground != null && linesPerStripe != null)
                highlighter = HighlighterFactory.createAlternateStriping(baseBackground, alternateBackground, linesPerStripe)
            else if (baseBackground == null && alternateBackground == null && linesPerStripe == null)
                highlighter = HighlighterFactory.createAlternateStriping()
            else {
                if (baseBackground != null && alternateBackground != null)
                    highlighter = HighlighterFactory.createAlternateStriping(baseBackground, alternateBackground)
                else highlighter = HighlighterFactory.createAlternateStriping(linesPerStripe)
            }
        }
        else if (name.equals("simpleStripingHighlighter")) {
            def rowsPerGroup = properties.remove("rowsPerGroup")
            def stripeBackground = properties.remove("stripeBackground")
            if (rowsPerGroup != null && stripeBackground != null)
                highlighter = HighlighterFactory.createSimpleStriping(stripeBackground, rowsPerGroup)
            else if (rowsPerGroup == null & stripeBackground == null)
                highlighter = HighlighterFactory.createSimpleStriping()
            else {
                if (stripeBackground != null)
                    highlighter = HighlighterFactory.createSimpleStriping(stripeBackground)
                else highlighter = HighlighterFactory.createSimpleStriping(rowsPerGroup)
            }
        }
        return highlighter
    }
}
