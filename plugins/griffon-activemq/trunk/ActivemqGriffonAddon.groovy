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

import org.codehaus.griffon.plugins.activemq.ActiveMQUtils
import org.apache.activemq.xbean.XBeanBrokerService
import org.apache.activemq.broker.TransportConnector
import org.apache.activemq.ActiveMQConnectionFactory
import org.springframework.jms.core.JmsTemplate
import org.springframework.jms.connection.SingleConnectionFactory

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author Andres Almiray
 */
class ActivemqGriffonAddon {
    private static final Logger LOG = LoggerFactory.getLogger('griffon.addon.activemq.ActivemqGriffonAddon')

    def doWithSpring = {
        def conf = ActiveMQUtils.config
        if (!conf || !conf.active) {
            LOG.info 'ActiveMQ Embedded is disabled, not loading'
            return
        }

        LOG.info 'Configuring ActiveMQ Embedded...'

        jmsBroker(XBeanBrokerService) {
            useJmx = conf.useJmx
            persistent = conf.persistent
            transportConnectors = [new TransportConnector(uri: new URI("tcp://localhost:${conf.port}"))]
        }

        jmsConnectionFactory(SingleConnectionFactory) {
            targetConnectionFactory = { ActiveMQConnectionFactory cf ->
                brokerURL = 'vm://localhost'
            }
        }
    }
}
