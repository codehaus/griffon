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

import javax.swing.filechooser.FileSystemView
import org.jvnet.flamingo.bcb.core.BreadcrumbFileSelector

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class BreadcrumbFileSelectorFactory extends AbstractBreadcrumbBarFactory {
    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, BreadcrumbFileSelector) ) {
         return value
      }

      def useNativeIcons = attributes.remove("useNativeIcons")
      def fileSystemView = attributes.remove("fileSystemView")
      if( useNativeIcons != null ) {
         if( fileSystemView != null ) {
            return new BreadcrumbFileSelector(fileSystemView,useNativeIcons)
         } else {
            return new BreadcrumbFileSelector(useNativeIcons)
         }
      } else {
         return new BreadcrumbFileSelector()
      }
   }
}