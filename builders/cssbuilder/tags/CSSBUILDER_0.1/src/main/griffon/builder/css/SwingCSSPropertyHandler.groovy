/*
 * Copyright 2009 The original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
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

package griffon.builder.css

import java.util.logging.Logger
import java.util.logging.Level

import javax.swing.*
import javax.swing.border.*
import java.awt.Color
import java.awt.Insets
import java.awt.Dimension
import java.awt.Container
import com.feature50.clarity.css.CSSPropertyHandler
import static com.feature50.clarity.css.CSSUtils.getColor
import static com.feature50.clarity.css.CSSUtils.normalizeSize
import org.codehaus.groovy.control.CompilationFailedException

/**
 *
 * @author Andres Almiray
 */
@Singleton
class SwingCSSPropertyHandler implements CSSPropertyHandler {
    private static final Logger logger = Logger.getLogger(SwingCSSPropertyHandler.class.getName())

    public void processStyles(JComponent[] selected, String propertyName, String propertyValue) {
        for (int i = 0; i < selected.length; i++) {
            JComponent component = selected[i]

            /*if (propertyName.equalsIgnoreCase("margin")) {
                component.setBorder(calculateMargin(component,propertyValue))
            } else if (propertyName.equalsIgnoreCase("margin-top") ||
                       propertyName.equalsIgnoreCase("margin-left") ||
                       propertyName.equalsIgnoreCase("margin-bottom") ||
                       propertyName.equalsIgnoreCase("margin-right")) {
                component.setBorder(calculateMargin(component,propertyName,propertyValue))
            } else */if (propertyName.equalsIgnoreCase("padding")) {
                component.setBorder(calculatePadding(component,propertyValue))
            } else if (propertyName.equalsIgnoreCase("padding-top") ||
                       propertyName.equalsIgnoreCase("padding-left") ||
                       propertyName.equalsIgnoreCase("padding-bottom") ||
                       propertyName.equalsIgnoreCase("padding-right")) {
                component.setBorder(calculatePadding(component,propertyName,propertyValue))
            } else if (propertyName.equalsIgnoreCase("border-color")) {
                component.setBorder(calculateMatteBorder(component, getColor(propertyName,propertyValue)))
            } else if (propertyName.equalsIgnoreCase("border-width")) {
                component.setBorder(calculateMatteBorder(component, normalizeSize(propertyName,propertyValue)))
            } else if (propertyName.equalsIgnoreCase("width") ||
                       propertyName.equalsIgnoreCase("height")) {
                component.setSize(calculateSize(component.getSize(),propertyName,normalizeSize(propertyName,propertyValue)))
            } else if (propertyName.equalsIgnoreCase("min-width") ||
                       propertyName.equalsIgnoreCase("min-height")) {
                component.setMinimumSize(calculateSize(component.getMinimumSize(),propertyName.substring(4),normalizeSize(propertyName,propertyValue)))
            } else if (propertyName.equalsIgnoreCase("max-width") ||
                       propertyName.equalsIgnoreCase("max-height")) {
                component.setMaximumSize(calculateSize(component.getMaximumSize(),propertyName.substring(4),normalizeSize(propertyName,propertyValue)))
            } else if (propertyName.equalsIgnoreCase("pref-width") ||
                       propertyName.equalsIgnoreCase("pref-height")) {
                component.setPreferredSize(calculateSize(component.getPreferredSize(),propertyName.substring(5),normalizeSize(propertyName,propertyValue)))
            } else if (propertyName.equalsIgnoreCase("border-top-width") ||
                       propertyName.equalsIgnoreCase("border-left-width") ||
                       propertyName.equalsIgnoreCase("border-bottom-width") ||
                       propertyName.equalsIgnoreCase("border-right-width")) {
                component.setBorder(calculateMatteBorder(component,propertyName,normalizeSize(propertyName,propertyValue)))
            } else if (propertyName.equalsIgnoreCase("swing-row-height")) {
                if (component instanceof JTable) {
                    JTable table = (JTable) component
                    try {
                        table.setRowHeight(Integer.parseInt(propertyValue))
                    } catch (NumberFormatException e) {
                        logger.warning("swing-row-height value ('${propertyValue}') unsupported just use an integer value")
                    }
                }
            } else if (propertyName.equalsIgnoreCase("swing-client-property")) {
                propertyValue = propertyValue.trim()[1..-2] // strip quotes
                try {
                    Eval.me(propertyValue).each{ k, v -> component.putClientProperty(k,v) }
                } catch (CompilationFailedException e) {
                    logger.log(Level.WARNING, "swing-client-property value ('${propertyValue}') unsupported",e)
                }
            } else if (propertyName.equalsIgnoreCase("swing-halign")) {
                setHorizontalAlignment(component,propertyValue)
            } else if (propertyName.equalsIgnoreCase("swing-valign")) {
                setVerticalAlignment(component,propertyValue)
            }
        }
    }

