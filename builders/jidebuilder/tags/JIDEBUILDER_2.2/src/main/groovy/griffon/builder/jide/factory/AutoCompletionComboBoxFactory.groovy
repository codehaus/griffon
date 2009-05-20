/*
 * Copyright 2007-2008 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.builder.jide.factory

import com.jidesoft.swing.AutoCompletionComboBox

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
class AutoCompletionComboBoxFactory extends AbstractJideComponentFactory {
   public AutoCompletionComboBoxFactory() {
      super( AutoCompletionComboBox )
   }

   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map properties) throws InstantiationException, IllegalAccessException {
      FactoryBuilderSupport.checkValueIsNull(value, name)
      Object items = properties.remove("items")
      if( items instanceof Vector){
         return new AutoCompletionComboBox((Vector) items)
      }else if( items instanceof List ){
         List list = (List) items
         return new AutoCompletionComboBox(list.toArray())
      }else if( items instanceof Object[] ){
         return new AutoCompletionComboBox((Object[]) items )
      }else{
         return new AutoCompletionComboBox()
      }
   }
}