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

/**
 * @author Andres Almiray
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