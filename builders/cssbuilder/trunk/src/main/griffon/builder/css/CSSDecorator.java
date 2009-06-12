/*
 * Copyright 2007-2009 The orginal author or authors.
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


package griffon.builder.css;

import com.feature50.clarity.ClarityConstants;
import com.feature50.clarity.css.CSSPropertyHandlers;
import com.feature50.util.ArrayUtils;
import com.feature50.util.StringUtils;
import com.feature50.util.SwingUtils;
import com.steadystate.css.parser.CSSOMParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
import java.awt.Component;
import java.awt.Container;

import org.w3c.css.sac.InputSource;
import org.w3c.dom.css.CSSStyleSheet;
import org.w3c.dom.css.CSSRuleList;
import org.w3c.dom.css.CSSStyleRule;
import org.w3c.dom.css.CSSStyleDeclaration;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.swing.JComponent;
import javax.swing.JList;


/**
 *
 * @author Ben Galbraith
 * @author Andres Almiray
 */
public class CSSDecorator {
    private static final Logger logger = Logger.getLogger(CSSDecorator.class.getName());

    public static void decorate(String cssName, Container root) {
        decorate(new String[]{cssName}, SwingUtils.getAllJComponents(root), CSSDecorator.class.getClassLoader());
    }

    public static void decorate(String cssName, Container root, ClassLoader classLoader) {
        decorate(new String[]{cssName}, SwingUtils.getAllJComponents(root), classLoader);
    }

    public static void decorate(String cssName, List<JComponent> allComponents) {
        decorate(new String[]{cssName}, allComponents, CSSDecorator.class.getClassLoader());
    }

    public static void decorate(String cssName, List<JComponent> allComponents, ClassLoader classLoader) {
        decorate(new String[]{cssName}, allComponents, classLoader);
    }

    public static void decorate(List<String> cssNames, Container root) {
        decorate(cssNames.toArray(new String[cssNames.size()]), SwingUtils.getAllJComponents(root), CSSDecorator.class.getClassLoader());
    }

    public static void decorate(List<String> cssNames, Container root, ClassLoader classLoader) {
        decorate(cssNames.toArray(new String[cssNames.size()]), SwingUtils.getAllJComponents(root), classLoader);
    }

    public static void decorate(List<String> cssNames, List<JComponent> allComponents) {
        decorate(cssNames.toArray(new String[cssNames.size()]), allComponents, CSSDecorator.class.getClassLoader());
    }

    public static void decorate(List<String> cssNames, List<JComponent> allComponents, ClassLoader classLoader) {
       decorate(cssNames.toArray(new String[cssNames.size()]), allComponents, classLoader);
    }

    public static void decorate(String[] cssNames, Container root) {
        decorate(cssNames, SwingUtils.getAllJComponents(root), CSSDecorator.class.getClassLoader());
    }

    public static void decorate(String[] cssNames, Container root, ClassLoader classLoader) {
        decorate(cssNames, SwingUtils.getAllJComponents(root), classLoader);
    }

    public static void decorate(String[] cssNames, List<JComponent> allComponents) {
        decorate(cssNames, allComponents, CSSDecorator.class.getClassLoader());
    }

    public static void decorate(String[] cssNames, List<JComponent> allComponents, ClassLoader classLoader) {
        if (ArrayUtils.isNullOrEmpty(cssNames)) return;
        if( classLoader == null ) classLoader = CSSDecorator.class.getClassLoader();

        for (int i = 0; i < cssNames.length; i++) {
            String cssName = cssNames[i];
            if (!cssName.endsWith(".css")) cssName = cssName + ".css";
            InputStream in = classLoader.getResourceAsStream(cssName);
            if (in == null) {
                logger.warning(String.format("Stylesheet '%1$s' not found", cssName));
                continue;
            }

            CSSOMParser parser = new CSSOMParser();
            InputSource is = new InputSource(new InputStreamReader(in));
            CSSStyleSheet stylesheet = null;
            try {
                stylesheet = parser.parseStyleSheet(is,null,null);
            } catch (IOException e) {
                logger.log(Level.WARNING, String.format("Couldn't load stylesheet '%1$s'", cssName), e);
                continue;
            }

            CSSRuleList list = stylesheet.getCssRules();
            for (int k = 0; k < list.getLength(); k++) {
                CSSStyleRule rule = (CSSStyleRule) list.item(k);

                String selector = rule.getSelectorText();
                JComponent[] components = SwingUtils.parseSelector(selector, allComponents);

                CSSStyleDeclaration style = rule.getStyle();
                for (int j = 0; j < style.getLength(); j++) {
                    boolean result = CSSPropertyHandlers.getInstance().handle(components, style.item(j), style.getPropertyValue(style.item(j)));
                    if (!result) {
                        logger.warning(String.format("CSS property '%1$s' in selector '%2$s' not supported", style.item(j), selector));
                    }
                }
            }
        }
    }
}