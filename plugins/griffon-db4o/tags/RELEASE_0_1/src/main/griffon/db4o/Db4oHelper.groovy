/* --------------------------------------------------------------------
   griffon-db4o plugin
   Copyright (C) 2010 Andres Almiray

   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License as
   published by the Free Software Foundation; either version 2 of the
   License, or (at your option) any later version.

   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this library; if not, see <http://www.gnu.org/licenses/>.
   ---------------------------------------------------------------------
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

    void stopObjectContainer(dbConfig) {
        ObjectContainerHolder.instance.objectContainer.close()

        String dbfileName = dbConfig?.dataSource?.name ?: 'db.yarv'
        File dbfile = new File(dbfileName)
        if(!dbfile.absolute) dbfile = new File(System.getProperty('griffon.start.dir'), dbfileName)
        switch(Environment.current) {
            case Environment.DEVELOPMENT:
            case Environment.TEST:
                dbfile.delete()
        }
    }

    def withDb4o = { Closure closure ->
        closure(ObjectContainerHolder.instance.objectContainer)
    }
}
