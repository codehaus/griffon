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
package griffon.memcached.xmc

import griffon.core.GriffonApplication
import griffon.util.Environment

import griffon.memcached.MemcachedConnectorDelegate
import griffon.memcached.MemcachedClientHolder

import net.rubyeye.xmemcached.MemcachedClient
import net.rubyeye.xmemcached.XMemcachedClientBuilder
import com.google.code.yanf4j.config.Configuration

/**
 * @author Andres Almiray
 */
class XMemcachedConnector extends MemcachedConnectorDelegate {
    String getConfigClassName() {
        'XMemcachedConfig'
    }

    String getBootstrapClassName() {
        'BootstrapXMemcached'
    }

    void doConnect(GriffonApplication app, ConfigObject config) {
        List<InetSocketAddress> servers = []
        List weights = []
        config.servers?.each { server, settings = [:] ->
            servers << new InetSocketAddress(server, (settings.port ?: '11211'))
            weights << ((settings.weight ?: 1) as int)
        }
         
        XMemcachedClientBuilder clientBuilder = new XMemcachedClientBuilder(servers, weights as int[])
        config.client?.props?.each { key, value ->
            clientBuilder[key] = value
        }
        Configuration c = new Configuration()
        config.client?.configuration?.each { key, value ->
            c[key] = value
        }
        clientBuilder.configuration = c

        MemcachedClientHolder.instance.mcc = clientBuilder.build() 
    }

    void doDisconnect(GriffonApplication app, ConfigObject config) {
        MemcachedClientHolder.instance.mcc.shutdown()
    }
}
