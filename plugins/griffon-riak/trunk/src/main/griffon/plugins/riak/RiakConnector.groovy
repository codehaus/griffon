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
package griffon.plugins.riak

import com.basho.riak.client.RiakClient
import com.basho.riak.client.RiakConfig

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
final class RiakConnector {
    private bootstrap

    private static final Logger LOG = LoggerFactory.getLogger(RiakConnector)

    static void enhance(MetaClass mc) {
        mc.withRiak = {Closure closure ->
            RiakClientHolder.instance.withRiak('default', closure)
        }
        mc.withRiak << {String clientName, Closure closure ->
            RiakClientHolder.instance.withRiak(clientName, closure)
        }
        mc.withRiak << {CallableWithArgs callable ->
            RiakClientHolder.instance.withRiak('default', callable)
        }
        mc.withRiak << {String clientName, CallableWithArgs callable ->
            RiakClientHolder.instance.withRiak(clientName, callable)
        }
    }

    Object withRiak(String clientName = 'default', Closure closure) {
        RiakClientHolder.instance.withRiak(clientName, closure)
    }

    public <T> T withRiak(String clientName = 'default', CallableWithArgs<T> callable) {
        return RiakClientHolder.instance.withRiak(clientName, callable)
    }

    // ======================================================

    ConfigObject createConfig(GriffonApplication app) {
        def clientClass = app.class.classLoader.loadClass('RiakConfig')
        new ConfigSlurper(Environment.current.name).parse(clientClass)
    }

    private ConfigObject narrowConfig(ConfigObject config, String clientName) {
        return clientName == 'default' ? config.client : config.clients[clientName]
    }

    RiakClient connect(GriffonApplication app, ConfigObject config, String clientName = 'default') {
        if (RiakClientHolder.instance.isClientConnected(clientName)) {
            return RiakClientHolder.instance.getClient(clientName)
        }

        config = narrowConfig(config, clientName)
        app.event('RiakConnectStart', [config, clientName])
        RiakClient client = startRiak(config)
        RiakClientHolder.instance.setClient(clientName, client)
        bootstrap = app.class.classLoader.loadClass('BootstrapRiak').newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(clientName, client)
        app.event('RiakConnectEnd', [clientName, client])
        client
    }

    void disconnect(GriffonApplication app, ConfigObject config, String clientName = 'default') {
        if (RiakClientHolder.instance.isClientConnected(clientName)) {
            config = narrowConfig(config, clientName)
            RiakClient client = RiakClientHolder.instance.getClient(clientName)
            app.event('RiakDisconnectStart', [config, clientName, client])
            bootstrap.destroy(clientName, client)
            stopRiak(config, client)
            app.event('RiakDisconnectEnd', [config, clientName])
            RiakClientHolder.instance.disconnectClient(clientName)
        }
    }

    private RiakClient startRiak(ConfigObject config) {
        RiakConfig riakConfig = new RiakConfig()
        config.each { key, value ->
            riakConfig[key] = value
        }
        return new RiakClient(riakConfig)
    }

    private void stopRiak(ConfigObject config, RiakClient client) {
        // empty ??
    }
}
