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
package griffon.neodatis

import griffon.core.GriffonApplication
import griffon.util.Environment
import org.neodatis.odb.*

/**
 * @author Andres Almiray
 */
@Singleton
final class NeodatisHelper {
    def parseConfig(GriffonApplication app) {
        def configClass = app.class.classLoader.loadClass('NeodatisConfig')
        return new ConfigSlurper(Environment.current.name).parse(configClass)
    }

    void startOdb(config) {
        boolean isClient = config.database?.client ?: false
        String alias = config.database?.alias ?: 'neodatis.odb'

        NeoDatisConfig neodatisConfig = NeoDatis.getConfig()
        config.database.config.each { key, value ->
            try {
                neodatisConfig[key] = value
                return
            } catch(MissingPropertyException mpe) {
                // ignore
            }
        }
        
        if(isClient) {
            OdbHolder.instance.odb = NeoDatis.openClient(alias, neodatisConfig)
        } else {
            File aliasFile = new File(alias)
            if(!aliasFile.absolute) aliasFile = new File(System.getProperty('griffon.start.dir'), alias)
            aliasFile.parentFile?.mkdirs()
            OdbHolder.instance.odb = NeoDatis.open(aliasFile.absolutePath, neodatisConfig)
        }
    }

    void stopOdb(config) {
        boolean isClient = config.database?.client ?: false
        String alias = config.database?.alias ?: 'neodatis/db.odb'

        File aliasFile = new File(alias)
        if(!aliasFile.absolute) aliasFile = new File(System.getProperty('griffon.start.dir'), alias)

        switch(Environment.current) {
            case Environment.DEVELOPMENT:
            case Environment.TEST:
                if(isClient) return
                // Runtime.getRuntime().addShutdownHook {
                    aliasFile.parentFile?.eachFileRecurse { f -> 
                        try { if(f?.exists()) f.delete() }
                        catch(IOException ioe) { /* ignore */ }
                    }
                    try { if(aliasFile?.exists()) aliasFile.delete() }
                    catch(IOException ioe) { /* ignore */ }
                // }
            default:
                OdbHolder.instance.odb.close()
        }
    }

    def withOdb = { Closure closure ->
        OdbHolder.instance.withOdb(closure)
    }
}
