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

/**
 * @author Andres Almiray
 */
class TimelineFactory extends AbstractFactory {
   Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( value instanceof Timeline ) {
         return value
      }

      if( !value ) {
         value = attributes.remove("target")
      }

      return new Timeline(value)
   }

   boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object node, Map attributes ) {
      builder.context.start = attributes.remove("start") ?: builder.context.start
      builder.context.reverse = attributes.remove("reverse") ?: builder.context.reverse
      builder.context.loop = attributes.remove("loop") ?: builder.context.loop
      builder.context.skip = attributes.remove("skip") ?: builder.context.skip
      builder.context.count = attributes.remove("count") ?: builder.context.count

      int flags = builder.context.start != null ? 1 : 0
      flags += builder.context.loop != null ? 1 : 0
      flags += builder.context.reverse != null ? 1 : 0

      if(flags > 1) {
         throw new IllegalArgumentException("You may specify one of start:, loop:, or reverse:")
      }
      return true
   }	

   void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node ) {
	  def skip = builder.context.skip ?: 0
	  if(builder.context.start) {
		  node.playSkipping(parseLong(skip))	
	  } else if(builder.context.reverse) {
	      node.playReverseSkipping(parseLong(skip))
	  } else if(builder.context.loop) {
          Timeline.RepeatBehavior repeatBehavior = getRepeatBehavior(builder.context.loop)
          def count = builder.context.count ?: -1
          node.playLoopSkipping(parseInt(count), repeatBehavior, parseLong(skip))
      }
   }

   private int parseInt(value) {
       if(value instanceof Number) return value.intValue()
       return Integer.parseInt(value)	
   }

   private long parseLong(value) {
       if(value instanceof Number) return value.longValue()
       return Long.parseLong(value)	
   }

   private static Timeline.RepeatBehavior getRepeatBehavior(value) {
      switch(value) {
         case ~/(?i:reverse)/:
         case Timeline.RepeatBehavior.REVERSE:
            return Timeline.RepeatBehavior.REVERSE
         case true:
         case ~/(?i:loop)/:
         case Timeline.RepeatBehavior.LOOP:
         default:
            return Timeline.RepeatBehavior.LOOP
      }	
   }
}