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
import griffon.util.Metadata
import griffon.util.CallableWithArgs

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.neodatis.odb.*

/**
 * @author Andres Almiray
 */
@Singleton
final class NeodatisConnector {
    private bootstrap

    private static final Logger LOG = LoggerFactory.getLogger(NeodatisConnector)

    static void enhance(MetaClass mc) {
        mc.withOdb = {Closure closure ->
            OdbHolder.instance.withOdb('default', closure)
        }
        mc.withOdb << {String databaseName, Closure closure ->
            OdbHolder.instance.withOdb(databaseName, closure)
        }
        mc.withOdb << {CallableWithArgs callable ->
            OdbHolder.instance.withOdb('default', callable)
        }
        mc.withOdb << {String databaseName, CallableWithArgs callable ->
            OdbHolder.instance.withOdb(databaseName, callable)
        }
    }

    Object withOdb(String databaseName = 'default', Closure closure) {
        return OdbHolder.instance.withOdb(databaseName, closure)
    }

    public <T> T withOdb(String databaseName = 'default', CallableWithArgs<T> callable) {
        return OdbHolder.instance.withOdb(databaseName, callable)
    }

    // ======================================================

    ConfigObject createConfig(GriffonApplication app) {
        def databaseClass = app.class.classLoader.loadClass('NeodatisConfig')
        new ConfigSlurper(Environment.current.name).parse(databaseClass)
    }

    private ConfigObject narrowConfig(ConfigObject config, String databaseName) {
        return databaseName == 'default' ? config.database : config.databases[databaseName]
    }

    ODB connect(GriffonApplication app, ConfigObject config, String databaseName = 'default') {
        if (OdbHolder.instance.isDatabaseConnected(databaseName)) {
            return OdbHolder.instance.getDatabase(databaseName)
        }

        config = narrowConfig(config, databaseName)
        app.event('NeodatisConnectStart', [config, databaseName])
        ODB db = startOdb(config)
        OdbHolder.instance.setDatabase(databaseName, db)
        bootstrap = app.class.classLoader.loadClass('BootstrapNeodatis').newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(databaseName, db)
        app.event('NeodatisConnectEnd', [databaseName, db])
        db
    }

    void disconnect(GriffonApplication app, ConfigObject config, String databaseName = 'default') {
        if (OdbHolder.instance.isDatabaseConnected(databaseName)) {
            config = narrowConfig(config, databaseName)
            ODB db = OdbHolder.instance.getDatabase(databaseName)
            app.event('DatabaseDisconnectStart', [config, databaseName, db])
            bootstrap.destroy(databaseName, db)
            stopOdb(config, db)
            app.event('DatabaseDisconnectEnd', [config, databaseName])
            OdbHolder.instance.disconnectDatabase(databaseName)
        }
    }

    private ODB startOdb(ConfigObject config) {
        boolean isClient = config.client ?: false
        String alias = config.alias ?: 'neodatis.odb'

        NeoDatisConfig neodatisConfig = NeoDatis.getConfig()
        config.config.each { key, value ->
            try {
                neodatisConfig[key] = value
                return
            } catch(MissingPropertyException mpe) {
                // ignore
            }
        }
        
        if(isClient) {
            return NeoDatis.openClient(alias, neodatisConfig)
        } else {
            File aliasFile = new File(alias)
            if(!aliasFile.absolute) aliasFile = new File(Metadata.current.getGriffonWorkingDir(), alias)
            aliasFile.parentFile?.mkdirs()
            return NeoDatis.open(aliasFile.absolutePath, neodatisConfig)
        }
    }

    private void stopOdb(ConfigObject config, ODB db) {
        boolean isClient = config.client ?: false
        String alias = config.alias ?: 'neodatis/db.odb'

        File aliasFile = new File(alias)
        if(!aliasFile.absolute) aliasFile = new File(Metadata.current.getGriffonWorkingDir(), alias)

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
                db.close()
        }
    }
}
