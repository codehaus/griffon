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

import groovyx.net.http.AsyncHTTPBuilder
import groovyx.net.http.HTTPBuilder
import groovyx.net.http.RESTClient
import griffon.util.IGriffonApplication

import java.util.logging.Level
import java.util.logging.Logger
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException

/**
 * @author Andres.Almiray
 */
class RestGriffonAddon {
   private IGriffonApplication application

/*
   private static final Class[] ZERO_ARGS = []
   private static final Class[] ONE_ARG = [Object]
   private static final Class[] TWO_ARGS = [Object, Object]
   private static final Constructor[] HTTP_CTORS = new Constructor[3]
   private static final Constructor[] REST_CTORS = new Constructor[3]

   static {
      try {
         [[klass: HTTPBuilder, ctors: HTTP_CTORS],
          [klass: RESTClient,  ctors: REST_CTORS]].each { entry ->
             entry.ctors[0] = entry.klass.getConstructor(ZERO_ARGS)
             entry.ctors[1] = entry.klass.getConstructor(ONE_ARG)
             entry.ctors[2] = entry.klass.getConstructor(TWO_ARGS)
         }
      } catch(NoSuchMethodException ex) {
         Logger.getLogger("global").log(Level.INFO, null, ex)
      } catch(SecurityException ex) {
         Logger.getLogger("global").log(Level.SEVERE, null, ex)
      }
   }
*/

   def addonInit = { app ->
      application = app
      app.addApplicationEventListener(this)
   }

   def onNewInstance = { klass, type, instance ->
      def types = application.config.griffon?.rest?.injectInto ?: ["controller"]
      if(!types.contains(type)) return
      // instance.metaClass.withHttp = withClient.curry(HTTP_CTORS, instance)
      // instance.metaClass.withRest = withClient.curry(REST_CTORS, instance)
      instance.metaClass.withAsyncHttp = withClient.curry(AsyncHTTPBuilder, instance)
      instance.metaClass.withHttp = withClient.curry(HTTPBuilder, instance)
      instance.metaClass.withRest = withClient.curry(RESTClient, instance)
   }

   // ======================================================

   //private withClient = { Constructor[] ctors, Object instance, Map params, Closure closure ->
   private withClient = { Class klass, Object instance, Map params, Closure closure ->
      def client = null
      if(params.id) {
         String id = params.remove("id").toString()
         if(instance.metaClass.hasProperty(instance, id)) {
            client = instance."$id"
         } else {
            // client = makeClient(ctors, params) 
            client = makeClient(klass, params) 
            instance.metaClass."$id" = client
         }
      } else {
        // client = makeClient(ctors, params) 
        client = makeClient(klass, params) 
      }

      if(params.containsKey("proxy")) {
         Map proxyArgs = [scheme: "http", port: 80] + params.remove("proxy")
         if(!proxyArgs.host) throw new IllegalArgumentException("proxy.host cannot be null!")
         client.setProxy(proxyArgs.host, proxyArgs.port as int, proxyArgs.scheme)
      }

      closure.delegate = client
      closure.resolveStrategy = Closure.DELEGATE_FIRST
      closure()
   }

   // private makeClient(Constructor[] ctors, Map params) {
   private makeClient(Class klass, Map params) {
      /*
      def args = []
      if(params.uri) {
         args << params.remove("uri")
         if(params.contentType) {
            args << params.remove("contentType")
         }
      }
      try {
         return ctors[args.size()].newInstance(*args)
      } catch (IllegalArgumentException e) {
         throw new RuntimeException("Failed to create ${(ctors == HTTP_CTORS? 'http' : 'rest')} client reason: $e", e)
      } catch (InvocationTargetException e) {
         throw new RuntimeException("Failed to create ${(ctors == HTTP_CTORS? 'http' : 'rest')} client reason: $e", e)
      }
      */
      if(klass == AsyncHTTPBuilder) {
         try {
            Map args = [:]
            ["threadPool", "poolSize", "uri", "contentType", "timeout"].each { arg ->
               if(params[(arg)] != null) args[(arg)] = params[(arg)]
            }
            return klass.newInstance(args)
         } catch (IllegalArgumentException e) {
            throw new RuntimeException("Failed to create async http client reason: $e", e)
         } catch (InvocationTargetException e) {
            throw new RuntimeException("Failed to create async http client reason: $e", e)
         }
      }
      try {
         def client =  klass.newInstance()
         if(params.uri) client.uri = params.remove("uri")
         if(params.contentType) client.contentType = params.remove("contentType")
         return client
      } catch (IllegalArgumentException e) {
         throw new RuntimeException("Failed to create ${(klass == HTTPBuilder? 'http' : 'rest')} client reason: $e", e)
      } catch (InvocationTargetException e) {
         throw new RuntimeException("Failed to create ${(klass == HTTPBuilder? 'http' : 'rest')} client reason: $e", e)
      }
   }
}
