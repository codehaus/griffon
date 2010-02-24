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
package griffon.berkeleydb

import griffon.core.GriffonApplication
import griffon.util.Environment GE
import com.sleepycat.je.*
import com.sleepycat.persist.*
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Andres.Almiray
 */
@Singleton
final class BerkeleydbHelper {
    private final Map ENTIY_STORES = [:]

    def parseConfig(GriffonApplication app) {
        def berkeleydbConfigClass = app.class.classLoader.loadClass('BerkeleydbConfig')
        return new ConfigSlurper(GE.current.name).parse(berkeleydbConfigClass)
    }

    void startEnvironment(dbConfig) {
        String envhome = dbConfig.environment?.home ?: 'bdb_home'
        File envhomeDir = new File(envhome)
        if(!envhomeDir.absolute) envhomeDir = new File(System.getProperty('griffon.start.dir'), envhomeDir)
        EnvironmentConfig envConfig = new EnvironmentConfig()
        dbconfig.environment.each { key, value ->
            if(key == 'home') return

            // 1 - attempt direct property setter
            try {
                envConfig[key] = value
                return
            } catch(MissingPropertyException mpe) {
                // ignore
            }

            // 2 - attempt field resolution
            try {
                String rkey = EnvironmentConfig.@"${key.toUpperCase()).replaceAll(' ','_')}"
                envConfig.setConfigParam(rkey, value?.toString())
                return
            } catch(Exception mpe) {
                // ignore
            }

            // 3 - assume key is a valid config param
            envConfig.setConfigParam(key, value?.toString())
        }
        EnvironmentHolder.instance.environment = new Environment(envhomeDir, envConfig)
    }

    void stopEnvironment(dbConfig) {
        EnvironmentHelper.instance.environment.close()
    }

    def withBerkeleydb = { Closure closure ->
        EnvironmentHolder.instance.withBerkeleydb(closure)
    }

    def withEntityStore = { Map params = [:], Closure closure ->

    }
}
