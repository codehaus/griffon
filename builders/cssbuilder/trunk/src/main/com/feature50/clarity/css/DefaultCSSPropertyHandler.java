package com.feature50.clarity.css;

import java.util.logging.Logger;
import java.util.logging.Level;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.UIManager;
import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

public class DefaultCSSPropertyHandler implements CSSPropertyHandler {
    private static final Logger logger = Logger.getLogger(DefaultCSSPropertyHandler.class.getName());

    private static final String[] FONT_ABSOLUTE_SIZE_NAMES = { "xx-small", "x-small", "small", "medium", "large", "x-large", "xx-large" };
    private static final int[] FONT_ABSOLUTE_SIZE_VALUES =   { 7, 8, 9, 10, 11, 12, 13 };


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
            } else if (propertyName.equalsIgnoreCase("swing-row-height")) {
                if (component instanceof JTable) {
                    JTable table = (JTable) component;
                    try {
                        table.setRowHeight(Integer.parseInt(propertyValue));
                    } catch (NumberFormatException e) {
                        logger.warning(String.format("swing-row-height value ('%1$s') unsupported; just use an integer value", propertyValue));
                    }
                }
            }
        }
    }

    private Color getColor(String propertyName, String cssColor) {
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

    private Color rgbToColor(String colorValue) {
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
                "background-color",

                "swing-row-height"
        };
    }
}