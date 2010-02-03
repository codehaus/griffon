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
import griffon.domain.DomainClassEnhancer
import com.db4o.*
import com.db4o.cs.*
import griffon.domain.db4o.Db4oDomainClassEnhancerDelegate

/**
 * @author Andres Almiray
 */
class Db4oGriffonAddon {
    private ObjectContainer objectContainer
    private ObjectServer objectServer
    private dbConfig
    private bootstrap

    def events = [
        LoadAddonEnd: { name, addon, app -> startupObjectContainer(app) },
        ShutdownStart: { app -> shutdownObjectContainer(app) },
    ]

    private void startupObjectContainer(GriffonApplication app) {
        dbConfig = parseConfig()
        int serverPort = Integer.parseInt(dbConfig?.dataSource?.server?.port ?: '65321')
        String dbfileName = dbConfig?.dataSource?.name ?: 'db.db4o'
        File dbfile = new File(dbfileName)
        if(!dbfile.absolute) dbfile = new File(System.getProperty('griffon.start.dir'), dbfileName)
        // db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), dbfile.absolutePath)
        // db = Db4oEmbedded.openFile(dbfile.canonicalPath)
        objectServer = Db4oClientServer.openServer(dbfile.canonicalPath, serverPort)
        objectContainer = objectServer.openClient()
        DomainClassEnhancer.instance.enhance(app, new Db4oDomainClassEnhancerDelegate(app, objectContainer))
        bootstrap = app.class.classLoader.loadClass("BootstrapDb4o").newInstance()
        bootstrap.init(app)
    }

    private void shutdownObjectContainer(GriffonApplication app) {
        bootstrap.destroy(app)
        objectContainer.close()
        objectServer.close()
    }

    private parseConfig() {
        def db4oConfigClass = app.class.classLoader.loadClass("Db4oConfig")
        return new ConfigSlurper(Environment.current.name).parse(db4oConfigClass)
    }
}
