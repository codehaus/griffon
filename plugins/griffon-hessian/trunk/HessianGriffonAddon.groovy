/*
 * Copyright 2009-2010 the original author or authors.
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

import griffon.hessian.HessianProxy
import griffon.hessian.BurlapProxy

import java.lang.reflect.InvocationTargetException

/**
 * @author Andres Almiray
 */
class HessianGriffonAddon {
   private static final Class[] CTOR_ARGS1 = [String, Class] as Class[]
   private static final Class[] CTOR_ARGS2 = [String, String] as Class[]

   def events = [
      NewInstance: { klass, type, instance ->
         def types = app.config.griffon?.hessian?.injectInto ?: ['controller']
         if(!types.contains(type)) return
         instance.metaClass.withHessian = withClient.curry(HessianProxy, instance)
         instance.metaClass.withBurlap = withClient.curry(BurlapProxy, instance)
      }
   ]

   // ======================================================

   private withClient = { Class klass, Object instance, Map params, Closure closure ->
      def client = null
      if(params.id) {
         String id = params.remove('id').toString()
         if(instance.metaClass.hasProperty(instance, id)) {
            client = instance."$id"
         } else {
            client = makeClient(klass, params) 
            instance.metaClass."$id" = client
         }
      } else {
        client = makeClient(klass, params) 
      }

      if(closure) {
         closure.delegate = client
         closure.resolveStrategy = Closure.DELEGATE_FIRST
         closure()
      }
   }

   private makeClient(Class klass, Map params) {
      def url = params.remove('url')
      if(!url) {
         throw new RuntimeException("Failed to create ${(klass == HessianProxy? 'hessian' : 'burlap')} client, url: parameter is null or invalid.")
      }

      def ctorArgs = CTOR_ARGS1
      def serviceClass = params.remove('service')
      if(!serviceClass) {
         throw new RuntimeException("Failed to create ${(klass == HessianProxy? 'hessian' : 'burlap')} client, service: parameter is null or invalid.")
      }
      if(!(serviceClass instanceof Class)) {
         serviceClass = serviceClass.toString()
         ctorArgs = CTOR_ARGS2
      }

      try {
         return klass.getDeclaredConstructor(ctorArgs).newInstance(url, serviceClass)
      } catch (IllegalArgumentException e) {
         throw new RuntimeException("Failed to create ${(klass == HessianProxy? 'hessian' : 'burlap')} client, reason: $e", e)
      } catch (InvocationTargetException e) {
         throw new RuntimeException("Failed to create ${(klass == HessianProxy? 'hessian' : 'burlap')} client, reason: $e", e)
      }
   }
}
