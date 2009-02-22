/*
 * Copyright 2008-2009 the original author or authors.
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

import javax.swing.JTree
import javax.swing.tree.TreeModel
import org.jvnet.flamingo.bcb.core.BreadcrumbTreeAdapterSelector

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class BreadcrumbTreeAdapterSelectorFactory extends AbstractBreadcrumbBarFactory {
    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, BreadcrumbTreeAdapterSelector) ) {
         return value
      }

      JTree tree = attributes.remove("tree")
      TreeModel treeModel = attributes.remove("treeModel")
      BreadcrumbTreeAdapterSelector.TreeAdapter treeAdapter = attributes.remove("treeAdapter")
      def rootVisible = attributes.remove("rootVisible") ?: true

      if( tree != null ) {
         if( treeAdapter != null ) {
            return new BreadcrumbTreeAdapterSelector(tree,treeAdapter)
         } else {
            return new BreadcrumbTreeAdapterSelector(tree)
         }
      } else if( treeModel != null && treeAdapter != null ) {
         return new BreadcrumbTreeAdapterSelector(treeModel,treeAdapter,rootVisible)
      } else {
         throw new RuntimeException("$name has neither a value argument or one of tree:, treeModel:, or treeAdapter:")
      }
   }
}