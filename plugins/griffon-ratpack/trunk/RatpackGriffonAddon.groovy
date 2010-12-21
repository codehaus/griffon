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
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.mortbay.jetty.Server
import org.mortbay.jetty.servlet.Context
import com.bleedingwolf.ratpack.RatpackApp

import griffon.core.GriffonApplication
import griffon.util.Metadata
import griffon.plugins.ratpack.GriffonRatpackServlet
import org.codehaus.griffon.runtime.ratpack.RatpackAppArtifactHandler

/**
 * @author Andres Almiray
 */
class RatpackGriffonAddon {
    private static final Logger log = LoggerFactory.getLogger('griffon.addon.ratpack.RatpackGriffonAddon')
    private Server server
    
    void addonInit(GriffonApplication app) {
        app.artifactManager.registerArtifactHandler(new RatpackAppArtifactHandler(app))
    }
    
    void addonPostInit(GriffonApplication app) {
        int port = app.config.ratpack?.port ?: 5000i
        server = new Server(port)
        Context context = new Context(server, '/' + Metadata.current.getApplicationName(), Context.SESSIONS)
        
        app.artifactManager.ratpackClasses.each { ratpackAppClass ->
            def routes = ratpackAppClass.newInstance().routes
            RatpackApp ratpackApp = new RatpackApp()
            routes.resolveStrategy = Closure.DELEGATE_FIRST
            routes.delegate = ratpackApp
            routes()
            
            ratpackApp.config.templateRoot = 'ratpack/templates'
            ratpackApp.config.public = 'ratpack/public'
            ratpackApp.config.context = '/' + ratpackAppClass.logicalPropertyName
            
            GriffonRatpackServlet.configure(ratpackApp, context)
        }
        
        log.info("Starting Ratpack server on port $port")
        server.start()
    }
}
