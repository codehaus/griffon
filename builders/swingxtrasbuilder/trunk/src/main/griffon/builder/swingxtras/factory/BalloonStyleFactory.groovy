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

package griffon.builder.swingxtras.factory

import java.awt.Color
import java.awt.Image
import java.awt.Toolkit

import net.java.balloontip.styles.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class EdgedBalloonStyleFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      Color fillColor = attributes.remove("fillColor") ?: Color.WHITE
      Color borderColor = attributes.remove("borderColor") ?: Color.BLACK
      return new EdgedBalloonStyle(fillColor, borderColor)
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class MinimalBalloonStyleFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      Color fillColor = attributes.remove("fillColor") ?: Color.WHITE
      int arcWidth = attributes.remove("arcWidth") ?: 5
      return new MinimalBalloonStyle(fillColor, arcWidth)
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class ModernBalloonStyleFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      Color topColor = attributes.remove("topColor") ?: Color.WHITE
      Color bottomColor = attributes.remove("bottomColor") ?: Color.LIGHT_GRAY
      Color borderColor = attributes.remove("borderColor") ?: Color.BLACK
      int arcWidth = attributes.remove("arcWidth") ?: 5
      int arcHeight = attributes.remove("arcHeight") ?: 5
      return new ModernBalloonStyle(arcWidth, arcHeight, topColor, bottomColor, borderColor)
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class RoundedBalloonStyleFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      Color fillColor = attributes.remove("fillColor") ?: Color.WHITE
      Color borderColor = attributes.remove("borderColor") ?: Color.BLACK
      int arcWidth = attributes.remove("arcWidth") ?: 5
      int arcHeight = attributes.remove("arcHeight") ?: 5
      return new RoundedBalloonStyle(arcWidth, arcHeight, fillColor, borderColor)
   }
}

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class TexturedBalloonStyleFactory extends AbstractFactory {
   public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
      Image image = null
      if( !attributes.containsKey("image") ) {
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
               throw new RuntimeException("In $name file: attributes must be of type java.io.File or a String")
            }
         } else if (attributes.containsKey("inputStream")) {
            value = attributes.remove("inputStream")
            if (!(value instanceof InputStream)) {
               throw new RuntimeException("In $name inputStream: attributes must be of type java.io.InputStream")
            }
         }

         // not else if so we can adjust for the case of file string where the file does not exist
         def resource = attributes.remove("resource")
         if (resource != null) {
            def klass = builder.context.owner
            if (attributes.containsKey("class")) {
                klass = attributes.remove("class")
            }
            if (klass == null) {
                klass = PreferencesTabFactory
            } else if (!(klass instanceof Class)) {
                klass = klass.class
            }
            // for now try URL approach.
            // we may need to extract the byte[] for some packaging cases
            value = klass.getResource(resource)
            if (value == null) {
                throw new RuntimeException("In $name the value argument '$resource' does not refer to a file or a class resource")
            }
         }

         if(value) image = Toolkit.getDefaultToolkit().getImage(value)
      } else {
         image = attributes.remove("image")
      }

      Color borderColor = attributes.remove("borderColor") ?: Color.BLACK
      int arcWidth = attributes.remove("arcWidth") ?: 5
      int arcHeight = attributes.remove("arcHeight") ?: 5
      return new TexturedBalloonStyle(arcWidth, arcHeight, image, borderColor)
   }
}