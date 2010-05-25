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

import org.pushingpixels.trident.ease.*
import griffon.builder.trident.impl.*

/**
 * @author Andres Almiray
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
