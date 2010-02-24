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
import griffon.riak.RiakHelper
import griffon.riak.RawClientHolder

/**
 * @author Andres Almiray
 */
class RiakGriffonAddon {
    private bootstrap

    def events = [
        BootstrapEnd: { app -> start(app) },
        ShutdownStart: { app -> stop(app) },
        NewInstance: { klass, type, instance ->
            def types = app.config.griffon?.riak?.injectInto ?: ['controller']
            if(!types.contains(type)) return
            instance.metaClass.withRiak = RiakHelper.instance.withRiak
        }
    ]

    private void start(GriffonApplication app) {
        RiakHelper.instance.metaClass.app = app
        def riakConfig = RiakHelper.instance.parseConfig(app)
        RiakHelper.instance.startRiak(riakConfig)
        bootstrap = app.class.classLoader.loadClass('BootstrapRiak').newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(RawClientHolder.instance.rawClient)
    }

    private void stop(GriffonApplication app) {
        bootstrap.destroy(RawClientHolder.instance.rawClient)
        def riakConfig = RiakHelper.instance.parseConfig(app)
        RiakHelper.instance.stopRiak(riakConfig)
    }
}
