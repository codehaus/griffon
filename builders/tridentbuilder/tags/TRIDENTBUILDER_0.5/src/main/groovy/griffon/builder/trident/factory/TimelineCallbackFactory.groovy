/*
 * Copyright 2009-2010 the original author or authors.
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
import griffon.builder.trident.impl.*

/**
 * @author Andres Almiray
 */
class TimelineCallbackFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if(value instanceof MutableTimelineCallback) {
         return value
      }
      return new MutableTimelineCallback(builder)
   }

   public boolean isHandlesNodeChildren() {
      return true
   }

   public boolean onNodeChildren( FactoryBuilderSupport builder, Object node, Closure childContent ) {
      node.closure = childContent
      return false
   }

   public void setParent(FactoryBuilderSupport builder, Object parent, Object child) {
      if(parent instanceof Timeline) {
         parent.addCallback(child)
      }
   }
}

/**
 * @author Andres Almiray
 */
class UIThreadTimelineCallbackFactory extends TimelineCallbackFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if(value instanceof MutableUIThreadTimelineCallback) {
         return value
      }
      return new MutableUIThreadTimelineCallback(builder)
   }
}