    public String[] getPropertyNames() {
        return [/*
              "margin",
              "margin-top",
              "margin-bottom",
              "margin-left",
              "margin-right",*/
              "width",
              "height",
              "min-width",
              "min-height",
              "max-width",
              "max-height",
              "pref-width",
              "pref-height",
              "padding",
              "padding-top",
              "padding-bottom",
              "padding-left",
              "padding-right",
              "border-color",
              "border-width",
              "border-top-width",
              "border-left-width",
              "border-bottom-width",
              "border-right-width",
              "swing-row-height",
              "swing-client-property",
              "swing-halign",
              "swing-valign"
           ] as String[]
    }

    private Border calculateMargin( JComponent component, String value ) {
        Border currentBorder = component.border
        Insets insets = newInsets("margin",value.split(" "))
        if( !insets ) return currentBorder
        Border margin = createEmptyBorder(insets)
        if( !BorderUtils.isMargin(currentBorder) ) {
            Border newBorder = BorderFactory.createCompoundBorder(margin, currentBorder)
            newBorder.borderInsets = margin.borderInsets
            BorderUtils.setAsMargin(newBorder)
            currentBorder = newBorder
        } else {
            // currentBorder must be a compoundBorder
            Border newBorder = BorderFactory.createCompoundBorder(margin, currentBorder.insideBorder)
            newBorder.borderInsets = margin.borderInsets
            BorderUtils.setAsMargin(newBorder)
            BorderUtils.clear(currentBorder)
            currentBorder = newBorder
        }
        return currentBorder
    }

    private Border calculateMargin( JComponent component, String propertyName, String value ) {
        Border currentBorder = component.border
        String coord = propertyName.substring(7)
        int val = normalizeSize(propertyName,value)
        if( val < 0 ) return currentborder
        if( !BorderUtils.isMargin(currentBorder) ) {
            Insets insets = newInsets(propertyName,val,coord)
            Border margin = createEmptyBorder(insets)
            if( !margin ) return currentBorder
            Border newBorder = BorderFactory.createCompoundBorder(margin, currentBorder)
            newBorder.borderInsets = insets
            BorderUtils.setAsMargin(newBorder)
            currentBorder = newBorder
        } else {
            // currentBorder must be a compoundBorder
            Insets insets = merge(newInsets(propertyName,val,coord), currentBorder.borderInsets, coord)
            Border margin = createEmptyBorder(insets)
            if( !margin ) return currentBorder
            Border newBorder = BorderFactory.createCompoundBorder(margin, currentBorder.insideBorder)
            newBorder.borderInsets = insets
            BorderUtils.setAsMargin(newBorder)
            BorderUtils.clear(currentBorder)
            currentBorder = newBorder
        }
        return currentBorder
    }

