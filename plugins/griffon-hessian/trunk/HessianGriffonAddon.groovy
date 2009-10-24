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

import griffon.util.IGriffonApplication
import griffon.hessian.HessianProxy
import griffon.hessian.BurlapProxy

import java.lang.reflect.InvocationTargetException

/**
 * @author Andres.Almiray
 */
class HessianGriffonAddon {
   private IGriffonApplication application
   private static final Class[] STR_ARGS = [String] as Class[]

   def addonInit = { app ->
      application = app
      app.addApplicationEventListener(this)
   }

   def onNewInstance = { klass, type, instance ->
      def types = application.config.griffon?.ws?.injectInto ?: ["controller"]
      if(!types.contains(type)) return
      instance.metaClass.withHessian = withClient.curry(HessianProxy, instance)
      instance.metaClass.withBurlap = withClient.curry(BurlapProxy, instance)
   }

   // ======================================================

   private withClient = { Class klass, Object instance, Map params, Closure closure ->
      def client = null
      if(params.id) {
         String id = params.remove("id").toString()
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
      def url = params.remove("url")
      if(!url) {
         throw new RuntimeException("Failed to create ${(klass == HessianProxy? 'hessian' : 'burlap')} client, url: parameter is null or invalid.")
      }
      try {
         return klass.getDeclaredConstructor(STR_ARGS).newInstance(url)
      } catch (IllegalArgumentException e) {
         throw new RuntimeException("Failed to create ${(klass == HessianProxy? 'hessian' : 'burlap')} client, reason: $e", e)
      } catch (InvocationTargetException e) {
         throw new RuntimeException("Failed to create ${(klass == HessianProxy? 'hessian' : 'burlap')} client, reason: $e", e)
      }
   }
}
