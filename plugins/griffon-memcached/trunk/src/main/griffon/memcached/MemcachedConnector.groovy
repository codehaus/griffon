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
package griffon.memcached

import griffon.core.GriffonApplication

/**
 * @author Andres Almiray
 */
@Singleton
final class MemcachedConnector {
    private final Object lock = new Object()
    private boolean connected = false
    private MemcachedConnectorDelegate connectorDelegate

    MemcachedConnectorDelegate getMemcachedConnectorDelegate(GriffonApplication app) {
        synchronized(lock) {
            if(!connectorDelegate) {
                connectorDelegate = instantiateConnectorDelegate(app)
            }
        }
        return connectorDelegate
    } 

    private MemcachedConnectorDelegate instantiateConnectorDelegate(GriffonApplication app) {
        String connectorType = app.config?.griffon?.memcached?.connector ?: 'java_memcached'
        String className = 'griffon.memcached.jmc.JavaMemcachedConnector'
        switch(connectorType.toUpperCase()) {
            case 'SPYMEMCACHED':
                className = 'griffon.memcached.spy.SpyMemcachedConnector'
                break
            case 'XMEMCACHED': 
                className = 'griffon.memcached.xmc.XMemcachedConnector'
        }
        app.class.classLoader.loadClass(className).newInstance()
    }

    ConfigObject createConfig(GriffonApplication app) {
        return getMemcachedConnectorDelegate(app).createConfig(app)
    }

    void connect(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            if(connected) return
            connected = true
        }

        getMemcachedConnectorDelegate(app).connect(app, config)
    }

    void disconnect(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            if(!connected) return
            connected = false
        }

        getMemcachedConnectorDelegate(app).disconnect(app, config)
    }

    def withMcc = MemcachedClientHolder.instance.withMcc
}
