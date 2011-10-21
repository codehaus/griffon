/*
 * Copyright 2009-2011 the original author or authors.
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

package griffon.plugins.rmi

import griffon.util.ApplicationHolder
import griffon.util.CallableWithArgs
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Andres Almiray
 */
@Singleton
class RmiConnector {
    private final Map CLIENTS = new ConcurrentHashMap()

    static void enhance(MetaClass mc, Object instance = null) {
        mc.withRmi = {Map params, Closure closure ->
            RmiConnector.instance.withRmi(instance, params, closure)
        }
        mc.withRmi = {Map params, CallableWithArgs callable ->
            RmiConnector.instance.withRmi(instance, params, callable)
        }
    }

    Object withRmi(Object instance = null, Map params, Closure closure) {
        return doWithClient(instance, params, closure)
    }

    public <T> T withRmi(Object instance = null, Map params, CallableWithArgs<T> callable) {
        return doWithClient(instance, params, callable)
    }

    // ======================================================

    private doWithClient(Object instance, Map params, Closure closure) {
        def client = configureClient(instance, params)

        if (closure) {
            closure.delegate = proxy
            closure.resolveStrategy = Closure.DELEGATE_FIRST
            return closure()
        }
        return null
    }

    private <T> T doWithClient(Object instance, Map params, CallableWithArgs<T> callable) {
        def client = configureClient(instance, params)

        if (callable) {
            callable.args = [client] as Object[]
            return callable.run()
        }
        return null
    }

    private configureClient(Object instance, Map params) {
        def client = null
        if (params.id) {
            String id = params.remove('id').toString()
            if(instance != null) {
                MetaClass mc = ApplicationHolder.application.artifactManager.findGriffonClass(instance).metaClass
                if (mc.hasProperty(instance, id)) {
                    client = instance."$id"
                } else {
                    client = makeClient(params)
                    mc."$id" = client
                }
            } else {
                client = CLIENTS[id]
                if(client == null) {
                    client = makeClient(params)
                    CLIENTS[id] = client 
                }
            }
        } else {
            client = makeClient(params)
        }
        client
    }

    private makeClient(Map params) {
        def host = params.remove('host') ?: 'localhost'
        def port = params.remove('port') ?: 1199
        def lazy = params.remove('lazy') ?: true
        try {
            return new RMIClient(host, port as int, lazy)
        } catch(Exception e) {
            throw new RuntimeException("Failed to create RMI client, reason: $e", e)
        }
    }
}
