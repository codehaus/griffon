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
package griffon.plugins.terrastore

import terrastore.client.TerrastoreClient

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
class TerrastoreClientHolder {
    private static final Logger LOG = LoggerFactory.getLogger(TerrastoreClientHolder)
    private static final Object[] LOCK = new Object[0]
    private final Map<String, TerrastoreClient> clients = [:]
  
    String[] getClientNames() {
        List<String> clientNames = new ArrayList().addAll(clients.keySet())
        clientNames.toArray(new String[clientNames.size()])
    }

    TerrastoreClient getClient(String clientName = 'default') {
        if(isBlank(clientName)) clientName = 'default'
        retrieveClient(clientName)
    }

    void setClient(String clientName = 'default', TerrastoreClient client) {
        if(isBlank(clientName)) clientName = 'default'
        storeClient(clientName, client)       
    }

    Object withTerrastore(String clientName = 'default', Closure closure) {
        TerrastoreClient client = fetchClient(clientName)
        if(LOG.debugEnabled) LOG.debug("Executing statement on client '$clientName'")
        return closure(clientName, client)
    }

    public <T> T withTerrastore(String clientName = 'default', CallableWithArgs<T> callable) {
        TerrastoreClient client = fetchClient(clientName)
        if(LOG.debugEnabled) LOG.debug("Executing statement on client '$clientName'")
        callable.args = [clientName, client] as Object[]
        return callable.call()
    }
    
    boolean isClientConnected(String clientName) {
        if(isBlank(clientName)) clientName = 'default'
        retrieveClient(clientName) != null
    }
    
    void disconnectClient(String clientName) {
        if(isBlank(clientName)) clientName = 'default'
        storeClient(clientName, null)        
    }

    private TerrastoreClient fetchClient(String clientName) {
        if(isBlank(clientName)) clientName = 'default'
        TerrastoreClient client = retrieveClient(clientName)
        if(client == null) {
            GriffonApplication app = ApplicationHolder.application
            ConfigObject config = TerrastoreConnector.instance.createConfig(app)
            client = TerrastoreConnector.instance.connect(app, config, clientName)
        }
        
        if(client == null) {
            throw new IllegalArgumentException("No such TerrastoreClient configuration for name $clientName")
        }
        client
    }

    private TerrastoreClient retrieveClient(String clientName) {
        synchronized(LOCK) {
            clients[clientName]
        }
    }

    private void storeClient(String clientName, TerrastoreClient client) {
        synchronized(LOCK) {
            clients[clientName] = client
        }
    }
}
