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

package griffon.builder.macwidgets.factory

import com.explodingpixels.macwidgets.MacWidgetFactory
import com.explodingpixels.macwidgets.SourceList
import com.explodingpixels.macwidgets.SourceListModel
import com.explodingpixels.macwidgets.SourceListItem
import com.explodingpixels.macwidgets.SourceListCategory
import com.explodingpixels.macwidgets.SourceListClickListener
import com.explodingpixels.macwidgets.SourceListSelectionListener
import com.explodingpixels.macwidgets.SourceListControlBar
import com.explodingpixels.macwidgets.SourceListContextMenuProvider

import javax.swing.JComponent
import groovy.swing.SwingBuilder
import groovy.swing.factory.LayoutFactory
import org.codehaus.groovy.runtime.InvokerHelper

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class SourceListFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      builder.context.widgetId = attributes.remove(builder.getAt(SwingBuilder.DELEGATE_PROPERTY_OBJECT_ID) ?: SwingBuilder.DEFAULT_DELEGATE_PROPERTY_OBJECT_ID)
      builder.context.constraints = attributes[LayoutFactory.DELEGATE_PROPERTY_CONSTRAINT] ?: attributes[LayoutFactory.DEFAULT_DELEGATE_PROPERTY_CONSTRAINT]

      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, SourceList) ) {
         builder.context.sourceListModel = sourceList.model
         builder.context.sourceList = value
         builder.setVariable(builder.context.widgetId,value)
         return value
      }

      def model = new SourceListModel()
      builder.context.sourceListModel = model
      def sourceList = new SourceList(model)
      builder.context.sourceList = sourceList
      builder.setVariable(builder.context.widgetId,sourceList)
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

      ["focusable","selectedItem","sourceListContextMenuProvider"].each { property ->
         def value = attributes.remove(property)
         if( value != null ) sourceList."$property" = value
      }

      return true
   }

   public void setChild(FactoryBuilderSupport builder, Object parent, Object child) {
      if( child instanceof SourceListCategory ) {
         builder.context.sourceListModel.addCategory(child)
      } else if( child instanceof SourceListControlBar ) {
         builder.parentContext.sourceList.installSourceListControlBar(child)
         builder.parentContext.sourceListControlBar = child
      } else if( child instanceof SourceListContextMenuProvider ) {
         builder.parentContext.sourceList.sourceListContextMenuProvider = child
      } else {
         throw new RuntimeException("sourceList accepts sourceListCategory, sourceListControlBar and sourceListContextMenuProvider as child content only.")
      }
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class SourceListSplitPaneFactory extends AbstractFactory {
   public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      builder.context.widgetId = attributes.remove(builder.getAt(SwingBuilder.DELEGATE_PROPERTY_OBJECT_ID) ?: SwingBuilder.DEFAULT_DELEGATE_PROPERTY_OBJECT_ID)
      builder.context.constraints = attributes[LayoutFactory.DELEGATE_PROPERTY_CONSTRAINT] ?: attributes[LayoutFactory.DEFAULT_DELEGATE_PROPERTY_CONSTRAINT]

      return [:]
   }

   public void setChild( FactoryBuilderSupport builder, Object parent, Object child ) {
      if( builder.context.sourceList && !parent.sourceList ) {
         parent.sourceList = builder.context.sourceList
      } else if( child instanceof JComponent ) {
         parent.component = child
      }

      if( parent.sourceList && builder.context.sourceListControlBar ) {
         parent.sourceListControlBar = builder.context.sourceListControlBar
      }
   }

   public void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node ) {
      super.onNodeCompleted(builder, parent, node)
      if(!node || !node.sourceList || !node.component) {
         throw new RuntimeException("In ${builder.currentName} you need to specify a sourceList and another JComponent as children.")
      }

      def splitPane = MacWidgetFactory.createSplitPaneForSourceList(
        node.remove("sourceList"),
        node.remove("component")
      )
      def sourceListControlBar = node.remove("sourceListControlBar")
      if( sourceListControlBar ) {
         sourceListControlBar.installDraggableWidgetOnSplitPane(splitPane)
      }
      node.each { property, value ->
         InvokerHelper.setProperty(splitPane, property, value)
      }
      if( builder.parentFactory ) {
         builder.parentFactory.setChild(builder,parent,splitPane)
      }
      if( builder.context.widgetId ) {
         builder.setVariable(builder.context.widgetId,splitPane)
      }
   }
}