    private Border calculatePadding( JComponent component, String value ) {
        Border currentBorder = component.border
        Insets insets = newInsets("padding",value.split(" "))
        if( !insets ) return currentBorder
        Border padding = createEmptyBorder(insets)
        if( BorderUtils.isMargin(currentBorder) ) currentBorder = currentBorder.insideBorder
        if( !BorderUtils.isPadding(currentBorder) ) {
            Border newBorder = BorderFactory.createCompoundBorder(currentBorder,padding)
            newBorder.borderInsets = padding.borderInsets
            BorderUtils.setAsPadding(newBorder)
            currentBorder = newBorder
        } else {
            // currentBorder must be a compoundBorder
            Border newBorder = BorderFactory.createCompoundBorder(currentBorder.outsideBorder,padding)
            newBorder.borderInsets = padding.borderInsets
            BorderUtils.setAsPadding(newBorder)
            BorderUtils.clear(currentBorder)
            currentBorder = newBorder
        }
        // rewrap if margin
        if( BorderUtils.isMargin(component.border) ) {
            BorderUtils.clear(component.border)
            currentBorder = BorderFactory.createCompoundBorder(component.border.outsideBorder,currentBorder)
            currentBorder.borderInsets = component.border.borderInsets
            BorderUtils.setAsMargin(currentBorder)
        }
        return currentBorder
    }

    private Border calculatePadding( JComponent component, String propertyName, String value ) {
        Border currentBorder = component.border
        String coord = propertyName.substring(8)
        int val = normalizeSize(propertyName,value)
        if( val < 0 ) return currentborder
        if( BorderUtils.isMargin(currentBorder) ) currentBorder = currentBorder.insideBorder
        if( !BorderUtils.isPadding(currentBorder) ) {
            Insets insets = newInsets(propertyName,val,coord)
            Border padding = createEmptyBorder(insets)
            if( !padding ) return currentBorder
            Border newBorder = BorderFactory.createCompoundBorder(currentBorder,padding)
            newBorder.borderInsets = insets
            BorderUtils.setAsPadding(newBorder)
            currentBorder = newBorder
        } else {
            // currentBorder must be a compoundBorder
            Insets insets = merge(newInsets(propertyName,val,coord), currentBorder.borderInsets, coord)
            Border padding = createEmptyBorder(insets)
            if( !padding ) return currentBorder
            Border newBorder = BorderFactory.createCompoundBorder(currentBorder.outsideBorder,padding)
            newBorder.borderInsets = insets
            BorderUtils.setAsPadding(newBorder)
            BorderUtils.clear(currentBorder)
            currentBorder = newBorder
        }
        // rewrap if margin
        if( BorderUtils.isMargin(component.border) ) {
            BorderUtils.clear(component.border)
            currentBorder = BorderFactory.createCompoundBorder(component.border.outsideBorder,currentBorder)
            currentBorder.borderInsets = component.border.borderInsets
            BorderUtils.setAsMargin(currentBorder)
        }
        return currentBorder
    }

    private Border calculateMatteBorder( JComponent component, Color color ) {
        Border currentBorder = component.border
        if( BorderUtils.isMargin(currentBorder) ) currentBorder = currentBorder.insideBorder
        if( !BorderUtils.isPadding(currentBorder) ) {
            // wack it
            Insets borderInsets = currentBorder.borderInsets
            Insets insets = new Insets(
               borderInsets?.top != null    ? borderInsets.top : 0,
               borderInsets?.left != null   ? borderInsets.left : 0,
               borderInsets?.bottom != null ? borderInsets.bottom : 0,
               borderInsets?.right != null  ? borderInsets.right : 0,
            )
            Border newBorder = createMatteBorder(insets, color)
            newBorder.borderColor = color
            currentBorder = newBorder
        } else {
            Border outer = currentBorder.outsideBorder
            Insets borderInsets = outer.borderInsets
            Insets insets = new Insets(
               borderInsets?.top != null    ? borderInsets.top : 0,
               borderInsets?.left != null   ? borderInsets.left : 0,
               borderInsets?.bottom != null ? borderInsets.bottom : 0,
               borderInsets?.right != null  ? borderInsets.right : 0,
            )
            Border newBorder = BorderFactory.createCompoundBorder(createMatteBorder(insets, color),currentBorder.insideBorder)
            newBorder.borderColor = color
            newBorder.borderInsets = insets
            BorderUtils.setAsPadding(newBorder)
            BorderUtils.clear(currentBorder)
            currentBorder = newBorder
        }
        if( BorderUtils.isMargin(component.border) ) {
            BorderUtils.clear(component.border)
            currentBorder = BorderFactory.createCompoundBorder(component.border.outsideBorder,currentBorder)
            currentBorder.borderInsets = component.border.borderInsets
            BorderUtils.setAsMargin(currentBorder)
        }
        return currentBorder
    }

