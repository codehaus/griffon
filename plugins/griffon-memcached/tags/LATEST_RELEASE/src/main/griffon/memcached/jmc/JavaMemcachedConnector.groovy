/*
 * Copyright 2010 the original author or authors.
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
package griffon.memcached.jmc

import griffon.core.GriffonApplication
import griffon.util.Environment

import griffon.memcached.MemcachedConnectorDelegate
import griffon.memcached.MemcachedClientHolder

import com.danga.MemCached.SockIOPool
import com.danga.MemCached.MemCachedClient

/**
 * @author Andres Almiray
 */
class JavaMemcachedConnector extends MemcachedConnectorDelegate {
    static {
        MetaClass mc = MemCachedClient.metaClass
        mc.getAt = {String name -> delegate.get(name)}
        mc.putAt = {String name, Object value -> delegate.set(name, value)}
    }

    String getConfigClassName() {
        'JavaMemcachedConfig'
    }

    String getBootstrapClassName() {
        'BootstrapJavaMemcached'
    }

    void doConnect(GriffonApplication app, ConfigObject config) {
        SockIOPool pool = SockIOPool.instance

        List servers = []
        List weights = []
        config.servers?.each { server, settings = [:] ->
            servers << server +':'+ (settings.port ?: '11211')
            weights << (settings.weight ?: 1).toString().toInteger()
        }
        pool.servers = (servers as String[])
        pool.weights = (weights as Integer[])
        config.pool?.each { key, value ->
            pool[key] = value
        }

        pool.initialize()
        MemcachedClientHolder.instance.mcc = new MemCachedClient()
    }

    void doDisconnect(GriffonApplication app, ConfigObject config) {
        SockIOPool.instance.shutDown()
    }
}
