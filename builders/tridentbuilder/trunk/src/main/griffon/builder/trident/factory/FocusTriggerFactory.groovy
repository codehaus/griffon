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

import javax.swing.JComponent
import org.pushingpixels.trident.Timeline
import org.hybird.animation.trident.triggers.FocusTrigger
import org.hybird.animation.trident.triggers.FocusTriggerEvent

/**
 * @author Andres Almiray
 */
class FocusTriggerFactory extends AbstractFactory {	
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if(!value || !(value instanceof JComponent)) {
	      throw new IllegalArgumentException("In $name value must a JComponent")
      }

      builder.context.autoReverse = attributes.remove('autoReverse') ?: false
      builder.context.event = attributes.remove('event') ?: FocusTriggerEvent.GAINED
      value
   }

   public void setParent(FactoryBuilderSupport builder, Object parent, Object node) {
      if(parent instanceof Timeline) {
	     FocusTriggerEvent event = parseFocusTriggerEvent(builder.context.event)
	     FocusTrigger.addTrigger(node, parent, event, builder.context.autoReverse)
      }
   }

   public boolean isLeaf() {
      true
   }

   private FocusTriggerEvent parseFocusTriggerEvent(event) {
	  if(!event) return FocusTriggerEvent.GAINED
      if(event instanceof FocusTriggerEvent) return event
      switch(String.valueOf(event).toUpperCase()) {
	     case 'LOST': return FocusTriggerEvent.LOST
	     case 'GAINED':
	     default:
	         return FocusTriggerEvent.GAINED
      }	
   }
}