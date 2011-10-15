/*
 * Copyright 2010-2011 the original author or authors.
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
package griffon.plugins.memcached


import net.spy.memcached.MemcachedClient
import net.spy.memcached.ConnectionFactoryBuilder

import griffon.core.GriffonApplication
import griffon.util.Environment
import griffon.util.Metadata
import griffon.util.CallableWithArgs

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andres Almiray
 */
@Singleton
final class MemcachedConnector {
    private bootstrap

    private static final Logger LOG = LoggerFactory.getLogger(MemcachedConnector)

    static void enhance(MetaClass mc) {
        mc.withMemcached = {Closure closure ->
            MemcachedClientHolder.instance.withMemcached('default', closure)
        }
        mc.withMemcached << {String clientName, Closure closure ->
            MemcachedClientHolder.instance.withMemcached(clientName, closure)
        }
        mc.withMemcached << {CallableWithArgs callable ->
            MemcachedClientHolder.instance.withMemcached('default', callable)
        }
        mc.withMemcached << {String clientName, CallableWithArgs callable ->
            MemcachedClientHolder.instance.withMemcached(clientName, callable)
        }
    }

    Object withMemcached(String clientName = 'default', Closure closure) {
        MemcachedClientHolder.instance.withMemcached(clientName, closure)
    }

    public <T> T withMemcached(String clientName = 'default', CallableWithArgs<T> callable) {
        return MemcachedClientHolder.instance.withMemcached(clientName, callable)
    }

    // ======================================================

    ConfigObject createConfig(GriffonApplication app) {
        def clientClass = app.class.classLoader.loadClass('MemcachedConfig')
        new ConfigSlurper(Environment.current.name).parse(clientClass)
    }

    private ConfigObject narrowConfig(ConfigObject config, String clientName) {
        return clientName == 'default' ? config.client : config.clients[clientName]
    }

    MemcachedClient connect(GriffonApplication app, ConfigObject config, String clientName = 'default') {
        if (MemcachedClientHolder.instance.isClientConnected(clientName)) {
            return MemcachedClientHolder.instance.getClient(clientName)
        }

        ConfigObject clientConfig = narrowConfig(config, clientName)
        app.event('MemcachedConnectStart', [clientConfig, clientName])
        MemcachedClient client = startMemcached(config, clientName)
        MemcachedClientHolder.instance.setClient(clientName, client)
        bootstrap = app.class.classLoader.loadClass('BootstrapMemcached').newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(clientName, client)
        app.event('MemcachedConnectEnd', [clientName, client])
        client
    }

    void disconnect(GriffonApplication app, ConfigObject config, String clientName = 'default') {
        if (MemcachedClientHolder.instance.isClientConnected(clientName)) {
            config = narrowConfig(config, clientName)
            MemcachedClient client = MemcachedClientHolder.instance.getClient(clientName)
            app.event('MemcachedDisconnectStart', [config, clientName, client])
            bootstrap.destroy(clientName, client)
            stopMemcached(config, client)
            app.event('MemcachedDisconnectEnd', [config, clientName])
            MemcachedClientHolder.instance.disconnectClient(clientName)
        }
    }

    private MemcachedClient startMemcached(ConfigObject config, String clientName) {
        List<InetSocketAddress> servers = []
        config.servers?.each { server, settings = [:] ->
            servers << new InetSocketAddress(server, (settings.port ?: '11211'))
        }
         
        ConfigObject clientConfig = narrowConfig(config, clientName)
        ConnectionFactoryBuilder cfBuilder = new ConnectionFactoryBuilder()
        clientConfig.connectionFactory?.each { key, value ->
            cfBuilder[key] = value
        }

        new MemcachedClient(cfBuilder.build(), servers)
    }

    private void stopMemcached(ConfigObject config, MemcachedClient client) {
        client.shutdown()
    }
}
