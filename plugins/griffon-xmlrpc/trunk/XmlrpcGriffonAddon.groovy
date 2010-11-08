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

import groovy.net.xmlrpc.XMLRPCServerProxy

/**
 * @author Andres Almiray
 */
class XmlrpcGriffonAddon {
   def events = [
      NewInstance: { klass, type, instance ->
         def types = app.config.griffon?.xmlrpc?.injectInto ?: ['controller']
         if(!types.contains(type)) return
         def mc = app.artifactManager.findGriffonClass(klass).metaClass
         mc.withXmlrpc = withClient.curry(instance)
      }
   ]

   // ======================================================

   private withClient = { Object instance, Map params, Closure closure ->
      def client = null
      if(params.id) {
         String id = params.remove('id').toString()
         MetaClass mc = app.artifactManager.findGriffonClass(klass).metaClass
         if(mc.hasProperty(instance, id)) {
            client = instance."$id"
         } else {
            client = makeClient(params) 
            mc."$id" = client
         }
      } else {
        client = makeClient(params) 
      }

      if(closure) {
         closure.delegate = client
         closure.resolveStrategy = Closure.DELEGATE_FIRST
         closure()
      }
   }

   private makeClient(Map params) {
      def url = params.remove('url')
      // def detectEncoding = params.remove('detectEncoding') ?: false
      if(!url) {
         throw new RuntimeException("Failed to create xml-rpc client, url: parameter is null or invalid.")
      }
      try {
         return new XMLRPCServerProxy(url/*, detectEncoding*/)
      } catch(MalformedURLException mue) {
         throw new RuntimeException("Failed to create xml-rpc client, reason: $mue", mue)
      }
   }
}
