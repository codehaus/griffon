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

package griffon.plugins.ebean

import com.avaje.ebean.EbeanServer

import griffon.core.GriffonApplication
import griffon.util.ApplicationHolder
import static griffon.util.GriffonNameUtils.isBlank

/**
 * @author Andres Almiray
 */
@Singleton
class EbeanServerHolder {
    private static final Object[] LOCK = new Object[0]
    private final Map<String, EbeanServer> ebeanServers = [:]

    String[] getEbeanServerNames() {
        List<String> ebeanServerNames = new ArrayList().addAll(ebeanServers.keySet())
        ebeanServerNames.toArray(new String[ebeanServerNames.size()])
    }

    EbeanServer getEbeanServer(String ebeanServerName = 'default') {
        if(isBlank(ebeanServerName)) ebeanServerName = 'default'
        retrieveEbeanServer(ebeanServerName)
    }

    void setEbeanServer(String ebeanServerName = 'default', EbeanServer ebeanServer) {
        if(isBlank(ebeanServerName)) ebeanServerName = 'default'
        storeEbeanServer(ebeanServerName, ebeanServer)       
    }

    void withEbean(String ebeanServerName = 'default', Closure closure) {
        EbeanServer ebeanServer = fetchEbeanServer(ebeanServerName)
        closure(ebeanServerName, ebeanServer)
    }
    
    boolean isEbeanServerAvailable(String ebeanServerName) {
        if(isBlank(ebeanServerName)) ebeanServerName = 'default'
        retrieveEbeanServer(ebeanServerName) != null
    }
    
    void disconnectEbeanServer(String ebeanServerName) {
        if(isBlank(ebeanServerName)) ebeanServerName = 'default'
        storeEbeanServer(ebeanServerName, null)        
    }

    private EbeanServer fetchEbeanServer(String ebeanServerName) {
        if(isBlank(ebeanServerName)) ebeanServerName = 'default'
        EbeanServer ebeanServer = retrieveEbeanServer(ebeanServerName)
        if(ebeanServer == null) {
            GriffonApplication app = ApplicationHolder.application
            ConfigObject config = EbeanConnector.instance.createConfig(app)
            ebeanServer = EbeanConnector.instance.connect(app, config, ebeanServerName)
        }
        
        if(ebeanServer == null) {
            throw new IllegalArgumentException("No such EbeanServer configuration for name $ebeanServerName")
        }
        ebeanServer
    }

    private EbeanServer retrieveEbeanServer(String ebeanServerName) {
        synchronized(LOCK) {
            ebeanServers[ebeanServerName]
        }
    }

    private void storeEbeanServer(String ebeanServerName, EbeanServer ebeanServer) {
        synchronized(LOCK) {
            ebeanServers[ebeanServerName] = ebeanServer
        }
    }
}
