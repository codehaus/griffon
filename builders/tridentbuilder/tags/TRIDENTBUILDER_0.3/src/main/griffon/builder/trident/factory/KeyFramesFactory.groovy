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
import org.pushingpixels.trident.interpolator.*
import griffon.builder.trident.impl.*

/**
 * @author Andres Almiray
 */
class KeyFramesFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      PropertyInterpolator interpolator = attributes.remove("interpolator")
      [interpolator: interpolator, keyframes: []]
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if(child instanceof KeyFrame) parent.keyframes << child
   }

   public void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node ) {
      if(parent instanceof InterpolatedProperty) {
          def offsets = []
          def values = []
          def eases = []
          node.keyframes.each {
             offsets << it.offset
             values << it.value
             if(it.ease) eases << it.ease
          }
          KeyValues keyValues = node.interpolator ? KeyValues.create(node.interpolator, *values) : KeyValues.create(*values)
          KeyFrames keyFrames = eases ? new KeyFrames(keyValues, new KeyTimes(*offsets), *eases) :
                                        new KeyFrames(keyValues, new KeyTimes(*offsets))
          parent.keyFrames = keyFrames
      }
   }
}