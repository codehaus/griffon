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
import griffon.db4o.Db4oHelper
import griffon.db4o.ObjectContainerHolder
import com.db4o.*

/**
 * @author Andres Almiray
 */
class Db4oGriffonAddon {
    private bootstrap

    def events = [
        BootstrapEnd: { app -> start(app) },
        ShutdownStart: { app -> stop(app) },
        NewInstance: { klass, type, instance ->
            def types = app.config.griffon?.db4o?.injectInto ?: ['controller']
            if(!types.contains(type)) return
            instance.metaClass.withDb4o = Db4oHelper.instance.withDb4o
        }
    ]

    private void start(GriffonApplication app) {
        def dbConfig = Db4oHelper.instance.parseConfig(app)
        Db4oHelper.instance.startObjectContainer(dbConfig)
        bootstrap = app.class.classLoader.loadClass("BootstrapDb4o").newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(ObjectContainerHolder.instance.objectContainer)
    }

    private void stop(GriffonApplication app) {
        bootstrap.destroy(ObjectContainerHolder.instance.objectContainer)
        ObjectContainerHolder.instance.objectContainer.close()
    }
}
