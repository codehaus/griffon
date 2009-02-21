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

package griffon.builder.fx

import com.sun.javafx.runtime.FXObject
import com.sun.javafx.runtime.location.*
import com.sun.javafx.runtime.sequence.*
import com.sun.javafx.functions.*

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public class Fx {
   public static void enhance() {
      FXObject.metaClass.getProperty = { String name ->
         def clazz = delegate.getClass()
         def mc = clazz.metaClass
         def metaProperty = mc.getMetaProperty(name)
         if(!metaProperty) metaProperty = mc.getMetaProperty("\$$name")
         if(!metaProperty) metaProperty = mc.getMetaProperty("\$${clazz.name.replace('.','\$')}\$$name")
         if(metaProperty) {
            def value = metaProperty.getProperty(delegate)
            return ObjectLocation.isAssignableFrom(metaProperty.type) ? value.get() : value
         }
         throw new MissingPropertyException(name,clazz)
      }

      FXObject.metaClass.setProperty = { String name, value ->
         def clazz = delegate.getClass()
         def mc = clazz.metaClass
         def metaProperty = mc.getMetaProperty(name)
         if(!metaProperty) metaProperty = mc.getMetaProperty("\$$name")
         if(!metaProperty) metaProperty = mc.getMetaProperty("\$${clazz.name.replace('.','\$')}\$$name")
         if(metaProperty) {
            if( ObjectLocation.isAssignableFrom(metaProperty.type) ) {
               metaProperty.getProperty(delegate).set(value)
            } else {
               metaProperty.setProperty(delegate,value)
            }
         } else {
            throw new MissingPropertyException(name,clazz)
         }
      }

      FXObject.metaClass.hasAttribute = { String name ->
         def clazz = delegate.getClass()
         def mc = clazz.metaClass
         def metaProperty = mc.getMetaProperty(name)
         if(!metaProperty) metaProperty = mc.getMetaProperty("\$$name")
         if(!metaProperty) metaProperty = mc.getMetaProperty("\$${clazz.name.replace('.','\$')}\$$name")
         return metaProperty != null
      }

      FXObject.metaClass.attribute = { String name ->
         def clazz = delegate.getClass()
         def mc = clazz.metaClass
         def metaProperty = mc.getMetaProperty(name)
         if(!metaProperty) metaProperty = mc.getMetaProperty("\$$name")
         if(!metaProperty) metaProperty = mc.getMetaProperty("\$${clazz.name.replace('.','\$')}\$$name")
         if(metaProperty && ObjectLocation.isAssignableFrom(metaProperty.type)) return metaProperty.getProperty(delegate)
         throw new MissingPropertyException(name,clazz)
      }

      FXObject.metaClass.attributeType = { String name ->
         def clazz = delegate.getClass()
         def mc = clazz.metaClass
         def metaProperty = mc.getMetaProperty(name)
         if(!metaProperty) metaProperty = mc.getMetaProperty("\$$name")
         if(!metaProperty) metaProperty = mc.getMetaProperty("\$${clazz.name.replace('.','\$')}\$$name")
         if(metaProperty) return metaProperty.type
         throw new MissingPropertyException(name,clazz)
      }

      ObjectLocation.metaClass.setOnChange = { src ->
         if( src instanceof Closure ) src = [onChange:src] as ObjectChangeListener
         if( !(src instanceof ObjectChangeListener) ) src = src as ObjectChangeListener
         delegate.addChangeListener(src)
      }

      FXObject.metaClass.setOnChange = { String attrName, src ->
         delegate.attribute(attrName).onChange = src
      }

      ObjectLocation.metaClass.toString = { ->
         delegate.get()
      }
   }
}