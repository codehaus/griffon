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

import groovyx.net.http.AsyncHTTPBuilder
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.RESTClient

import java.lang.reflect.InvocationTargetException

/**
 * @author Andres Almiray
 */
class RestGriffonAddon {
   def events = [
      NewInstance: { klass, type, instance ->
         def types = app.config.griffon?.rest?.injectInto ?: ['controller']
         if(!types.contains(type)) return
         instance.metaClass.withAsyncHttp = withClient.curry(AsyncHTTPBuilder, instance)
         instance.metaClass.withHttp = withClient.curry(HTTPBuilder, instance)
         instance.metaClass.withRest = withClient.curry(RESTClient, instance)
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

      if(params.containsKey('proxy')) {
         Map proxyArgs = [scheme: 'http', port: 80] + params.remove('proxy')
         if(!proxyArgs.host) throw new IllegalArgumentException('proxy.host cannot be null!')
         client.setProxy(proxyArgs.host, proxyArgs.port as int, proxyArgs.scheme)
      }

      if(closure) {
         closure.delegate = client
         closure.resolveStrategy = Closure.DELEGATE_FIRST
         closure()
      }
   }

   private makeClient(Class klass, Map params) {
      if(klass == AsyncHTTPBuilder) {
         try {
            Map args = [:]
            ['threadPool', 'poolSize', 'uri', 'contentType', 'timeout'].each { arg ->
               if(params[(arg)] != null) args[(arg)] = params[(arg)]
            }
            return klass.newInstance(args)
         } catch(IllegalArgumentException e) {
            throw new RuntimeException("Failed to create async http client, reason: $e", e)
         } catch(InvocationTargetException e) {
            throw new RuntimeException("Failed to create async http client, reason: $e", e)
         }
      }
      try {
         def client =  klass.newInstance()
         if(params.uri) client.uri = params.remove('uri')
         if(params.contentType) client.contentType = params.remove('contentType')
         return client
      } catch(IllegalArgumentException e) {
         throw new RuntimeException("Failed to create ${(klass == HTTPBuilder? 'http' : 'rest')} client, reason: $e", e)
      } catch(InvocationTargetException e) {
         throw new RuntimeException("Failed to create ${(klass == HTTPBuilder? 'http' : 'rest')} client, reason: $e", e)
      }
   }
}
