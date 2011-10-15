/*
 * Copyright 2011 the original author or authors.
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
package griffon.plugins.voldemort

import voldemort.client.*

import griffon.core.GriffonApplication
import griffon.util.ApplicationHolder
import griffon.util.CallableWithArgs
import static griffon.util.GriffonNameUtils.isBlank

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andres Almiray
 */
@Singleton
class StoreClientHolder {
    private static final Logger LOG = LoggerFactory.getLogger(StoreClientHolder)
    private static final Object[] LOCK = new Object[0]
    private final Map<String, StoreClientFactory> storeClientFactories = [:]
    private final Map<String, StoreClient> storeClients = [:]

    Object withVoldemort(String clientName = 'default', Closure closure) {
        StoreClientFactory clientFactory = fetchStoreClientFactory(clientName)
        if(LOG.debugEnabled) LOG.debug("Executing statement on client '$clientName'")
        return closure(clientName, clientFactory)
    }

    public <T> T withVoldemort(String clientName = 'default', CallableWithArgs<T> callable) {
        StoreClientFactory clientFactory = fetchStoreClientFactory(clientName)
        if(LOG.debugEnabled) LOG.debug("Executing statement on client '$clientName'")
        callable.args = [clientName, clientFactory] as Object[]
        return callable.call()
    }

    Object withVoldemortStore(String clientName = 'default', String storeName, Closure closure) {
        StoreClient client = fetchStoreClient(clientName, storeName)
        if(LOG.debugEnabled) LOG.debug("Executing statement on client '$clientName' and store '$storeName'")
        return closure(clientName, storeName, client)
    }

    public <T> T withVoldemortStore(String clientName = 'default', String storeName, CallableWithArgs<T> callable) {
        StoreClient client = fetchStoreClient(clientName, storeName)
        if(LOG.debugEnabled) LOG.debug("Executing statement on client '$clientName' and store '$storeName'")
        callable.args = [clientName, storeName, client] as Object[]
        return callable.call()
    }
 
    String[] getStoreClientNames() {
        List<String> clientNames = new ArrayList().addAll(storeClientFactories.keySet())
        clientNames.toArray(new String[clientNames.size()])
    }

    StoreClientFactory getStoreClientFactory(String clientName = 'default') {
        if(isBlank(clientName)) clientName = 'default'
        retrieveStoreClientFactory(clientName)
    }

    void setStoreClientFactory(String clientName = 'default', StoreClientFactory storeClientFactory) {
        if(isBlank(clientName)) clientName = 'default'
        storeStoreClientFactory(clientName, storeClientFactory)       
    } 
   
    boolean isStoreClientFactoryConnected(String clientName) {
        if(isBlank(clientName)) clientName = 'default'
        retrieveStoreClientFactory(clientName) != null
    }
    
    void disconnectStoreClientFactory(String clientName) {
        if(isBlank(clientName)) clientName = 'default'
        storeStoreClientFactory(clientName, null)        
    }

    private StoreClient fetchStoreClient(String clientName, String storeName) {
        if(isBlank(storeName)) throw new IllegalArgumentException("Invalid store name [${storeName}]")
        if(isBlank(clientName)) clientName = 'default'
        synchronized(LOCK) {
            StoreClient client = retrieveStoreClient(storeName)
            if(client == null) {
                client = fetchStoreClientFactory(clientName).getStoreClient(storeName)
            }
            storeStoreClient(storeName, client)
            return client
        }
    }
    
    private StoreClientFactory fetchStoreClientFactory(String clientName) {
        StoreClientFactory clientFactory = retrieveStoreClientFactory(clientName)
        if(clientFactory == null) {
            GriffonApplication app = ApplicationHolder.application
            ConfigObject config = VoldemortConnector.instance.createConfig(app)
            clientFactory = VoldemortConnector.instance.connect(app, config, clientName)
        }

        if(clientFactory == null) {
            throw new IllegalArgumentException("No such StoreClientFactory configuration for name $clientName")
        }
        clientFactory
    }

    private StoreClientFactory retrieveStoreClientFactory(String clientName) {
        synchronized(LOCK) {
            storeClientFactories[clientName]
        }
    }

    private void storeStoreClientFactory(String clientName, StoreClientFactory storeClientFactory) {
        synchronized(LOCK) {
            storeClientFactories[clientName] = storeClientFactory
        }
    }

    private StoreClient retrieveStoreClient(String storeName) {
        synchronized(LOCK) {
            storeClients[storeName]
        }
    }

    private void storeStoreClient(String storeName, StoreClient storeClient) {
        synchronized(LOCK) {
            storeClients[storeName] = storeClient
        }
    }
}
