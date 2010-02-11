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
package griffon.db4o

import griffon.core.GriffonApplication
import griffon.util.Environment
import com.db4o.*

/**
 * @author Andres.Almiray
 */
@Singleton
final class Db4oHelper {
    def parseConfig(GriffonApplication app) {
        def db4oConfigClass = app.class.classLoader.loadClass("Db4oConfig")
        return new ConfigSlurper(Environment.current.name).parse(db4oConfigClass)
    }

    void startObjectContainer(dbConfig) {
        String dbfileName = dbConfig?.dataSource?.name ?: 'db.yarv'
        File dbfile = new File(dbfileName)
        if(!dbfile.absolute) dbfile = new File(System.getProperty('griffon.start.dir'), dbfileName)
        ObjectContainerHolder.instance.objectContainer = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), dbfile.absolutePath)
    }

    def withDb4o = { Closure closure ->
        closure(ObjectContainerHolder.instance.objectContainer)
    }
}
