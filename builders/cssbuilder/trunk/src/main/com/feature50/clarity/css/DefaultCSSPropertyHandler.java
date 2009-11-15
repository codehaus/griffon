/*
 * Copyright 2007 Ben Galbraith.
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

package com.feature50.clarity.css;

import java.util.logging.Logger;
import java.util.logging.Level;

import javax.swing.JComponent;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

public class DefaultCSSPropertyHandler implements CSSPropertyHandler {
    private static final Logger logger = Logger.getLogger(DefaultCSSPropertyHandler.class.getName());

    public void processStyles(JComponent[] selected, String propertyName, String propertyValue) {
        for (int i = 0; i < selected.length; i++) {
            JComponent component = selected[i];

            if (propertyName.equalsIgnoreCase("font-family")) {
                String[] fonts = propertyValue.split(",");
                for (int j = 0; j < fonts.length; j++) {
                    String font = fonts[j].trim();
                    String[] strings = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

                    boolean found = false;
                    for (int k = 0; k < strings.length; k++) {
                        if (strings[k].equalsIgnoreCase(font)) {
                            Font f = component.getFont();
                            component.setFont(new Font(strings[k], f.getStyle(), f.getSize()));
                            found = true;
                            break;
                        }
                    }
                    if (found) break;
                }
            } else if (propertyName.equalsIgnoreCase("font-size")) {
                int size = -1;

                // check for an absolute value
                for (int j = 0; j < CSSUtils.FONT_ABSOLUTE_SIZE_NAMES.length; j++) {
                    if (CSSUtils.FONT_ABSOLUTE_SIZE_NAMES[j].equalsIgnoreCase(propertyValue)) {
                        size = CSSUtils.FONT_ABSOLUTE_SIZE_VALUES[j];
                        break;
                    }
                }

                // check for a relative value
                if (size == -1) {
                    if (propertyValue.equals("smaller")) {
                        Container c = component.getParent();
                        int ps = c.getFont().getSize();
                        size = ps--;
                    } else if (propertyValue.equals("larger")) {
                        Container c = component.getParent();
                        int ps = c.getFont().getSize();
                        size = ps++;
                    }
                }

                // check for a percentage
                if (size == -1) {
                    if (propertyValue.endsWith("%")) {
                        Container c = component.getParent();
                        int ps = c.getFont().getSize();
                        String perc = propertyValue.substring(0, propertyValue.length() - 1);

                        int p = 100;
                        try {
                            p = Double.valueOf(perc).intValue();
                        } catch (Exception e) {
                            logger.log(Level.WARNING, String.format("font-size value ('%1$s') was an invalid percentage", propertyValue), e);
                        }

                        float factor = (float) p / (float) 100;
                        size = Math.round((float) ps * (factor));
                    }
                }

                // check for a length; this is a catch-all
                if (size == -1) {
                    String s = propertyValue;
                    if (propertyValue.endsWith("pt")) s = propertyValue.substring(0, propertyValue.length() - 2);

                    try {
                        size = Double.valueOf(s).intValue();
                    } catch (NumberFormatException e) {
                        logger.log(Level.WARNING, String.format("font-size value ('%1$s') not supported; 'pt' is the only supported length and is implicit", propertyValue), e);
                    }
                }

                Font f = component.getFont();
                component.setFont(f.deriveFont((float) size));
            } else if (propertyName.equalsIgnoreCase("font-weight")) {
                int style;
                if (propertyValue.equalsIgnoreCase("normal")) {
                    style = Font.PLAIN;
                } else if (propertyValue.equalsIgnoreCase("bold")) {
                    style = Font.BOLD;
                } else {
                    logger.warning(String.format("font-weight value ('%1$s') unsupported", propertyValue));
                    continue;
                }

                Font f = component.getFont();
                if (f.getStyle() == Font.ITALIC) {
                    if (style == Font.BOLD) {
                        style = Font.ITALIC + Font.BOLD;
                    } else {
                        continue;
                    }
                }
                if ((f.getStyle() == Font.ITALIC) && (style == Font.PLAIN)) continue;    // don't whack the existing italic styling
                component.setFont(f.deriveFont(style));
            } else if (propertyName.equalsIgnoreCase("font-style")) {
                int style;
                if (propertyValue.equalsIgnoreCase("normal")) {
                    style = Font.PLAIN;
                } else if (propertyValue.equalsIgnoreCase("italic")) {
                    style = Font.ITALIC;
                } else {
                    logger.warning(String.format("font-weight value ('%1$s') unsupported", propertyValue));
                    continue;
                }

                Font f = component.getFont();
                if (f.getStyle() == Font.BOLD) {
                    if (style == Font.ITALIC) {
                        style = Font.ITALIC + Font.BOLD;
                    } else {
                        continue;
                    }
                }
                component.setFont(f.deriveFont(style));
            } else if (propertyName.equalsIgnoreCase("color")) {
                Color c = CSSUtils.getColor("color", propertyValue);
                component.setForeground(c);
            } else if (propertyName.equalsIgnoreCase("background-color")) {
                Color c = CSSUtils.getColor("background-color", propertyValue);
                component.setBackground(c);
            }
        }
    }

    public String[] getPropertyNames() {
        return new String[] {
                "font-family",
                "font-size",
                "font-weight",
                "font-style",
                "color",
                "background-color"
        };
    }
}