    private Border calculateMatteBorder( JComponent component, int width ) {
        Border currentBorder = component.border
        if( BorderUtils.isMargin(currentBorder) ) currentBorder = currentBorder.insideBorder
        if( !BorderUtils.isPadding(currentBorder) ) {
            // wack it
            Insets insets = new Insets(width,width,width,width)
            Border newBorder = createMatteBorder(insets, currentBorder.borderColor)
            newBorder.borderColor = currentBorder.borderColor
            currentBorder = newBorder
        } else {
            Border outer = currentBorder.outsideBorder
            Insets insets = new Insets(width,width,width,width)
            Border newBorder = BorderFactory.createCompoundBorder(createMatteBorder(insets, currentBorder.borderColor),currentBorder.insideBorder)
            newBorder.borderColor = outer.borderColor
            newBorder.borderInsets = insets
            BorderUtils.setAsPadding(newBorder)
            BorderUtils.clear(currentBorder)
            currentBorder = newBorder
        }
        if( BorderUtils.isMargin(component.border) ) {
            BorderUtils.clear(component.border)
            currentBorder = BorderFactory.createCompoundBorder(component.border.outsideBorder,currentBorder)
            currentBorder.borderInsets = component.border.borderInsets
            BorderUtils.setAsMargin(currentBorder)
        }
        return currentBorder
    }

    private Border calculateMatteBorder( JComponent component, String propertyName, int width ) {
        Border currentBorder = component.border
        String coord = (propertyName - "-width").substring(7)
        if( width < 0 ) return currentborder
        if( BorderUtils.isMargin(currentBorder) ) currentBorder = currentBorder.insideBorder
        if( !BorderUtils.isPadding(currentBorder) ) {
            // wack it
            Insets insets = merge(newInsets(propertyName, width, coord), currentBorder.borderInsets, coord)
            Border newBorder = createMatteBorder(insets, currentBorder.borderColor)
            newBorder.borderColor = currentBorder.borderColor
            currentBorder = newBorder
        } else {
            Border outer = currentBorder.outsideBorder
            Insets insets = merge(newInsets(propertyName, width, coord), outer.borderInsets, coord)
            Border newBorder = BorderFactory.createCompoundBorder(createMatteBorder(insets, currentBorder.borderColor),currentBorder.insideBorder)
            newBorder.borderInsets = insets
            newBorder.borderColor = currentBorder.borderColor
            BorderUtils.setAsPadding(newBorder)
            BorderUtils.clear(currentBorder)
            currentBorder = newBorder
        }
        if( BorderUtils.isMargin(component.border) ) {
            BorderUtils.clear(component.border)
            currentBorder = BorderFactory.createCompoundBorder(component.border.outsideBorder,currentBorder)
            currentBorder.borderInsets = component.border.borderInsets
            BorderUtils.setAsMargin(currentBorder)
        }
        return currentBorder
    }

