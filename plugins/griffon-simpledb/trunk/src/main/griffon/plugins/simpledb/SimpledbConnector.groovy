/*
 * Copyright 2011 the original author or authors.
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
package griffon.plugins.simpledb

import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSCredentials
import com.amazonaws.services.simpledb.AmazonSimpleDB
import com.amazonaws.services.simpledb.AmazonSimpleDBClient

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
final class SimpledbConnector {
    private bootstrap

    private static final Logger LOG = LoggerFactory.getLogger(SimpledbConnector)

    static void enhance(MetaClass mc) {
        mc.withSimpledb = {Closure closure ->
            SimpledbClientHolder.instance.withSimpledb('default', closure)
        }
        mc.withSimpledb << {String clientName, Closure closure ->
            SimpledbClientHolder.instance.withSimpledb(clientName, closure)
        }
        mc.withSimpledb << {CallableWithArgs callable ->
            SimpledbClientHolder.instance.withSimpledb('default', callable)
        }
        mc.withSimpledb << {String clientName, CallableWithArgs callable ->
            SimpledbClientHolder.instance.withSimpledb(clientName, callable)
        }
    }

    Object withSimpledb(String clientName = 'default', Closure closure) {
        SimpledbClientHolder.instance.withSimpledb(clientName, closure)
    }

    public <T> T withSimpledb(String clientName = 'default', CallableWithArgs<T> callable) {
        return SimpledbClientHolder.instance.withSimpledb(clientName, callable)
    }

    // ======================================================

    ConfigObject createConfig(GriffonApplication app) {
        def clientClass = app.class.classLoader.loadClass('SimpledbConfig')
        new ConfigSlurper(Environment.current.name).parse(clientClass)
    }

    private ConfigObject narrowConfig(ConfigObject config, String clientName) {
        return clientName == 'default' ? config.client : config.clients[clientName]
    }

    AmazonSimpleDB connect(GriffonApplication app, ConfigObject config, String clientName = 'default') {
        if (SimpledbClientHolder.instance.isClientConnected(clientName)) {
            return SimpledbClientHolder.instance.getClient(clientName)
        }

        config = narrowConfig(config, clientName)
        app.event('SimpledbConnectStart', [config, clientName])
        AmazonSimpleDB client = startSimpledb(config, clientName)
        SimpledbClientHolder.instance.setClient(clientName, client)
        bootstrap = app.class.classLoader.loadClass('BootstrapSimpledb').newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(clientName, client)
        app.event('SimpledbConnectEnd', [clientName, client])
        client
    }

    void disconnect(GriffonApplication app, ConfigObject config, String clientName = 'default') {
        if (SimpledbClientHolder.instance.isClientConnected(clientName)) {
            config = narrowConfig(config, clientName)
            AmazonSimpleDB client = SimpledbClientHolder.instance.getClient(clientName)
            app.event('SimpledbDisconnectStart', [config, clientName, client])
            bootstrap.destroy(clientName, client)
            stopSimpledb(config, client)
            app.event('SimpledbDisconnectEnd', [config, clientName])
            SimpledbClientHolder.instance.disconnectClient(clientName)
        }
    }

    private AmazonSimpleDB startSimpledb(ConfigObject config, String clientName) {
        ClientConfiguration clientConfig = new ClientConfiguration()
        config.config.each { k, v -> clientConfig[k] = v }
        def credentials = createCredentials(config, clientName)
        new AmazonSimpleDBClient(credentials, clientConfig)
    }

    private void stopSimpledb(ConfigObject config, AmazonSimpleDB client) {
        // empty ??
    }
    
    private createCredentials(ConfigObject config, String clientName) {
        if(config.credentialsProvider && config.credentialsProvider instanceof Class) return config.credentialsProvider.newInstance()
        if(config.credentials) return new SimpledbCredentials(config.credentials.accessKey, config.credentials.secretKey)
        throw new IllegalArgumentException("Invalid credentials configuration for client $clientName")
    }
    
    private static class SimpledbCredentials implements AWSCredentials {
        private final String accessKey
        private final String secretKey
        
        SimpledbCredentials(String accessKey, String secretKey) {
            this.accessKey = accessKey
            this.secretKey = secretKey
        }
        
        String getAWSAccessKeyId() { accessKey }
        String getAWSSecretKey() { secretKey }
    }
}
