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
   private final Map services = [:]
   private final String url

   AbstractCauchoProxy(String url) {
      this.url = url.endsWith("/") ? url : url + "/"
   }

   def service(String serviceClassName, Closure closure) {
      Class serviceClass = null
      try {
         serviceClass = serviceClassName as Class // TODO chain classLoaders ?
      } catch(ClassNotFoundException cnfe) {
         throw new RuntimeException("Invalid service className ${serviceClassName}: $cnfe", cnfe)
      }
      return service(serviceClass, closure)
   }

   def service(Class serviceClass, Closure closure) {
      if(!serviceClass) return null
      def service = services.get(serviceClass.name)
      if(!service) {
         service = getFactory().create(serviceClass, url + serviceClass.simpleName)
         services.put(serviceClass.name, service)
      }
      closure.resolveStrategy = Closure.DELEGATE_FIRST
      closure.delegate = service
      closure()
   }

   def release(Class serviceClass) {
      if(!serviceClass) return
      services.remove(serviceClass.name)
   }

   def release(String serviceClassName) {
      if(!serviceClassName) return
      services.remove(serviceClassName)
   }

   def methodMissing(String serviceClassName, args) {
      // TODO validate params
      service(serviceClassName, args[0])
   }

   protected abstract getFactory()
}
