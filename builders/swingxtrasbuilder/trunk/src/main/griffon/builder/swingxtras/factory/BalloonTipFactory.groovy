/*
 * Copyright 2008-2009 the original author or authors.
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

import javax.swing.JComponent
import javax.swing.JTable
import javax.swing.Timer
import java.awt.Rectangle
import java.awt.Color
import java.awt.event.ActionListener
import java.awt.event.ComponentAdapter

import net.java.balloontip.*
import net.java.balloontip.positioners.*
import net.java.balloontip.styles.*
import net.java.balloontip.utils.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class BalloonTipFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      String text = null
      JComponent component = null

      if( value == null ) {
         text = attributes.remove("text")
         component = attributes.remove("component")
      } else if( value instanceof String ) {
         text = value
         component = attributes.remove("component")
      } else if( value instanceof JComponent ) {
         component = value
         text = attributes.remove("text")
      }

      if( text == null ) {
         throw new RuntimeException("In $name you must specify an attribute text: or supply a String as value")
      }
      if( component == null ) {
         throw new RuntimeException("In $name you must specify an attribute component: or supply a JComponent as value")
      }

      builder.context.widgetId = attributes.remove(builder.getAt(SwingBuilder.DELEGATE_PROPERTY_OBJECT_ID) ?: SwingBuilder.DEFAULT_DELEGATE_PROPERTY_OBJECT_ID)

      // force GStrings to be evalluated by calling toString()
      def balloonTip = newBalloonTip(builder, name, component, text.toString(), attributes)
      // make sure it starts invisible
      balloonTip.visible = false
      builder.context.balloonTip = balloonTip
      builder.setVariable(builder.context.widgetId, balloonTip)

      if( attributes.containsKey("hideAfter") ) {
         int hideAfter = attributes.remove("hideAfter")
         balloonTip.addComponentListener([
            componentShown: {
               def timer = new Timer(hideAfter, {
                  balloonTip.visible = false
               } as ActionListener)
               timer.repeats = false
               timer.start()
            }
         ] as ComponentAdapter)
      }

      // by returning something other than the balloonTip we prevent it from being added to
      // a parent container, we do this ony if the parent is a JComponent
      // at this stage builder.current is actually our parent
      return builder.current instanceof JComponent ? [:] : balloonTip
   }

   protected BalloonTip newBalloonTip( builder, name, component, text, attributes ) {
      BalloonTipStyle style = attributes.remove("style") ?: new RoundedBalloonStyle(5,5,Color.WHITE,Color.BLACK)
      BalloonTipPositioner positioner = attributes.remove("positioner")
      BalloonTip.Orientation alignment = attributes.remove("alignment") ?: BalloonTip.Orientation.LEFT_ABOVE
      BalloonTip.AttachLocation attachLocation = attributes.remove("attachLocation") ?: BalloonTip.AttachLocation.ALIGNED
      def horizontalOffset = attributes.remove("horizontalOffset")
      if( horizontalOffset == null ) horizontalOffset = 16
      def verticalOffset = attributes.remove("verticalOffset")
      if( verticalOffset == null ) verticalOffset = 20
      def useCloseButton = attributes.remove("useCloseButton")
      if( useCloseButton == null ) useCloseButton = true
      if( positioner == null ) {
         return new BalloonTip(component, text, style, alignment,
            attachLocation, horizontalOffset as int, verticalOffset as int, useCloseButton)
      } else {
         return new BalloonTip(component, text, style, positioner, useCloseButton)
      }
   }

   public boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object node, Map attributes ) {
      if( node instanceof Map ) {
         attributes.each { property, value ->
            InvokerHelper.setProperty(builder.context.balloonTip, property, value)
         }
         return false
      }
      return true
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if( child instanceof BalloonTipStyle ) {
         if( builder.parentContext?.balloonTip ) {
            builder.parentContext.balloonTip.style = child
         }
      }
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class CustomBalloonTipFactory extends BalloonTipFactory {
   protected BalloonTip newBalloonTip( builder, name, component, text, attributes ) {
      def offset = null

      if( attributes.containsKey("offset") ) {
         offset = attributes.remove("offset")
         if( offset instanceof List ) {
            if( offset.size() == 4 ) {
               offset = new Rectangle(
                  offset[0] as int,
                  offset[1] as int,
                  offset[2] as int,
                  offset[3] as int,
               )
            } else {
               throw new RuntimeException("In $name, when offset: is of type List it must have 4 elements")
            }
         } else if( !(offset instanceof Rectangle) ) {
            throw new RuntimeException("in $name value of offset: must be a Rectangle")
         }
      } else {
         throw new RuntimeException("in $name you must specify a value for offset:")
      }

      BalloonTipStyle style = attributes.remove("style") ?: new RoundedBalloonStyle(5,5,Color.WHITE,Color.BLACK)
      BalloonTipPositioner positioner = attributes.remove("positioner")
      BalloonTip.Orientation alignment = attributes.remove("alignment") ?: BalloonTip.Orientation.LEFT_ABOVE
      BalloonTip.AttachLocation attachLocation = attributes.remove("attachLocation") ?: BalloonTip.AttachLocation.ALIGNED
      def horizontalOffset = attributes.remove("horizontalOffset")
      if( horizontalOffset == null ) horizontalOffset = 16
      def verticalOffset = attributes.remove("verticalOffset")
      if( verticalOffset == null ) verticalOffset = 20
      def useCloseButton = attributes.remove("useCloseButton")
      if( useCloseButton == null ) useCloseButton = true

      if( positioner == null ) {
         return new CustomBalloonTip(component, text, offset, style, alignment,
            attachLocation, horizontalOffset as int, verticalOffset as int, useCloseButton)
      } else {
         return new CustomBalloonTip(component, text, offset, style, positioner, useCloseButton)
      }
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class TablecellBalloonTipFactory extends BalloonTipFactory {
   protected BalloonTip newBalloonTip( builder, name, component, text, attributes ) {
      if( !(Component instanceof JTable) ) {
         throw new RuntimeException("in $name component: must be a JTable")
      }

      def row = null
      def column = null

      if( attribute.containsKey("row") ) {
         row = attributes.remove("row")
      } else {
         throw new RuntimeException("in $name you must specify a value for row:")
      }
      if( attribute.containsKey("column") ) {
         row = attributes.remove("column")
      } else {
         throw new RuntimeException("in $name you must specify a value for column:")
      }

      BalloonTipStyle style = attributes.remove("style") ?: new RoundedBalloonStyle(5,5,Color.WHITE,Color.BLACK)
      BalloonTipPositioner positioner = attributes.remove("positioner")
      BalloonTip.Orientation alignment = attributes.remove("alignment") ?: BalloonTip.Orientation.LEFT_ABOVE
      BalloonTip.AttachLocation attachLocation = attributes.remove("attachLocation") ?: BalloonTip.AttachLocation.ALIGNED
      def horizontalOffset = attributes.remove("horizontalOffset")
      if( horizontalOffset == null ) horizontalOffset = 16
      def verticalOffset = attributes.remove("verticalOffset")
      if( verticalOffset == null ) verticalOffset = 20
      def useCloseButton = attributes.remove("useCloseButton")
      if( useCloseButton == null ) useCloseButton = true

      if( positioner == null ) {
         return new TablecellBalloonTip(component, text, row as int, column as int, style, alignment,
            attachLocation, horizontalOffset as int, verticalOffset as int, useCloseButton)
      } else {
         return new TablecellBalloonTip(component, text, row as int, column as int, style, positioner, useCloseButton)
      }
   }
}