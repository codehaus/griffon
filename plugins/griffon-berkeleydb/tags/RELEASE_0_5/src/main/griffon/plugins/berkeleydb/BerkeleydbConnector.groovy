/*
 * Copyright 2010-2011 the original author or authors.
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
package griffon.plugins.berkeleydb

import griffon.core.GriffonApplication
import griffon.util.Metadata
import griffon.util.CallableWithArgs
import griffon.util.Environment as GE
import com.sleepycat.je.*
import com.sleepycat.persist.*
import java.util.concurrent.ConcurrentHashMap

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andres Almiray
 */
@Singleton
final class BerkeleydbConnector {
    private final Map ENTITY_STORES = new ConcurrentHashMap()
    private final Map DATABASES = new ConcurrentHashMap()
    private static final Logger LOG = LoggerFactory.getLogger(BerkeleydbConnector)
    private final Object[] lock = new Object[0]
    private boolean connected = false
    private bootstrap
    private GriffonApplication app
    private final ConfigSlurper configSlurper = new ConfigSlurper(GE.current.name)

    final EnvironmentConfig envConfig = new EnvironmentConfig()

    static void enhance(MetaClass mc) {
        mc.withBerkeleyEnv = {Closure closure ->
            EnvironmentHolder.instance.withBerkeleyEnv(closure)
        }
        mc.withBerkeleyEnv << {CallableWithArgs callable ->
            EnvironmentHolder.instance.withBerkeleyEnv(callable)
        }
        mc.withEntityStore = {Map params = [:], Closure closure ->
            BerkeleydbConnector.instance.withEntityStore(params, closure)
        }
        mc.withEntityStore << {Map params = [:], CallableWithArgs callable ->
            BerkeleydbConnector.instance.withEntityStore(params, callable)
        }
        mc.withBerkeleyDb = {Map params = [:], Closure closure ->
            BerkeleydbConnector.instance.withBerkeleyDb(params, closure)
        }
        mc.withBerkeleyDb << {Map params = [:], CallableWithArgs callable ->
            BerkeleydbConnector.instance.withBerkeleyDb(params, callable)
        }
        mc.withBerkeleyCursor = {Map params = [:], Closure closure ->
            BerkeleydbConnector.instance.withBerkeleyCursor(params, closure)
        }
        mc.withBerkeleyCursor << {Map params = [:], CallableWithArgs callable ->
            BerkeleydbConnector.instance.withBerkeleyCursor(params, callable)
        }
    }

    Object withBerkeleyEnv(Closure closure) {
        EnvironmentHolder.instance.withBerkeleyEnv(closure)
    }
    
    public <T> T withBerkeleyEnv(CallableWithArgs<T> callable) {
        return EnvironmentHolder.instance.withBerkeleyEnv(callable)
    }

    Object withEntityStore(Map params = [:], Closure closure) {
        if(!params.id) {
            throw new IllegalArgumentException('You must specify a value for id: when using withEntityStore().')
        }

        Map storeBucket = ENTITY_STORES[params.id]
        if(!storeBucket) {
            storeBucket = createStoreBucket(params.id)
            ENTITY_STORES[params.id] = storeBucket
        }

        if(storeBucket.store.config.transactional) {
            TransactionConfig txnConfig = params.txnConfig ?: storeBucket.txnConfig
            Transaction txn = EnvironmentHolder.instance.environment.beginTransaction(null, txnConfig)
            try {
                Object result = closure(storeBucket.store, txn)
                txn.commit()
                return result
            } catch(Exception ex) {
                txn.abort()
                throw ex
            }
        } else {
            return closure(storeBucket.store, null)
        }
    }

