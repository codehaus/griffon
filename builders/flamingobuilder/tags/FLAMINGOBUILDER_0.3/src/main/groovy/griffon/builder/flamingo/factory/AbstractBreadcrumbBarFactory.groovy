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

package griffon.builder.flamingo.factory

import org.jvnet.flamingo.bcb.BreadcrumbPathListener

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
abstract class AbstractBreadcrumbBarFactory extends AbstractFactory {
   public boolean isLeaf() {
      return true
   }

   public boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object node, Map attributes ) {
      // expose model events
      def eventHandler = attributes.remove("breadcrumbPathEvent")
      if( eventHandler ) {
         node.model.addPathListener(eventHandler as BreadcrumbPathListener)
      }
   }
}