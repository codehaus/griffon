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
import org.apache.chemistry.opencmis.client.api.Session

/**
 * @author Andres Almiray
 */
@Singleton
class SessionHolder {
    Session session
    final Map<String, Session> sessions = [:]

    def withCmis = { String sessionName = 'default', Closure closure ->
        Session s = sessions[sessionName]
        if(s == null) {
            GriffonApplication app = ApplicationHolder.application
            ConfigObject config = CmisConnector.instance.createConfig(app)
            s = CmisConnector.instance.connect(app, config, sessionName)
        }
        
        if(s == null) {
            throw new IllegalArgumentException("No such OpenCMIS Session configuration for name $sessionName")
        }
        closure(s)
    }
}