    public <T> T withEntityStore(Map params = [:], CallableWithArgs<T> callable) {
        if(!params.id) {
            throw new IllegalArgumentException('You must specify a value for id: when using withEntityStore().')
        }

        Map storeBucket = ENTITY_STORES[params.id]
        if(!storeBucket) {
            storeBucket = createStoreBucket(params.id)
            ENTITY_STORES[params.id] = storeBucket
        }

        if(storeBucket.store.config.transactional) {
            TransactionConfig txnConfig = params.txnConfig ?: storeBucket.txnConfig
            Transaction txn = EnvironmentHolder.instance.environment.beginTransaction(null, txnConfig)
            try {
                callable.args = [storeBucket.store, txn] as Object[]
                T result = callable.run()
                txn.commit()
                return result
            } catch(Exception ex) {
                txn.abort()
                throw ex
            }
        } else {
            callable.args = [storeBucket.store, null] as Object[]
            return callable.run()
        }
    }
    
    Object withBerkeleyDb(Map params = [:], Closure closure) {
        if(!params.id) {
            throw new IllegalArgumentException('You must specify a value for id: when using withBerkeleyDb().')
        }

        Map dbBucket = DATABASES[params.id]
        if(!dbBucket) {
            dbBucket = createDatabaseBucket(params.id)
            DATABASES[params.id] = dbBucket
        }

        if(dbBucket.db.config.transactional) {
            TransactionConfig txnConfig = params.txnConfig ?: dbBucket.txnConfig
            Transaction txn = EnvironmentHolder.instance.environment.beginTransaction(null, txnConfig)
            try {
                Object result = closure(dbBucket.db, txn)
                txn.commit()
                return result
            } catch(Exception ex) {
                txn.abort()
                throw ex
            }
        } else {
            return closure(dbBucket.db, null)
        }
    }

    public <T> T withBerkeleyDb(Map params = [:], CallableWithArgs<T> callable) {
        if(!params.id) {
            throw new IllegalArgumentException('You must specify a value for id: when using withBerkeleyDb().')
        }

        Map dbBucket = DATABASES[params.id]
        if(!dbBucket) {
            dbBucket = createDatabaseBucket(params.id)
            DATABASES[params.id] = dbBucket
        }

        if(dbBucket.db.config.transactional) {
            TransactionConfig txnConfig = params.txnConfig ?: dbBucket.txnConfig
            Transaction txn = EnvironmentHolder.instance.environment.beginTransaction(null, txnConfig)
            try {
                callable.args = [dbBucket.db, txn] as Object[]
                T result = callable.run()
                txn.commit()
                return result
            } catch(Exception ex) {
                txn.abort()
                throw ex
            }
        } else {
            callable.args = [dbBucket.db, null] as Object[]
            return callable.run()
        }
    }

    Object withBerkeleyCursor(Map params = [:], Closure closure) {
        if(!params.id) {
            throw new IllegalArgumentException('You must specify a value for id: when using withBerkeleyCursor().')
        }

        Map dbBucket = DATABASES[params.id]
        if(!dbBucket) {
            dbBucket = createDatabaseBucket(params.id)
            DATABASES[params.id] = dbBucket
        }

        if(dbBucket.db.config.transactional) {
            TransactionConfig txnConfig = params.txnConfig ?: dbBucket.txnConfig
            CursorConfig cursorConfig = params.cursorConfig ?: dbBucket.cursorConfig
            Transaction txn = EnvironmentHolder.instance.environment.beginTransaction(null, txnConfig)
            try {
                Object result = closure(dbBucket.db.openCursor(txn, cursorConfig), txn)
                txn.commit()
                return result
            } catch(Exception ex) {
                txn.abort()
                throw ex
            }
        } else {
            return closure(dbBucket.db.openCursor(null, cursorConfig), null)
        }
    }

    public <T> T withBerkeleyCursor(Map params = [:], CallableWithArgs<T> callable) {
        if(!params.id) {
            throw new IllegalArgumentException('You must specify a value for id: when using withBerkeleyCursor().')
        }

        Map dbBucket = DATABASES[params.id]
        if(!dbBucket) {
            dbBucket = createDatabaseBucket(params.id)
            DATABASES[params.id] = dbBucket
        }

        if(dbBucket.db.config.transactional) {
            TransactionConfig txnConfig = params.txnConfig ?: dbBucket.txnConfig
            CursorConfig cursorConfig = params.cursorConfig ?: dbBucket.cursorConfig
            Transaction txn = EnvironmentHolder.instance.environment.beginTransaction(null, txnConfig)
            try {
                callable.args = [dbBucket.db.openCursor(txn, cursorConfig), txn] as Object[]
                T result = callable.run()
                txn.commit()
                return result
            } catch(Exception ex) {
                txn.abort()
                throw ex
            }
        } else {
            callable.args = [dbBucket.db.openCursor(null, cursorConfig), null] as Object[]
            return callable.run()
        }
    }

