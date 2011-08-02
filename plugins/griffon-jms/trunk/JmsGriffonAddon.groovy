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

import griffon.core.GriffonApplication
import griffon.jms.*
import griffon.jms.listener.*

import griffon.spring.ApplicationContextHolder

import griffon.util.GriffonClassUtils
import griffon.jms.listener.ServiceInspector
import griffon.jms.listener.ListenerConfigFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.beans.factory.InitializingBean

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Luke Daley
 * @author Andres Almiray
 */
class JmsGriffonAddon {
    private static final Logger LOG = LoggerFactory.getLogger('griffon.addon.jms.JmsGriffonAddon')
    static final DEFAULT_CONNECTION_FACTORY_BEAN_NAME = "jmsConnectionFactory"
    
    private listenerConfigs = [:]
    private serviceInspector = new ServiceInspector()
    private listenerConfigFactory = new ListenerConfigFactory()
    
    private getListenerConfigs(serviceClass, application) {
        LOG.info("inspecting '${serviceClass.name}' for JMS listeners")
        serviceInspector.getListenerConfigs(serviceClass, listenerConfigFactory, application)
    }
    
    private registerListenerConfig(listenerConfig, beanBuilder) {
        def methodOrClosure = (listenerConfig.listenerIsClosure) ? "closure" : "method"
        def queueOrTopic = (listenerConfig.topic) ? "TOPIC" : "QUEUE"
        LOG.info "registering listener for ${methodOrClosure} '${listenerConfig.listenerMethodOrClosureName}' of service '${listenerConfig.serviceBeanPrefix}' to ${queueOrTopic} '${listenerConfig.destinationName}'"
        listenerConfig.register(beanBuilder)        
    }
    
    def doWithSpring = {
        app.artifactManager.serviceClasses?.each { service ->
            def serviceClass = service.getClazz()
            def serviceClassListenerConfigs = getListenerConfigs(serviceClass, app)
            if (serviceClassListenerConfigs) {
                serviceClassListenerConfigs.each {
                    registerListenerConfig(it, delegate)
                }
                listenerConfigs[serviceClass.name] = serviceClassListenerConfigs
            }
        }
        defaultJmsTemplate(org.springframework.jms.core.JmsTemplate, ref(DEFAULT_CONNECTION_FACTORY_BEAN_NAME))
    }

    def doWhenSpringReady = { app ->
        listenerConfigs.each { serviceClassName, serviceClassListenerConfigs ->
            serviceClassListenerConfigs.each {
                startListenerContainer(it, ApplicationContextHolder.applicationContext)
            }
        }
    }
    
    private startListenerContainer(listenerConfig, applicationContext) {
        applicationContext.getBean(listenerConfig.listenerContainerBeanName).start()
    }

    def events = [
        NewInstance: { klass, type, instance ->
            def types = app.config.griffon?.jms?.injectInto ?: ['controller']
            if(!types.contains(type)) return
            def jmsService = ApplicationContextHolder.applicationContext.getBean("jmsService")
            if(instance == jmsService) return
            def mc = app.artifactManager.findGriffonClass(klass).metaClass
            addSendMethodsToClass(jmsService, mc)
        }
    ]

    private addSendMethodsToClass(jmsService, mc) {
        [
            sendJMSMessage: "sendJMSMessage", 
            sendQueueJMSMessage: "sendQueueJMSMessage", 
            sendTopicJMSMessage: "sendTopicJMSMessage",
            sendPubSubJMSMessage: "sendTopicJMSMessage"
        ].each { m, i ->
            2.upto(4) { n ->
                mc."$m" << this."$i$n".curry(jmsService)
            } 
        }
    }

    private sendJMSMessage2 = { jmsService, destination, message ->
        jmsService.send(destination, message)
    }
    private sendJMSMessage3 = { jmsService, destination, message, postProcessor ->
        jmsService.send(destination, message, postProcessor)
    }
    private sendJMSMessage4 = { jmsService, destination, message, jmsTemplateBeanName, postProcessor ->
        jmsService.send(destination, message, jmsTemplateBeanName, postProcessor)
    }
    private sendQueueJMSMessage2 = { jmsService, destination, message ->
        jmsService.send(queue: destination, message)
    }
    private sendQueueJMSMessage3 = { jmsService, destination, message, postProcessor ->
        jmsService.send(queue: destination, message, postProcessor)
    }
    private sendQueueJMSMessage4 = { jmsService, destination, message, jmsTemplateBeanName, postProcessor ->
        jmsService.send(queue: destination, message, jmsTemplateBeanName, postProcessor)
    }
    private sendTopicJMSMessage2 = { jmsService, destination, message ->
        jmsService.send(topic: destination, message)
    }
    private sendTopicJMSMessage3 = { jmsService, destination, message, postProcessor ->
        jmsService.send(topic: destination, message, postProcessor)
    }
    private sendTopicJMSMessage4 = { jmsService, destination, message, jmsTemplateBeanName, postProcessor ->
        jmsService.send(topic: destination, message, jmsTemplateBeanName, postProcessor)
    }
}
