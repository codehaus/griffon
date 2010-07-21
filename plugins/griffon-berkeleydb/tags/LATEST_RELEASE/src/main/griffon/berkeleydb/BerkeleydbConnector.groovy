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
import griffon.util.Metadata
import griffon.util.Environment as GE
import com.sleepycat.je.*
import com.sleepycat.persist.*
import java.util.concurrent.ConcurrentHashMap

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * @author Andres.Almiray
 */
@Singleton
final class BerkeleydbConnector {
    private final Map ENTITY_STORES = new ConcurrentHashMap()
    private final Map DATABASES = new ConcurrentHashMap()
    private static final Log LOG = LogFactory.getLog(BerkeleydbConnector)
    private final Object lock = new Object()
    private boolean connected = false
    private bootstrap
    private GriffonApplication app

    final EnvironmentConfig envConfig = new EnvironmentConfig()

    def createConfig(GriffonApplication app) {
        def configClass = app.class.classLoader.loadClass('BerkeleydbConfig')
        return new ConfigSlurper(GE.current.name).parse(configClass)
    }

    void connect(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            if(connected) return
            connected = true
        }
        this.app = app
        BerkeleydbConnector.instance.startEnvironment(dbConfig)
        bootstrap = app.class.classLoader.loadClass('BootstrapBerkeleydb').newInstance()
        bootstrap.metaClass.app = app
        bootstrap.init(EnvironmentHolder.instance.environment)
    }

    void disconnect(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            if(!connected) return
            connected = false
        }

        bootstrap.destroy(EnvironmentHolder.instance.environment)
        stopEnvironment(config)
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
        Environment.metaClass.withEntityStore = this.withEntityStore
        Environment.metaClass.withBerkeleyDb = this.withBerkeleyDb
        Environment.metaClass.withBerkeleyCursor = this.withBerkeleyCursor
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

    def withBerkeleyEnv = { Closure closure ->
        EnvironmentHolder.instance.withBerkeleyEnv(closure)
    }

    def withEntityStore = { Map params = [:], Closure closure ->
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
                closure(storeBucket.store, txn)
                txn.commit()
            } catch(Exception ex) {
                txn.abort()
                throw ex
            }
        } else {
            closure(storeBucket.store, null)
        }
    }

    def withBerkeleyDb = { Map params = [:], Closure closure ->
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
                closure(dbBucket.db, txn)
                txn.commit()
            } catch(Exception ex) {
                txn.abort()
                throw ex
            }
        } else {
            closure(dbBucket.db, null)
        }
    }

    def withBerkeleyCursor = { Map params = [:], Closure closure ->
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
                closure(dbBucket.db.openCursor(txn, cursorConfig), txn)
                txn.commit()
            } catch(Exception ex) {
                txn.abort()
                throw ex
            }
        } else {
            closure(dbBucket.db.openCursor(null, cursorConfig), null)
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
