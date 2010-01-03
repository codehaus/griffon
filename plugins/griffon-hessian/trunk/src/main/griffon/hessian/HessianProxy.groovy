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

package griffon.hessian

import com.caucho.hessian.client.HessianProxyFactory

/**
 * @author Andres Almiray
 */
class HessianProxy extends AbstractCauchoProxy {
   private final HessianProxyFactory hessianFactory = new HessianProxyFactory()

   HessianProxy(String url, Class serviceClass) {
      super(url, serviceClass)
   }

   HessianProxy(String url, String serviceClassName) {
      super(url, serviceClassName)
   }

   def getFactory() {
      return hessianFactory
   }
}
