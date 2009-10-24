/*
 * Copyright 2009 the original author or authors.
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

import griffon.util.IGriffonApplication
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.Chat
import org.jivesoftware.smack.ChatManagerListener
import org.jivesoftware.smack.MessageListener
import org.jivesoftware.smack.PacketListener
import org.jivesoftware.smack.XMPPConnection
import org.jivesoftware.smack.proxy.ProxyInfo
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.packet.Packet
import org.jivesoftware.smack.filter.PacketTypeFilter

/**
 * @author Andres.Almiray
 */
class JabberGriffonAddon {
   private IGriffonApplication application
   private static final Map CHATS = [:]
   private static final MessageListener MESSAGE_LISTENER

   JabberGriffonAddon() {
      MESSAGE_LISTENER = { Chat chat, Message message ->
         application.event("JabberMessage", [message])
      } as MessageListener
   }

   def addonInit = { app ->
      application = app
      app.addApplicationEventListener(this)
   }

   def onNewInstance = { klass, type, instance ->
      def types = application.config.griffon?.jabber?.injectInto ?: ["controller"]
      if(!types.contains(type)) return
      instance.metaClass.jabberConnect = jabberConnect
      instance.metaClass.jabberChat = jabberChat
   }

   // ======================================================

   private jabberConnect = { Map options ->
      def serviceName = options.remove("serviceName")
      def host = options.remove("host")
      def port = options.remove("port")
      def username = options.remove("username")
      def password = options.remove("password")
      if(!options.containsKey("SASLAuthenticationEnabled")) options["SASLAuthenticationEnabled"] = false
      def proxy = null
      if(options.containsKey("proxy")) {
          Map proxyOptions = options.remove("proxy")
          proxy = new ProxyInfo(ProxyInfo.ProxyType.valueOf((proxyOptions.remove("type") ?: "HTTP").toUpperCase()),
                                proxyOptions.remove("host").toString(),
                                proxyOptions.remove("port") as int,
                                proxyOptions.remove("username") ?: "",
                                proxyOptions.remove("password") ?: "")
      }

      def config = !proxy ? new ConnectionConfiguration(host, port as int, serviceName) : new ConnectionConfiguration(host, port as int, serviceName, proxy)
      options.each { k, v ->
          config[(k)] = v
      }

      XMPPConnection conn = new XMPPConnection(config)
      conn.connect()
      conn.login(username, password, username + Long.toHexString(System.currentTimeMillis()))
      conn.metaClass.chatWith = jabberChat.curry(username, password)
      conn.addPacketListener({ Packet packet ->
         println "Received message from ${packet.from}, subject: ${packet.subject}, body: ${packet.body}"
      } as PacketListener, new PacketTypeFilter(Message))
      conn
   }
   
   private jabberChat = { String username, String password, String participant, String message ->
      def conn = delegate
      if(!conn.connected) {
         conn.connect()
         conn.login(username, password, username + Long.toHexString(System.currentTimeMillis()))
         CHATS[participant]?.removeMessageListener(MESSAGE_LISTENER)
         CHATS[participant] = null
      }

      Chat chat = CHATS[participant]
      if(!chat) {
         chat = conn.chatManager.createChat(participant, MESSAGE_LISTENER)
         CHATS[participant] = chat
      }

      chat.sendMessage(message)
   }
}
