/*
 * Copyright 2008-2010 the original author or authors.
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

import java.awt.Color
import org.jvnet.flamingo.ribbon.*
import org.jvnet.flamingo.common.icon.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class MutableRibbonContextualTaskGroup extends RibbonContextualTaskGroup {
   private static final RibbonTask DEFAULT_TASK = new MutableRibbonTask("")

   MutableRibbonContextualTaskGroup(String title, Color hueColor) {
      super(title, hueColor, [DEFAULT_TASK] as RibbonTask[])
   }

   void addTask( RibbonTask task ) {
      if( tasks[0] == DEFAULT_TASK ) {
         tasksField.clear()
      }
      tasksField.add(task)
   }

   private getTasksField() {
      def field = this.class.superclass.getDeclaredField("tasks")
      field.accessible = true
      return field.get(this)
   }
}