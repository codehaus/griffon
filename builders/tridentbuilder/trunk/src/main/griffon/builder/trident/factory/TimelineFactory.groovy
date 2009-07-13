/*
 * Copyright 2009 the original author or authors.
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

package griffon.builder.trident.factory

import org.pushingpixels.trident.*
import org.pushingpixels.trident.callback.*
import org.pushingpixels.trident.ease.*
import org.pushingpixels.trident.swing.*
import org.pushingpixels.trident.interpolator.*
import griffon.builder.trident.impl.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class TimelineFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( value instanceof Timeline ) {
         return value
      }

      if( !value ) {
         value = attributes.remove("target")
      }

      return new Timeline(value)
   }

   public boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object node, Map attributes ) {
      builder.context.start = attributes.remove("start")
      builder.context.loop = attributes.remove("loop")
      if(builder.context.start && builder.context.loop) {
         throw new IllegalArgumentException("You may specify either start: or loop: but not both")
      }
      return true
   }

   public void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node ) {
      if(builder.context.start) {
         node.play()
      } else if(builder.context.loop) {
         switch(builder.context.loop) {
            case true:
            case ~/(?i:loop)/:
            case Timeline.RepeatBehavior.LOOP:
               node.playLoop(Timeline.RepeatBehavior.LOOP)
               break
            case ~/(?i:reverse)/:
            case Timeline.RepeatBehavior.REVERSE:
               node.playLoop(Timeline.RepeatBehavior.REVERSE)
               break
         }
      }
   }
}


/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class SwingRepaintTimelineFactory extends TimelineFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( value instanceof SwingRepaintTimeline ) {
         return value
      }

      if( !value ) {
         value = attributes.remove("target")
      }

      return new SwingRepaintTimeline(value, attributes.remove("zone")/*Rectangle*/)
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class TimelineCallbackFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( value instanceof TimelineCallback ) {
         return value
      }
      return new MutableTimelineCallback(builder)
   }

   public boolean isHandlesNodeChildren() {
      return true
   }

   public boolean onNodeChildren( FactoryBuilderSupport builder, Object node, Closure childContent ) {
      node.delegate = childContent.delegate
      childContent.delegate = node
      childContent()
      return false
   }

   public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
      if( parent instanceof Timeline ) {
         parent.addCallback(child)
      }
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class InterpolatedPropertyFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( value instanceof InterpolatedProperty ) {
         return value
      }

      def target = value
      def property = attributes.remove("property")
      if( !property ) {
         target = null
         property = value?.toString() ?: property.toString()
      }
      if( !property ) throw new RuntimeException("In $name you must specify either a value or a property: attribute.")
      def from = attributes.remove("from")
      def to = attributes.remove("to")
      if( to == null ) throw new RuntimeException("In $name you must specify a value for the to: attribute")
      def interpolator = attributes.remove("interpolator")

      return new InterpolatedProperty(target, property, from, to, interpolator)
   }

   public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
      if( parent instanceof Timeline ) {
          child.addToTimeline(parent)
      }
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class KeyFramesFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      def property = attributes.remove("property")
      if( !property ) {
         property = value?.toString() ?: property.toString()
      }
      if( !property ) throw new RuntimeException("In $name you must specify either a value or a property: attribute.")
      PropertyInterpolator interpolator = attributes.remove("interpolator")
      [property: property, interpolator: interpolator, keyframes: []]
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if(child instanceof KeyFrame) parent.keyframes << child
   }

   public void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node ) {
      if(parent instanceof Timeline) {
          def offsets = []
          def values = []
          def eases = []
          node.keyframes.each {
             offsets << it.offset
             values << it.value
             if(it.ease) eases << it.ease
          }
          KeyValues keyValues = node.interpolator ? KeyValues.create(node.interpolator, *values) : KeyValues.create(*values)
          eases ? parent.addPropertyToInterpolate(node.property, new KeyFrames(keyValues, new KeyTimes(*offsets), *eases)) :
                  parent.addPropertyToInterpolate(node.property, new KeyFrames(keyValues, new KeyTimes(*offsets)))
      }
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class KeyFrameFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      FactoryBuilderSupport.checkValueIsNull(value, name)
      def offset = attributes.remove("offset")
      value = attributes.remove("value")
      TimelineEase ease = attributes.remove("ease")
      if( offset == null ) {
         throw new RuntimeException("In $name you must specify a value for offset: attribute.")
      }
      return new KeyFrame(offset, value, ease)
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class EaseFactory extends AbstractFactory {
   Class easeClass

   EaseFactory( Class easeClass ) {
      this.easeClass = easeClass
   }

   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, easeClass) ) {
          return value
      }
      easeClass.newInstance()
   }

   public boolean isLeaf() {
      true
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class SplineEaseFactory extends EaseFactory {
   SplineEaseFactory() {
      super(Spline)
   }

   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, Spline) ) {
          return value
      }
      if( value == null ) {
         if( !attributes.containsKey("amount") ) {
            throw new RuntimeException("In $name you must specify either a value or an amount: property.")
         }
         value = attributes.remove("amount")
      }
      if( value < 0f && value > 1f ) {
         throw new RuntimeException("In $name amount must be in the range [0..1]")
      }
      return new Spline(value)
   }
}