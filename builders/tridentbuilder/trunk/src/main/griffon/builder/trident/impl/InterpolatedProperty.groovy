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

package griffon.builder.trident.impl

import org.pushingpixels.trident.Timeline
import org.pushingpixels.trident.interpolator.PropertyInterpolator

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class InterpolatedProperty {
   final Object target
   final String property
   final def from
   final def to
   final PropertyInterpolator interpolator

   InterpolatedProperty( Object target, String property, from, to, PropertyInterpolator interpolator ) {
      this.target = target
      this.property = property
      this.from = from
      this.to = to
      this.interpolator = interpolator
   }

   public String toString() {
      return "[property: property, from: $from, to: $to, interpolator: $interpolator, target: $target]"
   }

   public void addToTimeline( Timeline timeline ) {
      def args = []
      if(target) args << target
      args << property
      if(from != null ) args << from
      args << to
      if(interpolator) args << interpolator

      from != null ? timeline.addPropertyToInterpolate(*args) : timeline.addPropertyToInterpolateTo(*args)
   }
}