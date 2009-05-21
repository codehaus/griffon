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

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class TridentFactoryUtils {
   static processTimelineKind(builder, name, attributes) {
      if( attributes.containsKey("kind") ){
         def kind = attributes.kind
         switch(kind) {
            case TimelineKind:
               break
            case String:
            case GString:
               kind = kind.toString().toUpperCase().replaceAll(" ","_")
               kind = TimelineKind."$kind"
               break
         }
         attributes.kind = kind
      }
   }

   static processRepeatBehavior(builder, name, attributes) {
      if( attributes.containsKey("repeatBehavior") ){
         def repeatBehavior = attributes.repeatBehavior
         switch(repeatBehavior) {
            case Timeline.RepeatBehavior:
               break
            case String:
            case GString:
               repeatBehavior = repeatBehavior.toString().toUpperCase().replaceAll(" ","_")
               repeatBehavior = Timeline.RepeatBehavior."$repeatBehavior"
               break
         }
         attributes.repeatBehavior = repeatBehavior
      }
   }
}