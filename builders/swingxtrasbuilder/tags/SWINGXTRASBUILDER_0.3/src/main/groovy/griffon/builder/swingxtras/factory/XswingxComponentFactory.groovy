/*
 * Copyright 2008-2010 the original author or authors.
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

package griffon.builder.swingxtras.factory

import groovy.swing.SwingBuilder
import org.codehaus.groovy.runtime.InvokerHelper

import java.awt.Color
import java.awt.Font
import java.awt.Insets
import java.awt.Component
import java.awt.event.ActionListener
import javax.swing.JComponent
import javax.swing.JTextField
import javax.swing.JPopupMenu
import javax.swing.SwingConstants
import javax.swing.text.JTextComponent

import org.jdesktop.xswingx.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class SearchFieldFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      def node = new JXSearchField()
      if( value instanceof String ) {
         node.text = value
      } else if( value instanceof GString ) {
         node.text = value.toString()
      }
      return node
   }

   public boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object node, Map attributes ) {
      if( !(attributes.cancelAction instanceof ActionListener) ) {
         attributes.cancelAction = attributes.cancelAction as ActionListener
      }
      if( !attributes.remove("overrideCancel") ) {
         def defaultCancelAction = node.cancelAction
         def cancelAction = attributes.cancelAction
         def newCancelAction = { evt ->
            defaultCancelAction.actionPerformed(evt)
            cancelAction.actionPerformed(evt)
         }
         attributes.cancelAction = newCancelAction as ActionListener
      }
//       if( !(attributes.findAction instanceof ActionListener) ) {
//          attributes.findAction = attributes.findAction as ActionListener
//       }
      if( attributes.searchMode instanceof String ) {
         attributes.searchMode = JXSearchField.SearchMode."${attributes.searchMode.toUpperCase()}"
      }
      if( attributes.layoutStyle instanceof String ) {
         attributes.layoutStyle = JXSearchField.LayoutStyle."${attributes.layoutStyle.toUpperCase()}"
      }
      if( attributes.containsKey("promptFontStyle") ) {
         def fontStyle = attributes.remove("promptFontStyle")
         switch(fontStyle) {
            case Font.PLAIN:
            case ~/(?i:plain)/:
               fontStyle = Font.PLAIN
               break
            case Font.BOLD:
            case ~/(?i:bold)/:
               fontStyle = Font.BOLD
               break
            case Font.ITALIC:
            case ~/(?i:italic)/:
               fontStyle = Font.ITALIC
               break
            case Font.BOLD|Font.ITALIC:
            case ~/(?i:bold\|italic)/:
               fontStyle = Font.BOLD|Font.ITALIC
               break
            default:
               fontStyle = null
         }
         attributes.promptFontStyle = fontStyle
      }
      if( attributes.containsKey("margin") ) {
         def margin = attributes.remove("margin")
         if( margin instanceof List ) {
            if( margin.size() == 4 ) {
               margin = new Insets(
                  margin[0] as int,
                  margin[1] as int,
                  margin[2] as int,
                  margin[3] as int,
               )
            } else {
               throw new RuntimeException("In ${builder.currentName}, when margin: is of type List it must have 4 elements")
            }
         } else if( margin instanceof Number ) {
            margin = new Insets(
               margin.intValue(),
               margin.intValue(),
               margin.intValue(),
               margin.intValue(),
            )
         } else if( !(outerMargin instanceof Insets) ) {
            throw new RuntimeException("in ${builder.currentName} value of margin: must be of type Insets")
         }
         attributes.margin = margin
      }

      return true
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if( child instanceof JPopupMenu ) {
         parent.findPopupMenu = child
      }
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class PromptSupportFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      if( value == null ) {
         value = attributes.remove("component")
         if( value == null ) {
            throw new RuntimeException("In $name you must define either a component: attribute or set a JTextComponent as value")
         } else if( !(value instanceof JTextComponent) ) {
            throw new RuntimeException("In $name value of component: must be a JTextComponent")
         }
      } else if( !(value instanceof JTextComponent) ) {
         throw new RuntimeException("In $name value must be a JTextComponent")
      }

      if( attributes.containsKey("background") ) {
         Color background = attributes.remove("background")
         PromptSupport.setBackground(background, value)
      }
      if( attributes.containsKey("foreground") ) {
         Color foreground = attributes.remove("foreground")
         PromptSupport.setForeground(foreground, value)
      }
      if( attributes.containsKey("prompt") ) {
         String prompt = attributes.remove("prompt")
         PromptSupport.setPrompt(prompt, value)
      }
      if( attributes.containsKey("fontStyle") ) {
         def fontStyle = attributes.remove("fontStyle")
         switch(fontStyle) {
            case Font.PLAIN:
            case ~/(?i:plain)/:
               fontStyle = Font.PLAIN
               break
            case Font.BOLD:
            case ~/(?i:bold)/:
               fontStyle = Font.BOLD
               break
            case Font.ITALIC:
            case ~/(?i:italic)/:
               fontStyle = Font.ITALIC
               break
            case Font.BOLD|Font.ITALIC:
            case ~/(?i:bold\|italic)/:
               fontStyle = Font.BOLD|Font.ITALIC
               break
            default:
               fontStyle = null
         }
         PromptSupport.setFontStyle(fontStyle as Integer, value)
      }
      if( attributes.containsKey("focusBehavior") ) {
         def focusBehavior = attributes.remove("focusBehavior")
         switch(focusBehavior) {
            case PromptSupport.FocusBehavior:
               break
            case ~/(?i:highlight)/:
               focusBehavior = PromptSupport.FocusBehavior.HIGHLIGHT_PROMPT
               break
            case ~/(?i:show)/:
               focusBehavior = PromptSupport.FocusBehavior.SHOW_PROMPT
               break
            case ~/(?i:hide)/:
            default:
               focusBehavior = PromptSupport.FocusBehavior.HIDE_PROMPT
         }
         PromptSupport.setFocusBehavior(focusBehavior, value)
      }

      if( attributes ) {
         throw new RuntimeException("$name does not know what to do with additional attributes: ${attributes.keySet()}")
      }

      return [:] // make null return warning go away
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class BuddySupportFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      if( value == null ) {
         value = attributes.remove("textField")
         if( value == null ) {
            throw new RuntimeException("In $name you must define either a textField: attribute or set a JTextField as value")
         } else if( !(value instanceof JTextField) ) {
            throw new RuntimeException("In $name value of textField: must be a JTextField")
         }
      } else if( !(value instanceof JTextField) ) {
         throw new RuntimeException("In $name value must be a JTextField")
      }

      def buddy = null
      if( attributes.containsKey("buddy") ) {
         buddy = attributes.remove("buddy")
         if( !(buddy instanceof Component) ) {
            throw new RuntimeException("In $name value of buddy: must be a Component")
         }
      } else {
         throw new RuntimeException("In $name you must define a value for buddy:")
      }

//       String mode = attributes.remove("mode") ?: "set"
//       switch(mode) {
//          case ~/(?:add)/:
//          case ~/(?:set)/:
//             break
//          default:
//             throw new RuntimeException("In $name value of mode: must be either 'set' or 'add'.")
//       }

      def position = attributes.remove("position") ?: SwingConstants.LEFT
      switch(position) {
         case BuddySupport.Position.LEFT:
         case SwingConstants.LEFT:
         case ~/(?:left)/:
//             if( !BuddySupport.buddies(BuddySupport.Position.LEFT,value) ) mode = "add"
            position = BuddySupport.Position.LEFT
            break
         case BuddySupport.Position.RIGHT:
         case SwingConstants.RIGHT:
         case ~/(?:right)/:
//             if( !BuddySupport.buddies(BuddySupport.Position.RIGHT,value) ) mode = "add"
            position = BuddySupport.Position.RIGHT
            break
      }
//       BuddySupport."${mode.toLowerCase()}"(mode == "add"?buddy:[buddy],position,value)
      BuddySupport.add(buddy,position,value)

      if( attributes.containsKey("outerMargin") ) {
         def outerMargin = attributes.remove("outerMargin")
         if( outerMargin instanceof List ) {
            if( outerMargin.size() == 4 ) {
               outerMargin = new Insets(
                  outerMargin[0] as int,
                  outerMargin[1] as int,
                  outerMargin[2] as int,
                  outerMargin[3] as int,
               )
            } else {
               throw new RuntimeException("In $name, when outerMargin: is of type List it must have 4 elements")
            }
         } else if( outerMargin instanceof Number ) {
            outerMargin = new Insets(
               outerMargin.intValue(),
               outerMargin.intValue(),
               outerMargin.intValue(),
               outerMargin.intValue(),
            )
         } else if( !(outerMargin instanceof Insets) ) {
            throw new RuntimeException("in $name value of outerMargin: must be of type Insets")
         }
         BuddySupport.setOuterMargin(value, outerMargin)
      }

      if( attributes ) {
         throw new RuntimeException("$name does not know what to do with additional attributes: ${attributes.keySet()}")
      }

      return [:] // make null return warning go away
   }
}
