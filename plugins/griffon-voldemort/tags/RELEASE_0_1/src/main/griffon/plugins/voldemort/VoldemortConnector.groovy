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
package griffon.plugins.voldemort

import voldemort.client.*

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
final class VoldemortConnector {
    private bootstrap

    private static final Logger LOG = LoggerFactory.getLogger(VoldemortConnector)

    static void enhance(MetaClass mc) {
        mc.withVoldemort = {Closure closure ->
            StoreClientHolder.instance.withVoldemort('default', closure)
        }
        mc.withVoldemort << {String clientName, Closure closure ->
            StoreClientHolder.instance.withVoldemort(clientName, closure)
        }
        mc.withVoldemort << {CallableWithArgs callable ->
            StoreClientHolder.instance.withVoldemort('default', callable)
        }
        mc.withVoldemort << {String clientName, CallableWithArgs callable ->
            StoreClientHolder.instance.withVoldemort(clientName, callable)
        }
        mc.withVoldemortStore << {String storeName, Closure closure ->
            StoreClientHolder.instance.withVoldemortStore('default', storeName, closure)
        }
        mc.withVoldemortStore << {String clientName, String storeName, Closure closure ->
            StoreClientHolder.instance.withVoldemortStore(clientName, storeName, closure)
        }
        mc.withVoldemortStore << {String storeName, CallableWithArgs callable ->
            StoreClientHolder.instance.withVoldemortStore('default', storeName, callable)
        }
        mc.withVoldemortStore << {String clientName, String storeName, CallableWithArgs callable ->
            StoreClientHolder.instance.withVoldemortStore(clientName, storeName, callable)
        }
    }

    Object withVoldemortStore(String clientName = 'default', String storeName, Closure closure) {
        StoreClientHolder.instance.withVoldemortStore(clientName, storeName, closure)
    }

    public <T> T withVoldemortStore(String clientName = 'default', String storeName, CallableWithArgs<T> callable) {
        return StoreClientHolder.instance.withVoldemortStore(clientName, storeName, callable)
    }

    Object withVoldemort(String clientName = 'default', Closure closure) {
        StoreClientHolder.instance.withVoldemort(clientName, closure)
    }

    public <T> T withVoldemort(String clientName = 'default', CallableWithArgs<T> callable) {
        return StoreClientHolder.instance.withVoldemort(clientName, callable)
    }

    // ======================================================

    ConfigObject createConfig(GriffonApplication app) {
        def clientClass = app.class.classLoader.loadClass('VoldemortConfig')
        new ConfigSlurper(Environment.current.name).parse(clientClass)
    }

    private ConfigObject narrowConfig(ConfigObject config, String clientName) {
        return clientName == 'default' ? config.client : config.clients[clientName]
    }

    StoreClientFactory connect(GriffonApplication app, ConfigObject config, String clientName = 'default') {
        if (StoreClientHolder.instance.isStoreClientFactoryConnected(clientName)) {
            return StoreClientHolder.instance.getStoreClientFactory(clientName)
        }

        config = narrowConfig(config, clientName)
        app.event('VoldemortConnectStart', [config, clientName])
        StoreClientFactory clientFactory = startVoldemort(config)
        StoreClientHolder.instance.setStoreClientFactory(clientName, clientFactory)
        bootstrap = app.class.classLoader.loadClass('BootstrapVoldemort').newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(clientName, clientFactory)
        app.event('VoldemortConnectEnd', [clientName, clientFactory])
        clientFactory
    }

    void disconnect(GriffonApplication app, ConfigObject config, String clientName = 'default') {
        if (StoreClientHolder.instance.isStoreClientFactoryConnected(clientName)) {
            config = narrowConfig(config, clientName)
            StoreClientFactory clientFactory = StoreClientHolder.instance.getStoreClientFactory(clientName)
            app.event('VoldemortDisconnectStart', [config, clientName, clientFactory])
            bootstrap.destroy(clientName, clientFactory)
            stopVoldemort(config, clientFactory)
            app.event('VoldemortDisconnectEnd', [config, clientName])
            StoreClientHolder.instance.disconnectStoreClientFactory(clientName)
        }
    }

    private StoreClientFactory startVoldemort(ConfigObject config) {
        ClientConfig clientConfig = new ClientConfig()
        config.config.each { k, v -> clientConfig[k] = v }
        new SocketStoreClientFactory(clientConfig)
    }

    private void stopVoldemort(ConfigObject config, StoreClientFactory clientFactory) {
        clientFactory.close()
    }
}
