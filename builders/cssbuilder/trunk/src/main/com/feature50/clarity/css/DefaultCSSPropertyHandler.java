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
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

public class DefaultCSSPropertyHandler implements CSSPropertyHandler {
    private static final Logger logger = Logger.getLogger(DefaultCSSPropertyHandler.class.getName());

    private static final String[] FONT_ABSOLUTE_SIZE_NAMES = { "xx-small", "x-small", "small", "medium", "large", "x-large", "xx-large" };
    private static final int[] FONT_ABSOLUTE_SIZE_VALUES =   { 7, 8, 9, 10, 11, 12, 13 };

    /*
    private static final String[] COLOR_NAMES = { "maroon", "red", "orange", "yellow", "olive",
                                                  "purple", "fuschia", "white", "lime", "green",
                                                  "navy", "blue", "aqua", "teal", "black",
                                                  "silver", "gray"
    };
    private static final String[] COLOR_VALUES = { "#800000", "#ff0000", "#ffA500", "#ffff00", "#808000",
                                                  "#800080", "#ff00ff", "#ffffff", "#00ff00", "#008000",
                                                  "#000080", "#0000ff", "#00ffff", "#008080", "#000000",
                                                  "#c0c0c0", "#808080"
    };
    */
    private static final String[] COLOR_NAMES = {"aliceBlue", "antiqueWhite", "aqua", "aquamarine", "azure", "bakersChocolate",
                                                 "beige", "bisque", "black", "blanchedAlmond", "blue", "blueViolet",
                                                 "brass", "brightGold", "bronze", "brown", "burlyWood", "cadetBlue",
                                                 "chartreuse", "chocolate", "coolCopper", "copper", "coral", "cornflowerBlue",
                                                 "cornsilk", "crimson", "cyan", "darkBlue", "darkBrown", "darkCyan",
                                                 "darkGoldenRod", "darkGray", "darkGreen", "darkGreenCopper", "darkKhaki", "darkMagenta",
                                                 "darkOliveGreen", "darkOrange", "darkOrchid", "darkPurple", "darkRed", "darkSalmon",
                                                 "darkSeaGreen", "darkSlateBlue", "darkSlateGray", "darkTan", "darkTurquoise", "darkViolet",
                                                 "darkWood", "deepPink", "deepSkyBlue", "dimGray", "dodgerBlue", "dustyRose",
                                                 "fadedBrown", "feldspar", "fireBrick", "floralWhite", "forestGreen", "fuchsia",
                                                 "gainsboro", "ghostWhite", "gold", "goldenRod", "gray", "green",
                                                 "greenCopper", "greenYellow", "honeyDew", "hotPink", "hunterGreen", "indianRed",
                                                 "indigo", "ivory", "khaki", "lavender", "lavenderBlush", "lawnGreen",
                                                 "lemonChiffon", "lightBlue", "lightCoral", "lightCyan", "lightGoldenRodYellow", "lightGray",
                                                 "lightGreen", "lightPink", "lightSalmon", "lightSeaGreen", "lightSkyBlue", "lightSlateBlue",
                                                 "lightSlateGray", "lightSteelBlue", "lightWood", "lightYellow", "lime", "limeGreen",
                                                 "linen", "magenta", "mandarinOrange", "maroon", "mediumAquaMarine", "mediumBlue",
                                                 "mediumGoldenRod", "mediumOrchid", "mediumPurple", "mediumSeaGreen", "mediumSlateBlue", "mediumSpringGreen",
                                                 "mediumTurquoise", "mediumVioletRed", "mediumWood", "midnightBlue", "mintCream", "mistyRose",
                                                 "moccasin", "navajoWhite", "navy", "navyBlue", "neonBlue", "neonPink",
                                                 "newMidnightBlue", "newTan", "oldGold", "oldLace", "olive", "oliveDrab",
                                                 "orange", "orangeRed", "orchid", "paleGoldenRod", "paleGreen", "paleTurquoise",
                                                 "paleVioletRed", "papayaWhip", "peachPuff", "peru", "pink", "plum",
                                                 "powderBlue", "purple", "quartz", "red", "richBlue", "rosyBrown",
                                                 "royalBlue", "saddleBrown", "salmon", "sandyBrown", "scarlet", "seaGreen",
                                                 "seaShell", "semiSweetChocolate", "sienna", "silver", "skyBlue", "slateBlue",
                                                 "slateGray", "snow", "spicyPink", "springGreen", "steelBlue", "summerSky",
                                                 "tan", "teal", "thistle", "tomato", "turquoise", "veryLightGrey",
                                                 "violet", "violetRed", "wheat", "white", "whiteSmoke", "yellow", "yellowGreen"};
    private static final String[] COLOR_VALUES = {"#f0f8ff", "#faebd7", "#00ffff", "#7fffd4", "#f0ffff", "#5c3317",
                                                  "#f5f5dc", "#ffe4c4", "#000000", "#ffebcd", "#0000ff", "#8a2be2",
                                                  "#b5a642", "#d9d919", "#8c7853", "#a52a2a", "#deb887", "#5f9ea0",
                                                  "#7fff00", "#d2691e", "#d98719", "#b87333", "#ff7f50", "#6495ed",
                                                  "#fff8dc", "#dc143c", "#00ffff", "#00008b", "#5c4033", "#008b8b",
                                                  "#b8860b", "#404040", "#006400", "#4a766e", "#bdb76b", "#8b008b",
                                                  "#556b2f", "#ff8c00", "#9932cc", "#871f78", "#8b0000", "#e9967a",
                                                  "#8fbc8f", "#483d8b", "#2f4f4f", "#97694f", "#00ced1", "#9400d3",
                                                  "#855e42", "#ff1493", "#00bfff", "#696969", "#1e90ff", "#856363",
                                                  "#f5ccb0", "#d19275", "#b22222", "#fffaf0", "#228b22", "#ff00ff",
                                                  "#dcdcdc", "#f8f8ff", "#ffd700", "#daa520", "#808080", "#00ff00",
                                                  "#527f76", "#adff2f", "#f0fff0", "#ff69b4", "#215e21", "#cd5c5c",
                                                  "#4b0082", "#fffff0", "#f0e68c", "#e6e6fa", "#fff0f5", "#7cfc00",
                                                  "#fffacd", "#add8e6", "#f08080", "#e0ffff", "#fafad2", "#c0c0c0",
                                                  "#90ee90", "#ffb6c1", "#ffa07a", "#20b2aa", "#87cefa", "#8470ff",
                                                  "#778899", "#b0c4de", "#e9c2a6", "#ffffe0", "#00ff00", "#32cd32",
                                                  "#faf0e6", "#ff00ff", "#e47833", "#320000", "#66cdaa", "#0000cd",
                                                  "#eaeaae", "#ba55d3", "#9370d8", "#3cb371", "#7b68ee", "#00fa9a",
                                                  "#48d1cc", "#c71585", "#a68064", "#191970", "#f5fffa", "#ffe4e1",
                                                  "#ffe4b5", "#ffdead", "#000032", "#23238e", "#4d4dff", "#ff6ec7",
                                                  "#00009c", "#ebc79e", "#cfb53b", "#fdf5e6", "#323200", "#6b8e23",
                                                  "#ffc800", "#ff4500", "#da70d6", "#eee8aa", "#98fb98", "#afeeee",
                                                  "#d87093", "#ffefd5", "#ffdab9", "#cd853f", "#ffafaf", "#dda0dd",
                                                  "#b0e0e6", "#320032", "#d9d9f3", "#ff0000", "#5959ab", "#bc8f8f",
                                                  "#4169e1", "#8b4513", "#fa8072", "#f4a460", "#8c1717", "#2e8b57",
                                                  "#fff5ee", "#6b4226", "#a0522d", "#c0c0c0", "#87ceeb", "#6a5acd",
                                                  "#708090", "#fffafa", "#ff1cae", "#00ff7f", "#4682b4", "#38b0de",
                                                  "#d2b48c", "#003232", "#d8bfd8", "#ff6347", "#40e0d0", "#cdcdcd",
                                                  "#ee82ee", "#d02090", "#f5deb3", "#ffffff", "#f5f5f5", "#ffff00", "#9acd32"};

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
                for (int j = 0; j < FONT_ABSOLUTE_SIZE_NAMES.length; j++) {
                    if (FONT_ABSOLUTE_SIZE_NAMES[j].equalsIgnoreCase(propertyValue)) {
                        size = FONT_ABSOLUTE_SIZE_VALUES[j];
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
                            p = Integer.valueOf(perc);
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
                        size = Integer.valueOf(s);
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
                Color c = getColor("color", propertyValue);
                component.setForeground(c);
            } else if (propertyName.equalsIgnoreCase("background-color")) {
                Color c = getColor("background-color", propertyValue);
                component.setBackground(c);
            }
        }
    }

    public static Color getColor(String propertyName, String cssColor) {
        try {
            // check the CSS colors
            for (int j = 0; j < COLOR_NAMES.length; j++) {
                if (COLOR_NAMES[j].equalsIgnoreCase(cssColor)) {
                    return rgbToColor(COLOR_VALUES[j]);
                }
            }

            // check the UIDefaults colors
            Color color = UIManager.getColor(cssColor);
            if (color != null) return color;

            // parse as hex or rgb
            String c = cssColor.substring(4, cssColor.length() - 1);
            String[] values = c.split(",");
            for (int j = 0; j < values.length; j++) {
                if (values[j].endsWith("%")) {
                    int perc = Integer.valueOf(values[j].substring(0, values[j].length() - 1));
                    float factor = (float) perc / (float) 100;
                    values[j] = String.valueOf(Math.round((float) 255 * factor));
                }
            }
            return new Color(Integer.parseInt(values[0]), Integer.parseInt(values[1]), Integer.parseInt(values[2]));
        } catch (Exception e) {
            logger.log(Level.WARNING, String.format("%1$s value ('%2$s') not understood", propertyName, cssColor), e);
            return null;
        }
    }

    private static Color rgbToColor(String colorValue) {
        if (colorValue.startsWith("#")) colorValue = colorValue.substring(1);
        return Color.decode("0x" + colorValue);
    }

    public String[] getPropertyNames() {
        return new String[] {
                "font-family",
                "font-size",
                "font-weight",
                "font-style",
                "color",
                "background-color"
                //"swing-row-height"
        };
    }
}