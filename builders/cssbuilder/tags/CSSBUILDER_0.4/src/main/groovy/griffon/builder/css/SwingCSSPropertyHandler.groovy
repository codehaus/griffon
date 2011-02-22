/*
 * Copyright 2009-2010 The original author or authors.
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
import javax.swing.text.*
import java.awt.Color
import java.awt.Insets
import java.awt.Dimension
import java.awt.Container
import com.feature50.clarity.css.CSSPropertyHandler
import static com.feature50.clarity.css.CSSUtils.*
import org.codehaus.groovy.control.CompilationFailedException

/**
 *
 * @author Andres Almiray
 */
@Singleton
class SwingCSSPropertyHandler implements CSSPropertyHandler {
    private static final Logger logger = Logger.getLogger(SwingCSSPropertyHandler.class.getName())

    public void processStyles(JComponent[] selected, String propertyName, String propertyValue) {
        int n = -1
        for (int i = 0; i < selected.length; i++) {
            JComponent component = selected[i]

            switch(propertyName.toLowerCase()) {
               case "width":
               case "height":
                  n = normalizeSize(propertyName,propertyValue)
                  if( n > -1 ) component.setSize(calculateSize(component.getSize(),propertyName,n))
                  break;
               case "min-width":
               case "min-height":
                  n = normalizeSize(propertyName,propertyValue)
                  if( n > -1 ) component.setMinimumSize(calculateSize(component.getMinimumSize(),propertyName.substring(4),n))
                  break;
               case "max-width":
               case "max-height":
                  n = normalizeSize(propertyName,propertyValue)
                  if( n > -1 ) component.setMaximumSize(calculateSize(component.getMaximumSize(),propertyName.substring(4),n))
                  break;
               case "pref-width":
               case "pref-height":
                  n = normalizeSize(propertyName,propertyValue)
                  if( n > -1 ) component.setPreferredSize(calculateSize(component.getPreferredSize(),propertyName.substring(5),n))
                  break;
               case "padding":
                  component.setBorder(calculatePadding(component,propertyValue))
                  break;
               case "padding-top":
               case "padding-bottom":
               case "padding-left":
               case "padding-right":
                  component.setBorder(calculatePadding(component,propertyName,propertyValue))
                  break;
               case "border-color":
                  component.setBorder(calculateMatteBorder(component.border, getColor(propertyName,propertyValue)))
                  break;
               case "border-width":
                  component.setBorder(calculateMatteBorder(component.border, normalizeSize(propertyName,propertyValue)))
                  break;
               case "border-top-width":
               case "border-left-width":
               case "border-bottom-width":
               case "border-right-width":
                  component.setBorder(calculateMatteBorder(component.border,propertyName,7,normalizeSize(propertyName,propertyValue)))
                  break;
               case "swing-row-height":
                  switch(component) {
                     case JTable:
                     case JTree:
                       try {
                          component.setRowHeight(Integer.parseInt(propertyValue))
                       } catch (NumberFormatException e) {
                          logger.warning("${propertyName} value ('${propertyValue}') unsupported just use an integer value")
                       }
                  }
                  break;
               case "swing-client-property":
                  propertyValue = propertyValue.trim()[1..-2] // strip quotes
                  try {
                     Eval.me(propertyValue).each{ k, v -> component.putClientProperty(k,v) }
                  } catch (CompilationFailedException e) {
                     logger.log(Level.WARNING, "swing-client-property value ('${propertyValue}') not supported",e)
                  }
                  break;
               case "swing-halign":
                  switch(component) {
                     case JLabel:
                     case AbstractButton:
                     case JTextField:
                        component.setHorizontalAlignment(getHorizontalAlignment(propertyName,propertyValue))
                  }
                  break;
               case "swing-valign":
                  switch(component) {
                     case JLabel:
                     case AbstractButton:
                        component.setVerticalAlignment(getVerticalAlignment(propertyName,propertyValue))
                  }
                  break;
               case "swing-cell-height":
                  switch(component) {
                     case JList:
                       try {
                          component.setFixedCellHeight(Integer.parseInt(propertyValue))
                       } catch (NumberFormatException e) {
                          logger.warning("${propertyName} value ('${propertyValue}') unsupported just use an integer value")
                       }
                  }
                  break;
               case "swing-cell-width":
                  switch(component) {
                     case JList:
                       try {
                          component.setFixedCellWidth(Integer.parseInt(propertyValue))
                       } catch (NumberFormatException e) {
                          logger.warning("${propertyName} value ('${propertyValue}') unsupported just use an integer value")
                       }
                  }
                  break;
               case "swing-row-margin":
                  switch(component) {
                     case JTable:
                        n = normalizeSize(propertyName,propertyValue)
                        if( n > -1 ) component.setRowMargin(n)
                  }
                  break;
               case "swing-grid-color":
                  switch(component) {
                     case JTable:
                        component.setGridColor(getColor(propertyName,propertyValue))
                  }
                  break;
               case "swing-selection-color":
                  switch(component) {
                     case JTable:
                     case JList:
                        component.setSelectionForeground(getColor(propertyName,propertyValue))
                        break;
                     case JTextComponent:
                        component.setSelectionColor(getColor(propertyName,propertyValue))
                  }
                  break;
               case "swing-selection-background-color":
                  switch(component) {
                     case JTable:
                     case JList:
                        component.setSelectionBackground(getColor(propertyName,propertyValue))
                  }
                  break;
               case "swing-row-selection-allowed":
                  switch(component) {
                     case JTable:
                        component.setRowSelectionAllowed(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-column-selection-allowed":
                  switch(component) {
                     case JTable:
                        component.setColumnSelectionAllowed(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-auto-resize-mode":
                  switch(component) {
                     case JTable:
                        component.setAutoResizeMode(getAutoResizeMode(propertyName,propertyValue))
                  }
                  break;
               case "swing-show-grid":
                  switch(component) {
                     case JTable:
                        component.setShowGrid(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-show-vertical-lines":
                  switch(component) {
                     case JTable:
                        component.setShowVerticalLines(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-cell-selection-enabled":
                  switch(component) {
                     case JTable:
                        component.setCellSelectionEnabled(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-show-horizontal-lines":
                  switch(component) {
                     case JTable:
                        component.setShowHorizontalLines(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-fills-viewport-height":
                  switch(component) {
                     case JTable:
                        component.setFillsViewportHeight(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-selection-mode":
                  switch(component) {
                     case JTable:
                     case JList:
                        component.setSelectionMode(getSelectionMode(propertyName,propertyValue))
                  }
                  break;
               case "swing-border-painted":
                  switch(component) {
                     case JToolBar:
                     case JMenuBar:
                     case JProgressBar:
                        component.setBorderPainted(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-floatable":
                  switch(component) {
                     case JToolBar:
                        component.setFloatable(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-margin":
                  switch(component) {
                     case JToolBar:
                     case JMenuBar:
                     case JTextComponent:
                        component.setMargin(newInsets(propertyName,propertyValue.split(" ")))
                  }
                  break;
               case "swing-orientation":
                  switch(component) {
                     case JToolBar:
                     case JProgressBar:
                     case JScrollBar:
                     case JSlider:
                     case JSeparator:
                     case JSplitPane:
                        component.setOrientation(getOrientation(propertyName,propertyValue))
                  }
                  break;
               case "swing-rollover":
                  switch(component) {
                     case JToolBar:
                        component.setRollover(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-columns":
                  switch(component) {
                     case JTextField:
                     case JTextArea:
                        n = normalizeSize(propertyName,propertyValue)
                        if( n > -1 ) component.setColumns(n)
                  }
                  break;
               case "swing-rows":
                  switch(component) {
                     case JTextArea:
                        n = normalizeSize(propertyName,propertyValue)
                        if( n > -1 ) component.setRows(n)
                  }
                  break;
               case "swing-line-wrap":
                  switch(component) {
                     case JTextArea:
                        component.setLineWrap(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-tab-size":
                  switch(component) {
                     case JTextArea:
                        n = normalizeSize(propertyName,propertyValue)
                        if( n > -1 ) component.setTabSize(n)
                  }
                  break;
               case "swing-tab-placement":
                  switch(component) {
                     case JTabbedPane:
                        component.setTabPlacement(getTabPlacement(propertyName,propertyValue))
                  }
                  break;
               case "swing-tab-layout-policy":
                  switch(component) {
                     case JTabbedPane:
                        component.setTabLayoutPolicy(getTabLayoutPolicy(propertyName,propertyValue))
                  }
                  break;
               case "swing-wrap-style-word":
                  switch(component) {
                     case JTextArea:
                        component.setWrapStyleWord(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-caret-color":
                  switch(component) {
                     case JTextComponent:
                        component.setCaretColor(getColor(propertyName,propertyValue))
                  }
                  break;
               case "swing-disabled-text-color":
                  switch(component) {
                     case JTextComponent:
                        component.setDisabledTextColor(getColor(propertyName,propertyValue))
                  }
                  break;
               case "swing-selected-text-color":
                  switch(component) {
                     case JTextComponent:
                        component.setSelectedTextColor(getColor(propertyName,propertyValue))
                  }
                  break;
               case "swing-editable":
                  switch(component) {
                     case JTextComponent:
                     case JComboBox:
                     case JTree:
                        component.setEditable(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-horizontal-scrollbar-policy":
                  switch(component) {
                     case JScrollPane:
                        component.setHorizontalScrollBarPolicy(getHorizontalScrollBarPolicy(propertyName,propertyValue))
                  }
                  break;
               case "swing-vertical-scrollbar-policy":
                  switch(component) {
                     case JScrollPane:
                        component.setVerticalScrollBarPolicy(getVerticalScrollBarPolicy(propertyName,propertyValue))
                  }
                  break;
               case "swing-viewport-border-color":
                  switch(component) {
                     case JScrollPane:
                        component.setViewportBorder(calculateMatteBorder(component.viewportBorder, getColor(propertyName,propertyValue)))
                  }
                  break;
               case "swing-viewport-border-width":
                  switch(component) {
                     case JScrollPane:
                        component.setViewportBorder(calculateMatteBorder(component.viewportBorder, normalizeSize(propertyName,propertyValue)))
                  }
                  break;
               case "swing-viewport-border-top-width":
               case "swing-viewport-border-left-width":
               case "swing-viewport-border-bottom-width":
               case "swing-viewport-border-right-width":
                  switch(component) {
                     case JScrollPane:
                        component.setViewportBorder(calculateMatteBorder(component.viewportBorder,propertyName,22,normalizeSize(propertyName,propertyValue)))
                  }
                  break;
               case "swing-horizontal-text-position":
                  switch(component) {
                     case JLabel:
                     case AbstractButton:
                        component.setHorizontalTextPosition(getHorizontalTextPosition(propertyName,propertyValue))
                  }
                  break;
               case "swing-vertical-text-position":
                  switch(component) {
                     case JLabel:
                     case AbstractButton:
                        component.setVerticalTextPosition(getVerticalTextPosition(propertyName,propertyValue))
                  }
                  break;
               case "swing-icon-text-gap":
                  switch(component) {
                     case JLabel:
                        n = normalizeSize(propertyName,propertyValue)
                        if( n > -1 ) component.setIconTextGap(n)
                  }
                  break;
               case "swing-resizable":
                  switch(component) {
                     case JFrame:
                     case JDialog:
                     case JInternalFrame:
                        component.setResizable(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-indeterminate":
                  switch(component) {
                     case JProgressBar:
                        component.setIndeterminate(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-minimum":
                  switch(component) {
                     case JProgressBar:
                     case JScrollBar:
                     case JSlider:
                        n = normalizeSize(propertyName,propertyValue)
                        if( n > -1 ) component.setMinimum(n)
                  }
                  break;
               case "swing-maximum":
                  switch(component) {
                     case JProgressBar:
                     case JScrollBar:
                     case JSlider:
                        n = normalizeSize(propertyName,propertyValue)
                        if( n > -1 ) component.setMaximum(n)
                  }
                  break;
               case "swing-inverted":
                  switch(component) {
                     case JSlider:
                        component.setInverted(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-paint-labels":
                  switch(component) {
                     case JSlider:
                        component.setPaintLabels(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-paint-ticks":
                  switch(component) {
                     case JSlider:
                        component.setPaintTicks(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-paint-tracks":
                  switch(component) {
                     case JSlider:
                         component.setPaintTracks(getBoolean(propertyName,propertyValue))
                  }
                  break;
               case "swing-snap-to-ticks":
                  switch(component) {
                     case JSlider:
                         component.setSnapToTicks(getBoolean(propertyName,propertyValue))
                  }
                  break;
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
              "swing-valign",
              "swing-cell-height",
              "swing-cell-width",
              "swing-row-margin",
              "swing-grid-color",
              "swing-selection-color",
              "swing-selection-background-color",
              "swing-row-selection-allowed",
              "swing-column-selection-allowed",
              "swing-auto-resize-mode",
              "swing-show-grid",
              "swing-show-vertical-lines",
              "swing-cell-selection-enabled",
              "swing-show-horizontal-lines",
              "swing-fills-viewport-height",
              "swing-selection-mode",
              "swing-border-painted",
              "swing-floatable",
              "swing-margin",
              "swing-orientation",
              "swing-rollover",
              "swing-columns",
              "swing-rows",
              "swing-line-wrap",
              "swing-tab-size",
              "swing-tab-placement",
              "swing-tab-layout-policy",
              "swing-wrap-style-word",
              "swing-caret-color",
              "swing-disabled-text-color",
              "swing-selected-text-color",
              "swing-editable",
              "swing-horizontal-scrollbar-policy",
              "swing-vertical-scrollbar-policy",
              "swing-viewport-border-color",
              "swing-viewport-border-width",
              "swing-viewport-border-top-width",
              "swing-viewport-border-left-width",
              "swing-viewport-border-bottom-width",
              "swing-viewport-border-right-width",
              "swing-horizontal-text-position",
              "swing-vertical-text-position",
              "swing-icon-text-gap",
              "swing-resizable",
              "swing-indeterminate",
              "swing-minimum",
              "swing-maximum",
              "swing-inverted",
              "swing-paint-labels",
              "swing-paint-ticks",
              "swing-paint-tracks",
              "swing-snap-to-ticks"
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
        if( BorderUtils.isPadding(currentBorder) ) {
           // currentBorder must be a compoundBorder
            Border newBorder = BorderFactory.createCompoundBorder(currentBorder.outsideBorder,padding)
            newBorder.borderInsets = padding.borderInsets
            BorderUtils.setAsPadding(newBorder)
            BorderUtils.clear(currentBorder)
            currentBorder = newBorder
        } else {
            Border newBorder = BorderFactory.createCompoundBorder(currentBorder,padding)
            newBorder.borderInsets = padding.borderInsets
            BorderUtils.setAsPadding(newBorder)
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
        if( BorderUtils.isPadding(currentBorder) ) {
            // currentBorder must be a compoundBorder
            Insets insets = merge(newInsets(propertyName,val,coord), currentBorder.borderInsets, coord)
            Border padding = createEmptyBorder(insets)
            if( !padding ) return currentBorder
            Border newBorder = BorderFactory.createCompoundBorder(currentBorder.outsideBorder,padding)
            newBorder.borderInsets = insets
            BorderUtils.setAsPadding(newBorder)
            BorderUtils.clear(currentBorder)
            currentBorder = newBorder
        } else {
            Insets insets = newInsets(propertyName,val,coord)
            Border padding = createEmptyBorder(insets)
            if( !padding ) return currentBorder
            Border newBorder = BorderFactory.createCompoundBorder(currentBorder,padding)
            newBorder.borderInsets = insets
            BorderUtils.setAsPadding(newBorder)
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

    private Border calculateMatteBorder( Border originalBorder, Color color ) {
        Border currentBorder = originalBorder
        if( BorderUtils.isMargin(currentBorder) ) currentBorder = currentBorder.insideBorder
        if( BorderUtils.isPadding(currentBorder) ) {
            Border outer = currentBorder?.outsideBorder
            Insets borderInsets = outer?.borderInsets
            Insets insets = new Insets(
               borderInsets?.top != null    ? borderInsets.top : 0,
               borderInsets?.left != null   ? borderInsets.left : 0,
               borderInsets?.bottom != null ? borderInsets.bottom : 0,
               borderInsets?.right != null  ? borderInsets.right : 0,
            )
            Border newBorder = currentBorder ? BorderFactory.createCompoundBorder(createMatteBorder(insets, color),currentBorder.insideBorder) : createMatteBorder(insets, color)
            newBorder.borderColor = color
            newBorder.borderInsets = insets
            BorderUtils.setAsPadding(newBorder)
            BorderUtils.clear(currentBorder)
            currentBorder = newBorder
        } else {
            // wack it
            Insets borderInsets = currentBorder?.borderInsets
            Insets insets = new Insets(
               borderInsets?.top != null    ? borderInsets.top : 0,
               borderInsets?.left != null   ? borderInsets.left : 0,
               borderInsets?.bottom != null ? borderInsets.bottom : 0,
               borderInsets?.right != null  ? borderInsets.right : 0,
            )
            Border newBorder = createMatteBorder(insets, color)
            newBorder.borderColor = color
            currentBorder = newBorder
        }
        if( BorderUtils.isMargin(originalBorder) ) {
            BorderUtils.clear(originalBorder)
            currentBorder = BorderFactory.createCompoundBorder(originalBorder.outsideBorder,currentBorder)
            currentBorder.borderInsets = originalBorder.borderInsets
            BorderUtils.setAsMargin(currentBorder)
        }
        return currentBorder
    }

    private Border calculateMatteBorder( Border originalBorder, int width ) {
        Border currentBorder = originalBorder
        if( BorderUtils.isMargin(currentBorder) ) currentBorder = currentBorder.insideBorder
        if( BorderUtils.isPadding(currentBorder) ) {
            Border outer = currentBorder?.outsideBorder
            Insets insets = new Insets(width,width,width,width)
            Border newBorder = currentBorder ? BorderFactory.createCompoundBorder(createMatteBorder(insets, currentBorder.borderColor),currentBorder.insideBorder) : createMatteBorder(insets, Color.BLACK)
            newBorder.borderColor = outer.borderColor ?: Color.BLACK
            newBorder.borderInsets = insets
            BorderUtils.setAsPadding(newBorder)
            BorderUtils.clear(currentBorder)
            currentBorder = newBorder
        } else {
            // wack it
            Insets insets = new Insets(width,width,width,width)
            Border newBorder = createMatteBorder(insets, currentBorder.borderColor)
            newBorder.borderColor = currentBorder?.borderColor ?: Color.BLACK
            currentBorder = newBorder
        }
        if( BorderUtils.isMargin(originalBorder) ) {
            BorderUtils.clear(originalBorder)
            currentBorder = BorderFactory.createCompoundBorder(originalBorder.outsideBorder,currentBorder)
            currentBorder.borderInsets = originalBorder.borderInsets
            BorderUtils.setAsMargin(currentBorder)
        }
        return currentBorder
    }

    private Border calculateMatteBorder( Border originalBorder, String propertyName, int offset, int width ) {
        Border currentBorder = originalBorder
        String coord = (propertyName - "-width").substring(offset)
        if( width < 0 ) return currentborder
        if( BorderUtils.isMargin(currentBorder) ) currentBorder = currentBorder.insideBorder
        if( BorderUtils.isPadding(currentBorder) ) {
            Border outer = currentBorder?.outsideBorder
            Insets insets = merge(newInsets(propertyName, width, coord), outer?.borderInsets, coord)
            Border newBorder = currentBorder ? BorderFactory.createCompoundBorder(createMatteBorder(insets, currentBorder.borderColor),currentBorder.insideBorder) : createMatteBorder(insets, Color.BLACK)
            newBorder.borderInsets = insets
            newBorder.borderColor = currentBorder?.borderColor ?: Color.BLACK
            BorderUtils.setAsPadding(newBorder)
            BorderUtils.clear(currentBorder)
            currentBorder = newBorder
        } else {
            // wack it
            Insets insets = merge(newInsets(propertyName, width, coord), currentBorder.borderInsets, coord)
            Border newBorder = createMatteBorder(insets, currentBorder.borderColor)
            newBorder.borderColor = currentBorder?.borderColor ?: Color.BLACK
            currentBorder = newBorder
        }
        if( BorderUtils.isMargin(originalBorder) ) {
            BorderUtils.clear(originalBorder)
            currentBorder = BorderFactory.createCompoundBorder(originalBorder.outsideBorder,currentBorder)
            currentBorder.borderInsets = originalBorder.borderInsets
            BorderUtils.setAsMargin(currentBorder)
        }
        return currentBorder
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
               logger.warning("${propertyName} value ('${values}') unsupported use 1, 2, 3 or 4 integer values")
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
        return newInsets(propertyName, val, coord)
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

    private int getAutoResizeMode( String propertyName, String propertyValue ) {
        switch(propertyValue.toUpperCase()) {
            case "OFF": return JTable.AUTO_RESIZE_OFF
            case "NEXT_COLUMN": return JTable.AUTO_RESIZE_NEXT_COLUMN
            case "LAST_COLUMN": return JTable.AUTO_RESIZE_LAST_COLUMN
            case "ALL_COLUMNS": return JTable.AUTO_RESIZE_ALL_COLUMNS
            case "SUBSEQUENT_COLUMNS": return JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS
            default:
                logger.warning("${propertyName} value ('${propertyValue}') unsupported, use any of 'OFF', 'NEXT_COLUMN' ,'LAST_COLUMN' ,'ALL_COLUMNS' or 'SUBSEQUENT_COLUMNS'.")
        }
        return JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS
    }

    private int getSelectionMode( String propertyName, String propertyValue ) {
        switch(propertyValue.toUpperCase()) {
            case "SINGLE": return ListSelectionModel.SINGLE_SELECTION
            case "SINGLE_INTERVAL": return ListSelectionModel.SINGLE_INTERVAL_SELECTION
            case "MULTIPLE_INTERVAL": return ListSelectionModel.MULTIPLE_INTERVAL_SELECTION
            default:
                logger.warning("${propertyName} value ('${propertyValue}') unsupported, use any of 'SINGLE', 'SINGLE_INTERVAL' or 'MULTIPLE_INTERVAL'.")
        }
        return ListSelectionModel.SINGLE_SELECTION
    }

    private int getTabPlacement( String propertyName, String propertyValue ) {
        switch(propertyValue.toUpperCase()) {
            case "TOP": return JTabbedPane.TOP
            case "BOTTOM": return JTabbedPane.BOTTOM
            case "LEFT": return JTabbedPane.LEFT
            case "RIGHT": return JTabbedPane.RIGHT
            default:
                logger.warning("${propertyName} value ('${propertyValue}') unsupported, use any of 'TOP', 'BOTTOM', 'LEFT' or 'RIGHT'.")
        }
        return JTabbedPane.TOP
    }

    private int getTabLayoutPolicy( String propertyName, String propertyValue ) {
        switch(propertyValue.toUpperCase()) {
            case "WRAP": return JTabbedPane.WRAP_TAB_LAYOUT
            case "SCROLL": return JTabbedPane.SCROLL_TAB_LAYOUT
            default:
                logger.warning("${propertyName} value ('${propertyValue}') unsupported, use any of 'WRAP' or 'SCROLL'.")
        }
        return JTabbedPane.WRAP_TAB_LAYOUT
    }

    private int getHorizontalScrollBarPolicy( String propertyName, String propertyValue ) {
        switch(propertyValue.toUpperCase()) {
            case "AS_NEEDED": return ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
            case "NEVER": return ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER
            case "ALWAYS": return ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS
            default:
                logger.warning("${propertyName} value ('${propertyValue}') unsupported, use any of 'AS_NEEDED', 'NEVER' or 'ALWAYS'.")
        }
        return ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED
    }

    private int getVerticalScrollBarPolicy( String propertyName, String propertyValue ) {
        switch(propertyValue.toUpperCase()) {
            case "AS_NEEDED": return ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
            case "NEVER": return ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER
            case "ALWAYS": return ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS
            default:
                logger.warning("${propertyName} value ('${propertyValue}') unsupported, use any of 'AS_NEEDED', 'NEVER' or 'ALWAYS'.")
        }
        return ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED
    }
}
