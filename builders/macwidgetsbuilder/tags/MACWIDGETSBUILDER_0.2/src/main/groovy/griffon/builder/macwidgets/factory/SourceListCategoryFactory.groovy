/*
 * Copyright 2008 the original author or authors.
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

package griffon.builder.macwidgets.factory

import com.explodingpixels.macwidgets.SourceList
import com.explodingpixels.macwidgets.SourceListModel
import com.explodingpixels.macwidgets.SourceListItem
import com.explodingpixels.macwidgets.SourceListCategory

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class SourceListCategoryFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( builder?.parentContext?.sourceListModel ) {
         builder.context.sourceListModel = builder?.parentContext?.sourceListModel
      }
      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, SourceListCategory) ) {
         return value
      }

      if( value instanceof String ) {
         return new SourceListCategory(value)
      }
      throw new RuntimeException("in $name value must be of type java.lang.String")
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if( child instanceof SourceListItem ) {
         builder.context.sourceListModel.addItemToCategory(child,parent)
      } else {
         throw new RuntimeException("sourceListCategory accepts sourceListItem() as child content only.")
      }
   }
}