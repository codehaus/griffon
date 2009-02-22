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

import java.awt.Dimension
import java.awt.Image
import org.jvnet.flamingo.common.*
import org.jvnet.flamingo.common.icon.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class FlamingoFactoryUtils {
   static translateCommandButtonConstants( Map attributes ) {
      [ commandButtonKind: JCommandButton.CommandButtonKind,
        commandButtonPopupOrientationKind: JCommandButton.CommandButtonPopupOrientationKind,
        commandButtonDisplayState: CommandButtonDisplayState,
        stripOrientation: JCommandButtonStrip.StripOrientation ].each { name, host ->
         translateFlamingoConstant(name, host, attributes)
      }
   }

   static translateFlamingoConstant( String name, Class host, Map attributes ) {
      def value = attributes[name]
      if( value && value instanceof String ) {
         value = value.toUpperCase().replaceAll(" ","_")
         value = host."$value"
         attributes[name] = value
      }
   }

   // returns URL || InputStream
   static processIconAttributes( builder, name, value, attributes ) {
      if (!value) {
         if (attributes.containsKey("url")) {
            value = attributes.remove("url")
            if (!(value instanceof URL)) {
               throw new RuntimeException("In $name url: attributes must be of type java.net.URL")
            }
         } else if (attributes.containsKey("file")) {
            value = attributes.remove("file")
            if (value instanceof File) {
               value = value.toURI().toURL()
            } else if (value instanceof String || value instanceof GString) {
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
         if(value) return value
      }

      // not else if so we can adjust for the case of file string where the file does not exist
      def resource = null
      if ((!value) && (attributes.containsKey("resource"))) {
         resource = attributes.remove('resource')
      } else if ((value instanceof String || value instanceof GString) && !(new File(value).exists())) {
         resource = value
      }
      if (resource != null) {
         def klass = builder.context.owner
         def origValue = value
         if (attributes.containsKey("class")) {
            klass = attributes.remove("class")
         }
         if (klass == null) {
            klass = FlamingoFactoryUtils
         } else if (!(klass instanceof Class)) {
            klass = klass.class
         }
         // for now try URL approach.
         // we may need to extract the byte[] for some packaging cases
         value = klass.getResource(resource)
         if (!value) {
            throw new RuntimeException("In $name the value argument '$origValue' does not refer to a file or a class resource")
         }
      }

      if (!value) {
         throw new RuntimeException("$name has neither a value argument or one of url:, file:, inputStream:, or resource:")
      }

      return value
   }

   static processIconInitialDimAttribute( name, attributes ) {
      def id = attributes.remove("initialDim")
      if( id == null ) return new Dimension(32,32)
      else if( id instanceof Number ) return new Dimension(id.intValue(),id.intValue())
      else if( id instanceof List ) return (id as Dimension)
      else if( id instanceof Dimension) return id
      throw new RuntimeException("In $name initialDim: attribute must be of type Dimension, Number or a two element List.")
   }

   static createIcon( builder, name, value, attributes ) {
      def icon
      Dimension initialDim = processIconInitialDimAttribute(name, attributes)
      if( attributes.containsKey("icon") ) {
         icon = attributes.remove("icon")
         if( !(icon instanceof ResizableIcon) ) {
            throw new RuntimeException("In $name value of icon: must be of type ResizableIcon.")
         }
      } else if( attributes.containsKey("image") ) {
         def image = attributes.remove("image")
         if( !(image instanceof Image) ) {
            throw new RuntimeException("In $name value of image: must be of type Image.")
         }
         icon = ImageWrapperResizableIcon.getIcon(image, initialDim)
      } else {
         try {
            def urlOrStream = processIconAttributes(builder, name, value, attributes)
            if( urlOrStream ) icon = ImageWrapperResizableIcon.getIcon(urlOrStream, initialDim)
         } catch( x ) {
            // ignore
         }
      }
      return icon
   }
}