    private void setHorizontalAlignment( JComponent component, String propertyValue ) {
        try {
            if ("left".equalsIgnoreCase(propertyValue)) {
                component.setHorizontalAlignment(SwingConstants.LEFT)
            } else if ("right".equalsIgnoreCase(propertyValue)) {
                component.setHorizontalAlignment(SwingConstants.RIGHT)
            } else if ("center".equalsIgnoreCase(propertyValue)) {
                component.setHorizontalAlignment(SwingConstants.CENTER)
            } else {
               throw new RuntimeException("Unknown value ('${propertyValue}'), use 'left', 'right' or 'center'.")
            }
        } catch (Exception e) {
            logger.log(Level.WARNING,"swing-halign does not support value ('${propertyValue}')",e)
        }
    }

    private void setVerticalAlignment( JComponent component, String propertyValue ) {
        try {
            if ("top".equalsIgnoreCase(propertyValue)) {
                component.setVerticalAlignment(SwingConstants.TOP)
            } else if ("bottom".equalsIgnoreCase(propertyValue)) {
                component.setVerticalAlignment(SwingConstants.BOTTOM)
            } else if ("middle".equalsIgnoreCase(propertyValue)) {
                component.setHorizontalAlignment(SwingConstants.CENTER)
            } else {
               throw new RuntimeException("Unknown value ('${propertyValue}'), use 'top', 'bottom' or 'middle'.")
            }
        } catch (Exception e) {
            logger.log(Level.WARNING,"swing-valign does not support value ('${propertyValue}')",e)
        }
    }

    private Dimension calculateSize( Dimension originalSize, String prop, int value ) {
        if( value < 0 ) return originalSize
        return new Dimension(
            (prop == "width" ? value : originalSize.width) as int,
            (prop == "height" ? value : originalSize.height) as int
        )
    }

    private Border createEmptyBorder( Insets insets ) {
        Border border = BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right)
        border.borderInsets = insets
        return border
    }

    private Border createMatteBorder( Insets insets, Color color ) {
        Border border = BorderFactory.createMatteBorder(insets.top, insets.left, insets.bottom, insets.right, color)
        border.borderInsets = insets
        return border
    }

    private Insets newInsets( String propertyName, String[] values ) {
        Insets insets = null
        switch(values.size()) {
           case 1: insets = new Insets(normalizeSize(propertyName,values[0]),0,0,0); break;
           case 2: insets = new Insets(normalizeSize(propertyName,values[0]),0,normalizeSize(propertyName,values[1]),0); break;
           case 3: insets = new Insets(normalizeSize(propertyName,values[0]),normalizeSize(propertyName,values[1]),
                                       normalizeSize(propertyName,values[2]),normalizeSize(propertyName,values[1])); break;
           case 4: insets = new Insets(normalizeSize(propertyName,values[0]),normalizeSize(propertyName,values[3]),
                                       normalizeSize(propertyName,values[2]),normalizeSize(propertyName,values[1])); break;
           default:
               logger.warning("${propertyName} value ('${values}') unsupported just use integer values")
               return null
        }
        return insets
    }

    private Insets newInsets( String propertyName, String size ) {
        int val = normalizeSize(propertyName,size)
        if( val == -1 ) return null
        return new Insets(val,val,val,val)
    }

    private Insets newInsets( String propertyName, String size, String coord ) {
        int val = normalizeSize(propertyName,size)
        if( val == -1 ) return null
        return new Insets(
            coord == "top"    ? val: 0,
            coord == "left"   ? val: 0,
            coord == "bottom" ? val: 0,
            coord == "right"  ? val: 0
        )
    }

    private Insets newInsets( String propertyName, int val, String coord ) {
        return new Insets(
            coord == "top"    ? val: 0,
            coord == "left"   ? val: 0,
            coord == "bottom" ? val: 0,
            coord == "right"  ? val: 0
        )
    }

    private Insets merge( Insets a, Insets b, String coord ) {
        return new Insets(
             coord == "top"    ? a.top:    (b ? b.top : 0),
             coord == "left"   ? a.left:   (b ? b.left : 0),
             coord == "bottom" ? a.bottom: (b ? b.bottom : 0),
             coord == "right"  ? a.right:  (b ? b.right : 0)
        )
    }
}