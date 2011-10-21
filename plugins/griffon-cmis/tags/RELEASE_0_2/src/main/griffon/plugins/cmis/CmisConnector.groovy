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
import griffon.util.Metadata
import griffon.util.CallableWithArgs

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andres Almiray
 */
@Singleton
final class CmisConnector {
    final SessionFactory sessionFactory = SessionFactoryImpl.newInstance()
    private static final Logger LOG = LoggerFactory.getLogger(CmisConnector)

    static void enhance(MetaClass mc) {
        mc.withCmis = {Closure closure ->
            SessionHolder.instance.withCmis('default', closure)
        }
        mc.withCmis << {String sessionName, Closure closure ->
            SessionHolder.instance.withCmis(sessionName, closure)
        }
        mc.withCmis << {CallableWithArgs callable ->
            SessionHolder.instance.withCmis('default', callable)
        }
        mc.withCmis << {String sessionName, CallableWithArgs callable ->
            SessionHolder.instance.withCmis(sessionName, callable)
        }
    }

    Object withCmis(String sessionName = 'default', Closure closure) {
        return SessionHolder.instance.withCmis(sessionName, closure)
    }

    public <T> T withCmis(String sessionName = 'default', CallableWithArgs<T> callable) {
        return SessionHolder.instance.withCmis(sessionName, callable)
    }

    // ======================================================

    ConfigObject createConfig(GriffonApplication app) {
        def configClass = app.class.classLoader.loadClass('CmisConfig')
        return new ConfigSlurper(Environment.current.name).parse(configClass)
    }

    private ConfigObject narrowConfig(ConfigObject config, String sessionName) {
        return sessionName == 'default' ? config.session : config.sessions[sessionName]
    }

    Session connect(GriffonApplication app, ConfigObject config, String sessionName = 'default') {
        if (SessionHolder.instance.isSessionConnected(sessionName)) {
            return SessionHolder.instance.getSession(sessionName)
        }

        config = narrowConfig(config, sessionName)
        app.event('CmisConnectStart', [config, sessionName])
        Session s = createSession(config)
        SessionHolder.instance.setSession(sessionName, s)
        app.event('CmisConnectEnd', [sessionName, s])
        s
    }

    void disconnect(GriffonApplication app, ConfigObject config, String sessionName = 'default') {
        if (SessionHolder.instance.isSessionConnected(sessionName)) {
            config = narrowConfig(config, sessionName)
            Session s = SessionHolder.instance.getSession(sessionName)
            app.event('CmisDisconnectStart', [config, sessionName, s])
            app.event('CmisDisconnectEnd', [config, sessionName])
            SessionHolder.instance.disconnectSession(sessionName)
        }
    }


    Session createSession(ConfigObject config, String sessionName = 'default') {
        sessionFactory.createSession(config)
    }
}
