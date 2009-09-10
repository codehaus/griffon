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

package griffon.clojure

import clojure.lang.RT

/**
 * @author Jeff Brown
 * @author Andres Almiray
 */
class ClojureProxy {
    def ns = 'griffon'

    def getAt(String ns) {
        return new ClojureProxy(ns: ns)
    }

    def methodMissing(String name, args) {
        def impl = { Object[] a = new Object[0] ->
            def var = RT.var(delegate.ns, name)
            var.invoke (*a)
        }
        ClojureProxy.metaClass."${name}" = impl
        impl(args)
    }
}