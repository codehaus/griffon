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

import java.awt.Color
import org.pushingpixels.trident.*
import org.pushingpixels.trident.callback.*
import org.pushingpixels.trident.ease.*
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
         if( !attributes.containsKey("target") ) {
            throw new RuntimeException("In $name you must specify either a value or a target: property.")
         }
         value = attributes.remove("target")
      }

      TridentFactoryUtils.processTimelineKind(builder, name, attributes)
      TimelineKind kind = attributes.remove("kind")
      kind ? new Timeline(kind,value) : new Timeline(value)
   }

   public boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object node, Map attributes ) {
      if( attributes.containsKey("loopCount") && attributes.containsKey("repeatBehavior") ) {
         def loopCount = attributes.remove("loopCount")
         TridentFactoryUtils.processRepeatBehavior(builder, builder.currentName, attributes)
         Timeline.RepeatBehavior repeatBehavior = attributes.remove("repeatBehavior")
         node.loop(loopCount, repeatBehavior)
      }
      builder.context.start = attributes.remove("start")
      return true
   }

   public void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node ) {
      if(builder.context.start) {
         node.play()
      }
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
      if( !value ) {
         if( !attributes.remove("name") ) {
            throw new RuntimeException("In $name you must specify either a value or a name: property.")
         }
         value = attributes.remove("name")
      }
      value = value.toString() // trigger GString conversion
      if( attributes.from == null ) {
         throw new RuntimeException("In $name you must specify a value for from: property.")
      }
      if( attributes.to == null ) {
         throw new RuntimeException("In $name you must specify a value for to: property.")
      }
      return new InterpolatedProperty(value, attributes.remove("from"), attributes.remove("to"))
   }

   public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
      if( parent instanceof Timeline ) {
         if( child.from instanceof Color && child.to instanceof Color ) {
            parent.addPropertyToInterpolate(child.name, child.from, child.to)
         } else if( child.from instanceof Number && child.to instanceof Number ) {
            parent.addPropertyToInterpolate(child.name, child.from.floatValue(), child.to.floatValue())
         } else {
            throw new UnsupportedOperationException("Cannot add interpolated property $child")
         }
      }
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
      return new Spline(amount)
   }
}