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

package griffon.builder.flamingo.impl

import org.jvnet.flamingo.ribbon.*
import org.jvnet.flamingo.common.icon.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class MutableRibbonTask extends RibbonTask {
   private static final JRibbonBand DEFAULT_BAND = new JRibbonBand("", new EmptyResizableIcon(16))

   MutableRibbonTask(String title, AbstractRibbonBand<?>... bands) {
      super(title, bands)
   }

   MutableRibbonTask( String title ) {
      super(title, [DEFAULT_BAND] as AbstractRibbonBand[])
   }

   void addBand( JRibbonBand band ) {
      if( bands[0] == DEFAULT_BAND ) {
         bandsField.clear()
      }
      bandsField.add(band)
   }

   private getBandsField() {
      def field = this.class.superclass.getDeclaredField("bands")
      field.accessible = true
      return field.get(this)
   }
}