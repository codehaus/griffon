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
import griffon.builder.trident.impl.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class TimelineScenarioFactory extends AbstractFactory {
   private final scenarioClass

   TimelineScenarioFactory( Class scenarioClass ) {
      this.scenarioClass = scenarioClass
   }

   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( value != null && scenarioClass.isAssignableFrom(value.class) ) {
         return value
      }

      FactoryBuilderSupport.checkValueIsNull(value, name)
      return scenarioClass.newInstance()
   }

   public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
      if( child instanceof Timeline || child instanceof TimelineRunnable ) {
         parent.addScenarioActor(child)
      } else if( child instanceof TimelineScenarioCallback ) {
         parent.addCallback(child)
      }
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class TimelineRunnableFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( value instanceof TimelineRunnable ) {
         return value
      }
      return new MutableTimelineRunnable(builder)
   }

   public boolean isHandlesNodeChildren() {
      return true
   }

   public boolean onNodeChildren( FactoryBuilderSupport builder, Object node, Closure childContent ) {
      node.closure = childContente
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
class TimelineScenarioCallbackFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( value instanceof TimelineCallback ) {
         return value
      }
      return new MutableTimelineScenarioCallback(builder)
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
}