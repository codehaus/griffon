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

package griffon.plugins.cmis

import griffon.core.GriffonApplication
import griffon.util.ApplicationHolder
import griffon.util.CallableWithArgs
import static griffon.util.GriffonNameUtils.isBlank

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.apache.chemistry.opencmis.client.api.Session

/**
 * @author Andres Almiray
 */
@Singleton
class SessionHolder {
    private static final Logger LOG = LoggerFactory.getLogger(SessionHolder)
    private static final Object[] LOCK = new Object[0]
    private final Map<String, Session> sessions = [:]
  
    String[] getSessionNames() {
        List<String> sessionNames = new ArrayList().addAll(sessions.keySet())
        sessionNames.toArray(new String[sessionNames.size()])
    }

    Session getSession(String sessionName = 'default') {
        if(isBlank(sessionName)) sessionName = 'default'
        retrieveSession(sessionName)
    }

    void setSession(String sessionName = 'default', Session session) {
        if(isBlank(sessionName)) sessionName = 'default'
        storeSession(sessionName, session)       
    }

    Object withCmis(String sessionName = 'default', Closure closure) {
        Session session = fetchSession(sessionName)
        if(LOG.debugEnabled) LOG.debug("Executing statement on session '$sessionName'")
        return closure(sessionName, session)
    }

    public <T> T withCmis(String sessionName = 'default', CallableWithArgs<T> callable) {
        Session session = fetchSession(sessionName)
        if(LOG.debugEnabled) LOG.debug("Executing statement on session '$sessionName'")
        callable.args = [sessionName, session] as Object[]
        return callable.call()
    }
    
    boolean isSessionConnected(String sessionName) {
        if(isBlank(sessionName)) sessionName = 'default'
        retrieveSession(sessionName) != null
    }
    
    void disconnectSession(String sessionName) {
        if(isBlank(sessionName)) sessionName = 'default'
        storeSession(sessionName, null)        
    }

    private Session fetchSession(String sessionName) {
        if(isBlank(sessionName)) sessionName = 'default'
        Session session = retrieveSession(sessionName)
        if(session == null) {
            GriffonApplication app = ApplicationHolder.application
            ConfigObject config = CmisConnector.instance.createConfig(app)
            session = CmisConnector.instance.connect(app, config, sessionName)
        }
        
        if(session == null) {
            throw new IllegalArgumentException("No such Session configuration for name $sessionName")
        }
        session
    }

    private Session retrieveSession(String sessionName) {
        synchronized(LOCK) {
            sessions[sessionName]
        }
    }

    private void storeSession(String sessionName, Session session) {
        synchronized(LOCK) {
            sessions[sessionName] = session
        }
    }
}
