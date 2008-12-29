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
import com.explodingpixels.macwidgets.SourceListClickListener
import com.explodingpixels.macwidgets.SourceListSelectionListener

import groovy.swing.SwingBuilder
import org.codehaus.groovy.runtime.InvokerHelper

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class SourceListFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      builder.context.sourceListId = attributes.remove(builder.getAt(SwingBuilder.DELEGATE_PROPERTY_OBJECT_ID) ?: SwingBuilder.DEFAULT_DELEGATE_PROPERTY_OBJECT_ID)

      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, SourceList) ) {
         builder.context.sourceListModel = sourceList.model
         builder.context.sourceList = value
         builder.setVariable(builder.context.sourceListId,value)
         return value
      }

      def model = new SourceListModel()
      builder.context.sourceListModel = model
      def sourceList = new SourceList(model)
      builder.context.sourceList = sourceList
      builder.setVariable(builder.context.sourceListId,sourceList)
      return sourceList.component
   }

   public boolean onHandleNodeAttributes( FactoryBuilderSupport builder, Object node, Map attributes ) {
      def sourceList = builder.context.sourceList
      def sourceListCategoryClicked = attributes.remove("sourceListCategoryClicked")
      def sourceListItemClicked = attributes.remove("sourceListItemClicked")
      def sourceListItemSelected = attributes.remove("sourceListItemSelected")
      if( sourceListCategoryClicked ) {
         sourceList.addSourceListClickListener(sourceListCategoryClicked as SourceListClickListener)
      }
      if( sourceListItemClicked ) {
         sourceList.addSourceListClickListener(sourceListItemClicked as SourceListClickListener)
      }
      if( sourceListItemSelected ) {
         sourceList.addSourceListSelectionListener(sourceListItemSelected as SourceListSelectionListener)
      }
      attributes.each { property, value ->
         InvokerHelper.setProperty(sourceList, property, value)
      }
      return false
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if( child instanceof SourceListCategory ) {
         builder.context.sourceListModel.addCategory(child)
      } else {
         throw new RuntimeException("sourceList accepts sourceListCategory() as child content only.")
      }
   }
}