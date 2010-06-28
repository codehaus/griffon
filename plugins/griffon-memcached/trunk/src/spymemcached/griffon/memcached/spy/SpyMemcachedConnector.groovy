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
package griffon.memcached.spy

import griffon.core.GriffonApplication
import griffon.util.Environment

import griffon.memcached.MemcachedConnectorDelegate
import griffon.memcached.MemcachedClientHolder

import net.spy.memcached.MemcachedClient
import net.spy.memcached.ConnectionFactoryBuilder

/**
 * @author Andres Almiray
 */
class SpyMemcachedConnector extends MemcachedConnectorDelegate {
    String getConfigClassName() {
        'SpyMemcachedConfig'
    }

    String getBootstrapClassName() {
        'BootstrapSpyMemcached'
    }

    void doConnect(GriffonApplication app, ConfigObject config) {
        List<InetSocketAddress> servers = []
        config.servers?.each { server, settings = [:] ->
            servers << new InetSocketAddress(server, (settings.port ?: '11211'))
        }
         
        ConnectionFactoryBuilder cfBuilder = new ConnectionFactoryBuilder()
        config.connectionFactory?.each { key, value ->
            cfBuilder[key] = value
        }

        MemcachedClientHolder.instance.mcc = new MemcachedClient(cfBuilder.build(), servers)
    }

    void doDisconnect(GriffonApplication app, ConfigObject config) {
        MemcachedClientHolder.instance.mcc.shutdown()
    }
}
