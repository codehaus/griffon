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
 
package griffon.jms.listener

import griffon.util.GriffonClassUtils
import griffon.util.GriffonNameUtils
import griffon.jms.*

/**
 * @author Luke Daley
 * @author Andres Almiray
 */
class ServiceInspector {
    final static DEFAULT_SERVICE_LISTENER = "jmsMessage"
    final static CUSTOM_SERVICE_LISTENER_SPECIFIER = "listenerMethod"
    final static EXPOSES_SPECIFIER = "exposes"
    final static EXPOSE_SPECIFIER = "expose"
    final static EXPOSES_JMS_SPECIFIER = "jms"
    
    def getListenerConfigs(service, listenerConfigFactory, griffonApplication) {
        if (!exposesJms(service)) return []
        
        def listenerConfigs = []
        
        listenerConfigs << getServiceListenerConfig(service, listenerConfigFactory, griffonApplication)
        service.methods.each {
            listenerConfigs << getServiceMethodListenerConfig(service, it, listenerConfigFactory, griffonApplication)
        }
        
        listenerConfigs.findAll { it != null }
    } 
    
    def getServiceListenerConfig(service, listenerConfigFactory, griffonApplication) {
        def hasServiceListenerMethod = hasServiceListenerMethod(service)
        if (hasServiceListenerMethod || hasServiceListenerClosure(service)) {
            def listenerConfig = listenerConfigFactory.getListenerConfig(service, griffonApplication)
            listenerConfig.with {
                serviceListener = true
                listenerMethodOrClosureName = getServiceListenerName(service)
                listenerIsClosure = !hasServiceListenerMethod
                
                concurrentConsumers = GriffonClassUtils.getStaticPropertyValue(service, "listenerCount") ?: 1
                explicitDestinationName = GriffonClassUtils.getStaticPropertyValue(service, "destination")
                topic = GriffonClassUtils.getStaticPropertyValue(service, "pubSub") ?: false
                messageSelector = GriffonClassUtils.getStaticPropertyValue(service, "messageSelector")
                durable = GriffonClassUtils.getStaticPropertyValue(service, "durable")
                explicitClientId = GriffonClassUtils.getStaticPropertyValue(service, "clientId")
                messageConverter = GriffonClassUtils.getStaticPropertyValue(service, "messageConverter") ?: ""
            }
            listenerConfig
        } else {
            null
        }
    }
    
    def getServiceListenerName(service) {
        GriffonClassUtils.getStaticPropertyValue(service, CUSTOM_SERVICE_LISTENER_SPECIFIER) ?: DEFAULT_SERVICE_LISTENER
    }
    
    def hasServiceListenerMethod(service) {
        def serviceListenerName = getServiceListenerName(service)
        service.metaClass.methods.find { it.name == serviceListenerName && it.parameterTypes.size() == 1 } != null
    }
    
    def hasServiceListenerClosure(service) {
        def serviceListenerName = getServiceListenerName(service)
        service.metaClass.methods.find { it.name == GriffonNameUtils.getGetterName(serviceListenerName) && it.parameterTypes.size() == 0 } != null
    }
    
    def exposesJms(service) {
        GriffonClassUtils.getStaticPropertyValue(service, EXPOSES_SPECIFIER)?.contains(EXPOSES_JMS_SPECIFIER) || GriffonClassUtils.getStaticPropertyValue(service, EXPOSE_SPECIFIER)?.contains(EXPOSES_JMS_SPECIFIER)
    }
    
    def isSingleton(service) {
        def scope = GriffonClassUtils.getStaticPropertyValue(service, 'scope')
        (scope == null || scope == "singleton")
    }
    
    def getServiceMethodListenerConfig(service, method, listenerConfigFactory, griffonApplication) {
        def subscriberAnnotation = method.getAnnotation(Subscriber)
        def queueAnnotation = method.getAnnotation(Queue)
        
        if (subscriberAnnotation) {
            getServiceMethodSubscriberListenerConfig(service, method, subscriberAnnotation, listenerConfigFactory, griffonApplication) 
        } else if (queueAnnotation) {
            getServiceMethodQueueListenerConfig(service, method, queueAnnotation, listenerConfigFactory, griffonApplication) 
        } else {
            null
        }
    }
    
    def getServiceMethodSubscriberListenerConfig(service, method, annotation, listenerConfigFactory, griffonApplication) {
        def listenerConfig = listenerConfigFactory.getListenerConfig(service, griffonApplication)
        listenerConfig.with {
            topic = true
            listenerMethodOrClosureName = method.name
            explicitDestinationName = annotation.topic()
            messageSelector = annotation.selector()
            durable = annotation.durable()
            messageConverter = annotation.messageConverter()
        }
        listenerConfig
    }
    
    def getServiceMethodQueueListenerConfig(service, method, annotation, listenerConfigFactory, griffonApplication) {
        def listenerConfig = listenerConfigFactory.getListenerConfig(service, griffonApplication)
        listenerConfig.with {
            topic = false
            listenerMethodOrClosureName = method.name
            explicitDestinationName = annotation.name()
            messageSelector = annotation.selector()
            messageConverter = annotation.messageConverter()
        }
        listenerConfig
    }  
}