    // ======================================================

    ConfigObject createConfig(GriffonApplication app) {
        def configClass = app.class.classLoader.loadClass('BerkeleydbConfig')
        return configSlurper.parse(configClass)
    }

    void connect(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            if(connected) return
            connected = true
        }

        this.app = app
        app.event('BerkeleydbConnectStart', [config])
        BerkeleydbConnector.instance.startEnvironment(config)
        bootstrap = app.class.classLoader.loadClass('BootstrapBerkeleydb').newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(EnvironmentHolder.instance.environment)
        app.event('BerkeleydbConnectEnd', [EnvironmentHolder.instance.environment])
    }

    void disconnect(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            if(!connected) return
            connected = false
        }

        app.event('BerkeleydbDisconnectStart', [config, EnvironmentHolder.instance.environment])
        bootstrap.destroy(EnvironmentHolder.instance.environment)
        stopEnvironment(config)
        app.event('BerkeleydbDisconnectEnd')
    }

    private void startEnvironment(config) {
        String envhome = config.environment?.home ?: 'bdb_home'
        File envhomeDir = new File(envhome)
        if(!envhomeDir.absolute) envhomeDir = new File(Metadata.current.getGriffonWorkingDir(), envhome)
        envhomeDir.mkdirs()
        config.environment.each { key, value ->
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
                String rkey = EnvironmentConfig.@"${key.toUpperCase().replaceAll(' ','_')}"
                envConfig.setConfigParam(rkey, value?.toString())
                return
            } catch(Exception mpe) {
                // ignore
            }

            // 3 - assume key is a valid config param
            envConfig.setConfigParam(key, value?.toString())
        }
        enhance(Environment.metaClass)
        EnvironmentHolder.instance.environment = new Environment(envhomeDir, envConfig)
    }

    private void stopEnvironment(config) {
        ENTITY_STORES.each { id, storeBucket -> storeBucket.store.close() }
        DATABASES.each { id, dbBucket ->
            dbBucket.db.with {
                sync()
                close()
            }
        }
        EnvironmentHolder.instance.environment.with {
            cleanLog()
            close()
        }
    }

    private Map createStoreBucket(String storeId) {
        def config = createConfig(app)
        StoreConfig storeConfig = new StoreConfig()
        TransactionConfig txnConfig = new TransactionConfig()

        config.entityStores?.get(storeId)?.each { skey, svalue ->
            if(skey == 'transactionConfig') {
                svalue.each { tkey, tvalue ->
                    txnConfig[tkey] = tvalue
                }
            } else {
                storeConfig[skey] = svalue
            }
        }

        [store: new EntityStore(EnvironmentHolder.instance.environment, storeId, storeConfig),
         txnConfig: txnConfig]
    }

    private Map createDatabaseBucket(String dbId) {
        def config = createConfig(app)
        DatabaseConfig dbConfig = new DatabaseConfig()
        TransactionConfig txnConfig = new TransactionConfig()
        CursorConfig cursorConfig = new CursorConfig()

        config.databases?.get(dbId)?.each { skey, svalue ->
            if(skey == 'transactionConfig') {
                svalue.each { tkey, tvalue ->
                    txnConfig[tkey] = tvalue
                }
            } else if(skey == 'cursorConfig') {
                svalue.each { ckey, cvalue ->
                    cursorConfig[ckey] = cvalue
                }
            } else {
                dbConfig[skey] = svalue
            }
        }

        [db: new Database(EnvironmentHolder.instance.environment, dbId, dbConfig),
         txnConfig: txnConfig, cursorConfig: cursorConfig]
    }
}
