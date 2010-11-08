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
package griffon.riak

import griffon.core.GriffonApplication
import griffon.util.Environment
import com.basho.riak.client.RiakConfig
import com.basho.riak.client.raw.RawClient

/**
 * @author Andres Almiray
 */
@Singleton
final class RiakConnector {
    private final Object lock = new Object()
    private boolean connected = false
    private bootstrap
    private GriffonApplication app

    ConfigObject createConfig(GriffonApplication app) {
        def configClass = app.class.classLoader.loadClass('RiakConfig')
        return new ConfigSlurper(Environment.current.name).parse(configClass)
    }

    void connect(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            if(connected) return
            connected = true
        }

        this.app = app
        app.event('RiakConnectStart', [config])
        RiakConnector.instance.startRiak(riakConfig)
        bootstrap = app.class.classLoader.loadClass('BootstrapRiak').newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(RawClientHolder.instance.rawClient)
        app.event('RiakConnectEnd', [RawClientHolder.instance.rawClient])
    }

    void disconnect(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            if(!connected) return
            connected = false
        }

        app.event('RiakDisconnectStart', [config, RawClientHolder.instance.rawClient])
        bootstrap.destroy(RawClientHolder.instance.rawClient)
        stopRiak(config)
        app.event('RiakDisconnectEnd')
    }

    private void startRiak(config) {
        RiakConfig riakConfig = new RiakConfig()
        ['url', 'timeout', 'maxConnections'].each { p ->
            if(config.raw[p]) riakConfig[p] = config.raw[p]
        }
        RawClientHolder.instance.rawClient = new RawClient(riakConfig)
    }

    private void stopRiak(config) {
    }

    def withRiak = { Closure closure ->
        RawClientHolder.instance.withRiak(closure)
    }
}
