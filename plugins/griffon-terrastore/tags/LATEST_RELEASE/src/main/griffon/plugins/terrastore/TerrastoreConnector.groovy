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
package griffon.plugins.terrastore

import terrastore.client.TerrastoreClient
import terrastore.client.connection.*
import terrastore.client.connection.resteasy.HTTPConnectionFactory

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
final class TerrastoreConnector {
    private bootstrap

    private static final Logger LOG = LoggerFactory.getLogger(TerrastoreConnector)

    static void enhance(MetaClass mc) {
        mc.withTerrastore = {Closure closure ->
            TerrastoreClientHolder.instance.withTerrastore('default', closure)
        }
        mc.withTerrastore << {String clientName, Closure closure ->
            TerrastoreClientHolder.instance.withTerrastore(clientName, closure)
        }
        mc.withTerrastore << {CallableWithArgs callable ->
            TerrastoreClientHolder.instance.withTerrastore('default', callable)
        }
        mc.withTerrastore << {String clientName, CallableWithArgs callable ->
            TerrastoreClientHolder.instance.withTerrastore(clientName, callable)
        }
    }

    Object withTerrastore(String clientName = 'default', Closure closure) {
        TerrastoreClientHolder.instance.withTerrastore(clientName, closure)
    }

    public <T> T withTerrastore(String clientName = 'default', CallableWithArgs<T> callable) {
        return TerrastoreClientHolder.instance.withTerrastore(clientName, callable)
    }

    // ======================================================

    ConfigObject createConfig(GriffonApplication app) {
        def clientClass = app.class.classLoader.loadClass('TerrastoreConfig')
        new ConfigSlurper(Environment.current.name).parse(clientClass)
    }

    private ConfigObject narrowConfig(ConfigObject config, String clientName) {
        return clientName == 'default' ? config.client : config.clients[clientName]
    }

    TerrastoreClient connect(GriffonApplication app, ConfigObject config, String clientName = 'default') {
        if (TerrastoreClientHolder.instance.isClientConnected(clientName)) {
            return TerrastoreClientHolder.instance.getClient(clientName)
        }

        config = narrowConfig(config, clientName)
        app.event('TerrastoreConnectStart', [config, clientName])
        TerrastoreClient client = startTerrastore(config)
        TerrastoreClientHolder.instance.setClient(clientName, client)
        bootstrap = app.class.classLoader.loadClass('BootstrapTerrastore').newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(clientName, client)
        app.event('TerrastoreConnectEnd', [clientName, client])
        client
    }

    void disconnect(GriffonApplication app, ConfigObject config, String clientName = 'default') {
        if (TerrastoreClientHolder.instance.isClientConnected(clientName)) {
            config = narrowConfig(config, clientName)
            TerrastoreClient client = TerrastoreClientHolder.instance.getClient(clientName)
            app.event('TerrastoreDisconnectStart', [config, clientName, client])
            bootstrap.destroy(clientName, client)
            stopTerrastore(config, client)
            app.event('TerrastoreDisconnectEnd', [config, clientName])
            TerrastoreClientHolder.instance.disconnectClient(clientName)
        }
    }

    private TerrastoreClient startTerrastore(ConfigObject config) {
        new TerrastoreClient(
             config.hostManager ?: config.host,
             config.connectionFactory ?: new HTTPConnectionFactory(),
             config.descriptors ?: []
        )
    }

    private void stopTerrastore(ConfigObject config, TerrastoreClient client) {
        // empty ??
    }
}
