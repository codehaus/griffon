/*
 * Copyright 2009-2010 the original author or authors.
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

import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.ChatManagerListener
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.PacketListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.proxy.ProxyInfo
import org.jivesoftware.smack.packet.Message

/**
 * @author Andres.Almiray
 */
class JabberGriffonAddon {
   private static final Map CHATS = [:]

   def events = [
      NewInstance: { klass, type, instance ->
         def types = app.config.griffon?.jabber?.injectInto ?: ['controller']
         if(!types.contains(type)) return
         instance.metaClass.jabberConnect = jabberConnect.curry(instance)
      }
   ]

   // ======================================================

   private jabberConnect = { Object instance, Map options ->
      def serviceName = options.remove('serviceName')
      def host = options.remove('host')
      def port = options.remove('port')
      def username = options.remove('username')
      def password = options.remove('password')
      if(!options.containsKey('SASLAuthenticationEnabled')) options['SASLAuthenticationEnabled'] = false
      def proxy = null
      if(options.containsKey('proxy')) {
          Map proxyOptions = options.remove('proxy')
          proxy = new ProxyInfo(ProxyInfo.ProxyType.valueOf((proxyOptions.remove('type') ?: 'HTTP').toUpperCase()),
                                proxyOptions.remove('host').toString(),
                                (proxyOptions.remove('port') ?: 80) as int,
                                proxyOptions.remove('username') ?: '',
                                proxyOptions.remove('password') ?: '')
      }

      def config = !proxy ? new ConnectionConfiguration(host, port as int, serviceName) : new ConnectionConfiguration(host, port as int, serviceName, proxy)
      options.each { k, v ->
          config[(k)] = v
      }

      XMPPConnection conn = new XMPPConnection(config)
      conn.connect()
      conn.login(username, password, username + Long.toHexString(System.currentTimeMillis()))
      conn.metaClass.chatWith = jabberChat.curry(instance, username, password)
      conn
   }
   
   private jabberChat = { Object instance, String username, String password, String participant, String message ->
      def conn = delegate
      if(!conn.connected) {
         conn.connect()
         conn.login(username, password, username + Long.toHexString(System.currentTimeMillis()))
         CHATS[participant]?.chat?.removeMessageListener(CHATS[participant]?.listener)
         CHATS[participant] = null
      }

      Chat chat = CHATS[participant]?.chat
      if(!chat) {
         def listener = { Chat c, Message m ->
             notifyListener(instance, 'onJabberMessage', [m]) 
         } as MessageListener
         chat = conn.chatManager.createChat(participant, listener)
         CHATS[participant] = [chat: chat, listener: listener]
      }

      chat.sendMessage(message)
   }

   private void notifyListener(Object instance, String eventHandler, List params) {
      def mp = instance.metaClass.getMetaProperty(eventHandler)
      if(mp && mp.getProperty(instance)) {
         mp.getProperty(instance)(*params)
         return
      }

      Class[] argTypes = MetaClassHelper.convertToTypeArray(params as Object[])
      def mm = instance.metaClass.pickMethod(eventHandler,argTypes)
      if(mm) {
         mm.invoke(instance,*params)
      }
   }
}
