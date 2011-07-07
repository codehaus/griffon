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

import org.apache.chemistry.opencmis.client.api.Session
import org.apache.chemistry.opencmis.client.api.SessionFactory
import org.apache.chemistry.opencmis.client.runtime.SessionFactoryImpl

import griffon.core.GriffonApplication
import griffon.util.Environment
import static griffon.util.GriffonNameUtils.isBlank

import org.apache.commons.logging.Log
import org.apache.commons.logging.LogFactory

/**
 * @author Andres.Almiray
 */
@Singleton
final class CmisConnector {
    private static final Log LOG = LogFactory.getLog(CmisConnector)
    final SessionFactory sessionFactory = SessionFactoryImpl.newInstance()
    private final Object lock = new Object()
    private final Map<String, Boolean> connections = [:]
    private GriffonApplication app

    ConfigObject createConfig(GriffonApplication app) {
        def configClass = app.class.classLoader.loadClass('CmisConfig')
        return new ConfigSlurper(Environment.current.name).parse(configClass)
    }

    Session connect(GriffonApplication app, ConfigObject config, String sessionName = 'default') {
        synchronized(lock) {
            if(connections[sessionName]) return
        }

        this.app = app
        app.event('CmisConnectStart', [config, sessionName])
        Session session = createSession(config, sessionName)
        synchronized(lock) {
           connections[sessionName] = true
        }
        app.event('CmisConnectEnd', [sessionName, session])
    }

    void disconnect(GriffonApplication app, ConfigObject config, String sessionName = 'default') {
        synchronized(lock) {
            if(!connections[sessionName]) return
        }

        app.event('CmisDisconnectStart', [config, sessionName, SessionHolder.instance.sessions[sessionName]])
        SessionHolder.instance.sessions[sessionName] = null
        synchronized(lock) {
            connections[sessionName] = false
        }
        app.event('CmisDisconnectEnd', [sessionName])
    }

    void disconnectAll(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            connections.each { sessionName, state ->
                if(!state) return
                app.event('CmisDisconnectStart', [config, sessionName, SessionHolder.instance.sessions[sessionName]])
                SessionHolder.instance.sessions[sessionName] = null
                connections[sessionName] = false
                app.event('CmisDisconnectEnd', [sessionName])
            }
        }
    }

    Session createSession(ConfigObject config, String sessionName = 'default') {
        Map parameters = sessionName == 'default' ? config.session : config.sessions[sessionName]
        Session session = sessionFactory.createSession(parameters)
        SessionHolder.instance.sessions[sessionName] = session
        if(sessionName == 'default') SessionHolder.instance.session = session
        session
    }

    def withCmis = SessionHolder.instance.withCmis
}
