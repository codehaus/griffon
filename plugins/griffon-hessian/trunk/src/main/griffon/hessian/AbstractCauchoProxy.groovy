/*
 * Copyright 2009 the original author or authors.
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

package griffon.hessian

/**
 * @author Andres.Almiray
 */
abstract class AbstractCauchoProxy {
   private final String url
   private final Class serviceClass
   private service

   AbstractCauchoProxy(String url, Class serviceClass) {
      this.url = url
      this.serviceClass = serviceClass
   }

   AbstractCauchoProxy(String url, String serviceClassName) {
      this.url = url
      try {
         this.serviceClass = serviceClassName as Class // TODO chain classLoaders ?
      } catch(ClassNotFoundException cnfe) {
         throw new RuntimeException("Invalid service className ${serviceClassName}: $cnfe", cnfe)
      }
   }

   def methodMissing(String methodName, args) {
      if(!service) {
         service = getFactory().create(serviceClass, url)
      }
      service."$methodName"(*args)
   }

   protected abstract getFactory()
}
