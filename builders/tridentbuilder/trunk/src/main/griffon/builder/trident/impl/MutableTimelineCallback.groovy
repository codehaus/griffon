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

import org.pushingpixels.trident.Timeline.TimelineState
import org.pushingpixels.trident.callback.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class MutableTimelineCallback implements TimelineCallback {
   Closure timelineStateChangedClosure
   Closure timelinePulseClosure

   private delegate
   private final builder

   MutableTimelineCallback( FactoryBuilderSupport builder ) {
      this.builder = builder
   }

   public void onTimelineStateChanged(TimelineState oldState,
         TimelineState newState, float durationFraction,
         float timelinePosition) {
      if(timelineStateChangedClosure) timelineStateChangedClosure(oldState, newState, durationFraction, timelinePosition)
   }

   public void onTimelinePulse(float durationFraction, float timelinePosition) {
      if(timelinePulseClosure) timelinePulseClosure(durationFraction, timelinePosition)
   }

   public void setTimelineStateChanged( Closure closure ) {
      timelineStateChanged(closure)
   }

   public void timelineStateChanged( Closure closure ) {
      timelineStateChangedClosure = closure
      timelineStateChangedClosure.delegate = this
   }

   public void setTimelinePulse( Closure closure ) {
      timelinePulse(closure)
   }

   public void timelinePulse( Closure closure ) {
      timelinePulseClosure = closure
      timelinePulseClosure.delegate = this
   }

   def methodMissing( String name, Object value ) {
      // try the builder first
      try {
         return builder."$name"(value)
      }catch( MissingMethodException mme ) {
         // try original delegate if != builder
         if( delegate && delegate != builder ){
            return delegate."$name"(value)
         }else{
            throw mme
         }
      }
   }

   def propertyMissing( String name ) {
      // try the builder first
      try {
         return builder."$name"
      }catch( MissingPropertyException mpe ) {
         // try original delegate if != builder
         if( delegate && delegate != builder ){
            return delegate."$name"
         }else{
            throw mpe
         }
      }
   }

   def propertyMissing( String name, Object value ) {
      // try the builder first
      try {
         builder."$name" = value
         return
      }catch( MissingMethodException mpe ) {
         // try original delegate if != builder
         if( delegate && delegate != builder ){
            delegate."$name" = value
            return
         }else{
            throw mpe
         }
      }
   }
}