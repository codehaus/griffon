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

import griffon.core.GriffonApplication
import griffon.util.Environment
import griffon.berkeleydb.BerkeleydbHelper
import griffon.berkeleydb.EnvironmentHolder

/**
 * @author Andres Almiray
 */
class BerkeleydbGriffonAddon {
    private bootstrap

    def events = [
        BootstrapEnd: { app -> start(app) },
        ShutdownStart: { app -> stop(app) },
        NewInstance: { klass, type, instance ->
            def types = app.config.griffon?.berkeleydb?.injectInto ?: ['controller']
            if(!types.contains(type)) return
            instance.metaClass.withBerkeleydb = BerkeleydbHelper.instance.withBerkeleydb
            instance.metaClass.withEntityStore = BerkeleydbHelper.instance.withEntityStore
        }
    ]

    private void start(GriffonApplication app) {
        def dbConfig = BerkeleydbHelper.instance.parseConfig(app)
        BerkeleydbHelper.instance.startEnvironment(dbConfig)
        bootstrap = app.class.classLoader.loadClass('BootstrapBerkeleydb').newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(EnvironmentHolder.instance.environment)
    }

    private void stop(GriffonApplication app) {
        bootstrap.destroy(EnvironmentHolder.instance.environment)
        def dbConfig = BerkeleydbHelper.instance.parseConfig(app)
        BerkeleydbHelper.instance.stopEnvironment(dbConfig)
    }
}
