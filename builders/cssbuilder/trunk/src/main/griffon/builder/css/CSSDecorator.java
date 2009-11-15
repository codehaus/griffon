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
import java.io.StringReader;
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

import groovy.lang.Binding;
import groovy.lang.GroovyShell;

/**
 *
 * @author Ben Galbraith
 * @author Andres Almiray
 */
public class CSSDecorator {
    private static final Logger logger = Logger.getLogger(CSSDecorator.class.getName());
    private static final GroovyShell shell = new GroovyShell(CSSBindings.getInstance());

    public static void applyStyle(String style, Container root) {
        try {
            applyStylesheet(new InputSource(new StringReader(style)), SwingUtils.getAllJComponents(root));
        } catch (Exception e) {
            logger.log(Level.WARNING, String.format("Couldn't apply stylesheet '%1$s'", style), e);
        }
    }

    public static void applyStyle(String style, List<JComponent> allComponents) {
        try {
            applyStylesheet(new InputSource(new StringReader(style)), allComponents);
        } catch (Exception e) {
            logger.log(Level.WARNING, String.format("Couldn't apply stylesheet '%1$s'", style), e);
        }
    }

    public static void decorate(String cssName, Container root) {
        decorate(new String[]{cssName}, SwingUtils.getAllJComponents(root), null);
    }

    public static void decorate(String cssName, Container root, ClassLoader classLoader) {
        decorate(new String[]{cssName}, SwingUtils.getAllJComponents(root), classLoader);
    }

    public static void decorate(String cssName, List<JComponent> allComponents) {
        decorate(new String[]{cssName}, allComponents, null);
    }

    public static void decorate(String cssName, List<JComponent> allComponents, ClassLoader classLoader) {
        decorate(new String[]{cssName}, allComponents, classLoader);
    }

    public static void decorate(List<String> cssNames, Container root) {
        decorate(cssNames.toArray(new String[cssNames.size()]), SwingUtils.getAllJComponents(root), null);
    }

    public static void decorate(List<String> cssNames, Container root, ClassLoader classLoader) {
        decorate(cssNames.toArray(new String[cssNames.size()]), SwingUtils.getAllJComponents(root), classLoader);
    }

    public static void decorate(List<String> cssNames, List<JComponent> allComponents) {
        decorate(cssNames.toArray(new String[cssNames.size()]), allComponents, null);
    }

    public static void decorate(List<String> cssNames, List<JComponent> allComponents, ClassLoader classLoader) {
       decorate(cssNames.toArray(new String[cssNames.size()]), allComponents, classLoader);
    }

    public static void decorate(String[] cssNames, Container root) {
        decorate(cssNames, SwingUtils.getAllJComponents(root), null);
    }

    public static void decorate(String[] cssNames, Container root, ClassLoader classLoader) {
        decorate(cssNames, SwingUtils.getAllJComponents(root), classLoader);
    }

    public static void decorate(String[] cssNames, List<JComponent> allComponents) {
        decorate(cssNames, allComponents, null);
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
            try {
                applyStylesheet( new InputSource(new InputStreamReader(in)), allComponents);
            } catch(Exception e) {
                logger.log(Level.WARNING, String.format("Couldn't load stylesheet '%1$s'", cssName), e);
                continue;
            }
        }
    }

    private static void applyStylesheet(InputSource is, List<JComponent> allComponents) throws IOException {
        CSSOMParser parser = new CSSOMParser();
        CSSStyleSheet stylesheet = stylesheet = parser.parseStyleSheet(is,null,null);

        CSSRuleList list = stylesheet.getCssRules();
        for (int k = 0; k < list.getLength(); k++) {
            CSSStyleRule rule = (CSSStyleRule) list.item(k);

            String selector = rule.getSelectorText();
            JComponent[] components = SwingUtils.parseSelector(selector, allComponents);

            CSSStyleDeclaration style = rule.getStyle();
            for (int j = 0; j < style.getLength(); j++) {
                String value = style.getPropertyValue(style.item(j));
                if(value != null && value.contains("$")) {
                    Object output = shell.evaluate(value);
                    if(output != null) value = String.valueOf(output);
                    if(value.startsWith("\"")) value = value.substring(1);
                    if(value.endsWith("\"")) value = value.substring(0, value.length() - 2);
                }
                boolean result = CSSPropertyHandlers.getInstance().handle(components, style.item(j), value);
                if (!result) {
                    logger.warning(String.format("CSS property '%1$s' in selector '%2$s' not supported", style.item(j), selector));
                }
            }
        }
    }
}