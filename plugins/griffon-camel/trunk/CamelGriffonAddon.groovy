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

import org.apache.camel.spring.*
import org.apache.camel.model.*
import org.apache.camel.language.groovy.CamelGroovyMethods
import org.springframework.beans.factory.config.MethodInvokingFactoryBean

import griffon.core.GriffonApplication
import org.codehaus.griffon.runtime.camel.*

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andres Almiray
 */
class CamelGriffonAddon {
    private static final Logger log = LoggerFactory.getLogger('griffon.addon.camel.CamelGriffonAddon')

    void addonInit(GriffonApplication app) {
        app.artifactManager.registerArtifactHandler(new RouteArtifactHandler(app))
        
        log.debug "Adding dynamic methods to RouteBuilder helpers"
        ProcessorDefinition.metaClass.filter = { filter ->
            if (filter instanceof Closure) {
                filter = CamelGroovyMethods.toExpression(filter)
            }
            delegate.filter(filter)
        }

        ChoiceDefinition.metaClass.when = { filter ->
            if (filter instanceof Closure) {
                filter = CamelGroovyMethods.toExpression(filter)
            }
            delegate.when(filter)
        }

        ProcessorDefinition.metaClass.process = { filter ->
            if (filter instanceof Closure) {
                filter = new ClosureProcessor(filter)
            }
            delegate.process(filter)
        }
    }

    def doWithSpring = {
        def routeClasses = app.artifactManager.routeClasses

        log.debug "Configuring Routes"
        routeClasses.each { routeClass ->
            configureRouteBeans.delegate = delegate
            configureRouteBeans(routeClass)
        }

        xmlns camel:'http://camel.apache.org/schema/spring'

        camel.camelContext(id: 'camelContext') {
            routeClasses.each { routeClass ->
                camel.routeBuilder(ref: "${routeClass.fullName}Builder")
            }
            camel.template(id: 'producerTemplate')
        }
    }

    def whenSpringReady = { app ->
        def template = app.applicationContext.getBean('producerTemplate')

        def types = app.config.griffon?.camel?.injectInto ?: ['controller']
        types.each { type ->
            log.debug "Adding dynamic methods to ${type}"
            app.artifactManager.getClassesOfType(type).each { griffonClass ->
                griffonClass.metaClass.sendMessage = { endpoint, message ->
                    template.sendBody(endpoint, message)
                }
                griffonClass.metaClass.requestMessage = { endpoint, message ->
                    template.requestBody(endpoint, message)
                }                
            }    
        }
    }
    
    private configureRouteBeans = { routeClass ->
        "${routeClass.fullName}Builder"(GriffonRouteBuilderFactoryBean) {
            routeBean = ref(routeClass.propertyName)
        }
    }
}
