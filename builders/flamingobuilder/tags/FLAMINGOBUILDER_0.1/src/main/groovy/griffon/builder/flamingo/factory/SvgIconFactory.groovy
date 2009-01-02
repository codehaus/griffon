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

package griffon.builder.flamingo.factory

import java.awt.Dimension
import org.jvnet.flamingo.svg.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class SvgIconFactory extends AbstractFactory {
    public Object newInstance( FactoryBuilderSupport builder, Object name, Object value, Map attributes )
            throws InstantiationException, IllegalAccessException {
      if( FactoryBuilderSupport.checkValueIsTypeNotString(value, name, SvgBatikIcon) ) {
         return value
      }

      if (value == null) {
         if (attributes.containsKey("url")) {
            value = attributes.remove("url")
            if (!(value instanceof URL)) {
               throw new RuntimeException("In $name url: attributes must be of type java.net.URL")
            }
         } else if (attributes.containsKey("file")) {
            value = attributes.remove("file")
            if (value instanceof File) {
               value = value.toURI().toURL()
            } else if (value instanceof String) {
               value = new File(value).toURI().toURL()
            } else {
               throw new RuntimeException("In $name file: attributes must be of type java.io.File or a string")
            }
         } else if (attributes.containsKey("inputStream")) {
            value = attributes.remove("inputStream")
            if (!(value instanceof InputStream)) {
               throw new RuntimeException("In $name inputStream: attributes must be of type java.io.InputStream")
            }
         }
      }

      // not else if so we can adjust for the case of file string where the file does not exist
      def resource = null
      if ((value == null) && (attributes.containsKey("resource"))) {
         resource = attributes.remove('resource')
      } else if ((value instanceof String) && !(new File(value).exists())) {
         resource = value
      }
      if (resource != null) {
         def klass = builder.context.owner
         def origValue = value
         if (attributes.containsKey("class")) {
               klass = attributes.remove("class")
         }
         if (klass == null) {
               klass = SvgIconFactory
         } else if (!(klass instanceof Class)) {
               klass = klass.class
         }
         // for now try URL approach.
         // we may need to extract the byte[] for some packaging cases
         value = klass.getResource(resource)
         if (value == null) {
               throw new RuntimeException("In $name the value argument '$origValue' does not refer to a file or a class resource")
         }
      }

      if (value == null) {
         throw new RuntimeException("$name has neither a value argument or one of url:, file:, or resource:")
      }

      def id = attributes.remove("initialDim")
      if( id == null ) id = new Dimension(48,48)
      if( id instanceof Number ) id = new Dimension(id.intValue(),id.intValue())
      if( id instanceof List ) id = id as Dimension
      if( (!id instanceof Dimension) ) throw new RuntimeException("In $name initialDim: attributes must be of type java.awt.Dimension")

      if( attributes.remove("zip") ) {
         return SvgBatikResizableIcon.getSvgzIcon( value, id )
      } else {
         return SvgBatikResizableIcon.getSvgIcon( value, id )
      }
   }

   public boolean isLeaf() {
      return true
   }
}