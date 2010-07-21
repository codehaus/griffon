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
import griffon.util.Environment

/**
 * @author Andres Almiray
 */
abstract class MemcachedConnectorDelegate {
    protected bootstrap

    abstract String getConfigClassName()
    abstract String getBootstrapClassName()
    abstract void doConnect(GriffonApplication app, ConfigObject config)
    abstract void doDisconnect(GriffonApplication app, ConfigObject config)

    ConfigObject createConfig(GriffonApplication app) {
        def configClass = app.class.classLoader.loadClass(getConfigClassName())
        return new ConfigSlurper(Environment.current.name).parse(configClass)
    }

    void connect(GriffonApplication app, ConfigObject config) {
        doConnect(app, config)
        bootstrap = app.class.classLoader.loadClass(getBootstrapClassName()).newInstance()
        bootstrap.metaClass.app = app
        withMcc { mcc -> bootstrap.init(mcc) }
    }

    void disconnect(GriffonApplication app, ConfigObject config) {
        withMcc { mcc -> bootstrap.destroy(mcc) }
        doDisconnect(app, config)
    }

    def withMcc = MemcachedClientHolder.instance.withMcc
}
