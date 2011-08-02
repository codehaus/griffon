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
 
package griffon.jms

import javax.jms.Destination
import javax.jms.Topic
import griffon.util.Metadata
import griffon.spring.ApplicationContextHolder
import org.springframework.jms.core.MessagePostProcessor
import griffon.jms.listener.GriffonMessagePostProcessor

/**
 * @author Luke Daley
 * @author Andres Almiray
 */
class JmsService {
    static final DEFAULT_JMS_TEMPLATE_BEAN_NAME = "defaultJmsTemplate"
    
    def send(destination, message) {
        send(destination, message, null)
    }
    
    def send(destination, message, postProcessor) {
        send(destination, message, null, postProcessor)
    }
    
    def send(destination, message, jmsTemplateBeanName, postProcessor) {        
        jmsTemplateBeanName = jmsTemplateBeanName ?: DEFAULT_JMS_TEMPLATE_BEAN_NAME
        def jmsTemplate = ApplicationContextHolder.applicationContext.getBean(jmsTemplateBeanName)
        if (jmsTemplate == null) {
            throw new Error("Could not find bean with name '${jmsTemplateBeanName}' to use as a JmsTemplate")
        }
        
        def isTopic
        if (destination instanceof Destination) {
            isTopic = destination instanceof Topic
        } else {
            def destinationMap = convertToDestinationMap(destination)
            isTopic = destinationMap.containsKey("topic")
            jmsTemplate.pubSubDomain = isTopic
            destination = (isTopic) ? destinationMap.topic : destinationMap.queue
        }
        
        if (log.infoEnabled) {
            def topicOrQueue = (isTopic) ? "topic" : "queue"
            def logMsg = "Sending JMS message '$message' to $topicOrQueue '$destination'"
            if (jmsTemplateBeanName != DEFAULT_JMS_TEMPLATE_BEAN_NAME)
                logMsg += " using template '$jmsTemplateBeanName'"
            log.info(logMsg)
        }

        if (postProcessor) {
            jmsTemplate.convertAndSend(destination, message, new GriffonMessagePostProcessor(jmsService: this, jmsTemplate: jmsTemplate, processor: postProcessor))
        } else {
            jmsTemplate.convertAndSend(destination, message)
        }      
    }

    def convertToDestinationMap(destination) {      
        if (destination == null) {
            [queue: null]
        } else if (destination instanceof String) {
            [queue: destination]
        } else if (destination instanceof Map) {
            if (destination.queue) {
                [queue: destination.queue]
            } else if (destination.topic) {
                [topic: destination.topic]
            } else {
                def parts = []
                if (destination.app) {
                    parts << destination.app
                } else {
                    parts << Metadata.current.getApplicationName()
                }
                if (destination.service) {
                    parts << destination.service
                    if (destination.method) {
                        parts << destination.method
                    }
                }
                [queue: (parts) ? parts.join('.') : null]
            }
        } else {
            [queue: destination.toString()]
        }
    }
}