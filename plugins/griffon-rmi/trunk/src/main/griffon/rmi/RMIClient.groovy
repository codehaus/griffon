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

package griffon.rmi

import java.rmi.RemoteException
import java.rmi.registry.Registry
import java.rmi.registry.LocateRegistry

/**
 * @author Andres.Almiray
 */
class RMIClient {
   private final Map services = [:]
   private final String host
   private final int port
   private Registry registry

   RMIClient(String host, int port, boolean lazy) {
      this.host = host
      this.port = port
      if(!lazy) {
         locateRegistry()
      }
   }

   def service(String serviceName, Closure closure) {
      locateRegistry()
      def service = services.get(serviceName)
      if(!service) {
         service = registry.lookup(serviceName)
         services.put(serviceName, service)
      }
      
      closure.resolveStrategy = Closure.DELEGATE_FIRST
      closure.delegate = service
      closure()
   }

   private void locateRegistry() {
      if(!registry) {
         registry = LocateRegistry.getRegistry(host, port)
      }
   }
}
