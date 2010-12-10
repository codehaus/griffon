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
package griffon.plugins.jml

import net.sf.jml.MsnMessenger
import net.sf.jml.MsnUserStatus
import net.sf.jml.impl.MsnMessengerFactory

import griffon.core.GriffonApplication
import griffon.util.Environment

/**
 * @author Andres Almiray
 */
@Singleton
final class MessengerConnector {
    private final Object lock = new Object()
    private boolean connected = false
    private GriffonApplication app

    ConfigObject createConfig(GriffonApplication app) {
        def msnClass = app.class.classLoader.loadClass('MessengerConfig')
        return new ConfigSlurper(Environment.current.name).parse(msnClass).msn
    }

    void connect(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            if(connected) return
            connected = true
        }

        this.app = app
        app.event('MessengerConnectStart', [config])
        createMessenger(config)
        app.event('MessengerConnectEnd', [MessengerHolder.instance.messenger])
    }

    void disconnect(GriffonApplication app, ConfigObject config) {
        synchronized(lock) {
            if(!connected) return
            connected = false
        }

        app.event('MessengerDisconnectStart', [config, MessengerHolder.instance.messenger])
        if(MessengerHolder.instance.messenger) {
            MessengerHolder.instance.messenger.logout()
            MessengerHolder.instance.messenger = null
        }
        app.event('MessengerDisconnectEnd')
    }

    private void createMessenger(ConfigObject config) {
        if(MessengerHolder.instance.messenger) return

        String username = config.messenger.username
        String password = config.messenger.password
        MsnMessenger messenger = MsnMessengerFactory.createMsnMessenger(username, password)
        // explicit check for boolean value
        messenger.logIncoming = config.messenger.logIncoming == true ? true : false
        messenger.logOutgoing = config.messenger.logOutgoing == true ? true : false
        def status = config.messenger.initStatus ?: MsnUserStatus.ONLINE
        if(status instanceof String) status = MsnUserStatus.parseStr(status)
        if(!(status instanceof MsnUserStatus)) status = MsnUserStatus.ONLINE
        messenger.owner.initStatus = status

        config.adapters.each { adapter -> messenger.addListener(adapter) }

        ['contactList', 'email', 'fileTransfer', 'message', 'messenger', 'switchboard'].each { type ->
            config.listeners[type].each { listener ->
                messenger."add${type[0].toUpperCase() + type[1..-1]}Listener"(listener)
            }
        }

        messenger.login()
        MessengerHolder.instance.messenger = messenger
    }

    def withMessenger = MessengerHolder.instance.withMessenger
}
