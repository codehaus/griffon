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

import griffon.core.GriffonApplication
import griffon.core.GriffonServiceClass
import griffon.util.GriffonNameUtils
import org.springframework.jms.listener.DefaultMessageListenerContainer

/**
 * @author Luke Daley
 * @author Andres Almiray
 */
class ListenerConfig {
    static final LISTENER_ADAPTER_BEAN_SUFFIX = "JMSListener"
    static final LISTENER_CONTAINER_BEAN_SUFFIX = "JMSListenerContainer"
    
    static final LISTENER_ADAPTER_CLASS = ServiceListenerAdapter
    static final LISTENER_CONTAINER_CLASS = DefaultMessageListenerContainer
    
    static final DEFAULT_CONNECTION_FACTORY_BEAN_NAME = "jmsConnectionFactory"
    
    GriffonApplication griffonApplication
    
    boolean topic = false
    def concurrentConsumers = 1
    def subscriptionDurable = false
    def listenerMethodOrClosureName = null
    def listenerIsClosure = false    
    def messageSelector = null
    def durable = false
    def explicitDurableSubscriptionName = null
    def explicitClientId = null
    def explicitDestinationName = null
    def explicitConnectionFactoryBeanName
    def serviceListener = false
    def serviceBeanName
    def messageConverter = ""
    
    def getServiceBeanPrefix() {
        serviceBeanName - GriffonServiceClass.TRAILING
    }
    
    def getBeanPrefix() {
        if (serviceListener) {
            this.serviceBeanPrefix
        } else {
            this.serviceBeanPrefix + GriffonNameUtils.capitalize(listenerMethodOrClosureName)
        }
    }
    
    def getListenerAdapterBeanName() {
        this.beanPrefix + LISTENER_ADAPTER_BEAN_SUFFIX
    }
    
    def getListenerContainerBeanName() {
        this.beanPrefix + LISTENER_CONTAINER_BEAN_SUFFIX
    }
    
    def getDestinationName() {
        if (explicitDestinationName) {
            explicitDestinationName
        } else {
            if (serviceListener) {
                this.appName + "." + this.serviceBeanPrefix
            } else if (topic) {
                listenerMethodOrClosureName
            } else {
                this.appName + "." + this.serviceBeanPrefix + "." + listenerMethodOrClosureName
            }
        }
    }
    
    def getDurableSubscriptionName() {
        explicitDurableSubscriptionName ?: this.serviceBeanPrefix + GriffonNameUtils.capitalize(listenerMethodOrClosureName)
    }

    def getClientId() {
        explicitClientId ?: this.appName
    }
    
    def getAppName() {
        griffonApplication.metadata['app.name']
    }
    
    def getConnectionFactoryBeanName() {
        explicitConnectionFactoryBeanName ?: DEFAULT_CONNECTION_FACTORY_BEAN_NAME
    }
    
    def register(beanBuilder) {
        registerListenerAdapter(beanBuilder)
        registerListenerContainer(beanBuilder)
    }

    def registerListenerAdapter(beanBuilder) {
        beanBuilder.with {
            "${this.listenerAdapterBeanName}"(LISTENER_ADAPTER_CLASS) {
                delegate.delegate = ref(serviceBeanName)
                defaultListenerMethod = listenerMethodOrClosureName
                listenerIsClosure = listenerIsClosure
                if (this.messageConverter == null) {
                    messageConverter = null
                } else if (this.messageConverter != "") {
                    messageConverter = ref(this.messageConverter)
                }
            }
        }
    }
    
    def registerListenerContainer(beanBuilder) {
        beanBuilder.with {
            "${this.listenerContainerBeanName}"(LISTENER_CONTAINER_CLASS) {
                it.destroyMethod = "destroy"
                autoStartup = false
                
                concurrentConsumers = concurrentConsumers
                destinationName = this.destinationName
                
                pubSubDomain = this.topic
            
                if (this.topic && durable) {
                    subscriptionDurable = durable
                    durableSubscriptionName = this.durableSubscriptionName
                    clientId = this.clientId
                }
            
                if (messageSelector) {
                    messageSelector = messageSelector
                }
                
                // transactionManager = ref("transactionManager")
                connectionFactory = ref(this.connectionFactoryBeanName)
                messageListener = ref(this.listenerAdapterBeanName)
            }
        }
    }
    
    def removeBeansFromContext(ctx) {
        [listenerAdapterBeanName,listenerContainerBeanName].each {
            ctx.removeBeanDefinition(it)
        }
    }
}