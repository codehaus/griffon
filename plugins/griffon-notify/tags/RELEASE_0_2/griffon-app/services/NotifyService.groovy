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

import info.growl.Growl
import info.growl.GrowlUtils
import info.growl.GrowlException

/**
 * Notify Service - This class allows easy integration services like Snarl, Notify-Send, and Snarl. 
 * @author Hackergarten
 */
class NotifyService {

    /**
    * Notify the user with a title and message. 
    * On linux, notify send is called. 
    * On mac, growl is called. 
    * On Windows, snarl over the network is called. 
    * If any error occurs then a plain old dialog box is displayed. 
    */ 
    def notify(title, message, image = null) {
    
        def wasSuccessful = false 
        if (System.getProperty('os.name').startsWith("Linux")) {
            wasSuccessful = tryToExecute {
                [
                    'notify-send',
                    title,
                    message
                ].execute()
            } 
        } 
        
        if (!wasSuccessful && System.getProperty('os.name').startsWith("Mac")) {
           wasSuccessful = tryToExecute {
                Growl growl = GrowlUtils.getGrowlInstance(title);
                growl.addNotification("Griffon Notification", true);
			    growl.register();
			    growl.sendNotification("Griffon Notification", title, message);

           }
        } 
        
        if (!wasSuccessful && System.getProperty('os.name').startsWith("Windows")) {
            wasSuccessful = tryToExecute {
                new Snarl().send(title, message)
            }
        } 
        
        if (!wasSuccessful) {
            // show a JFrame
            def frame = new javax.swing.JDialog()
            frame.modal = false
            frame.title = title
            frame.defaultCloseOperation = javax.swing.WindowConstants.DISPOSE_ON_CLOSE
            def label = new javax.swing.JLabel(message)
            label.horizontalTextPosition = javax.swing.JLabel.CENTER
            label.preferredSize = new java.awt.Dimension(250, 50)
            frame.contentPane.add(label)
            
            // pack it
            frame.pack()
            
            // position in upper right
            frame.setVisible(true)
            
            
            // set time to disappear in 5 seconds
            new Timer().schedule(new java.util.TimerTask() {
                void run() {
                    frame.setVisible(false) 
                }
            }, 5000)
            
       }
    }
    
    boolean tryToExecute(Closure delegate) {
        try { 
            delegate() 
            return true
        } catch (Throwable t) {
            return false
        }
    }
}

class Snarl {
  private static final float SNP_VERSION = 1.1f
  private static final String HEAD = "type=SNP#?version=" + SNP_VERSION

  public void send(String title, String message) {
    send("localhost", title, message)
  }

  public void send(String host, String title, String message) {
    with(new Socket(InetAddress.getByName(host), 9887)) { sock ->
      with(new PrintWriter(sock.getOutputStream(), true)) { out ->
        out.println(formatMessage(title, message))
      }
    }
  }
  
  private String formatMessage(String title, String message) {
    def properties = [
            formatProperty("action", "notification"),
            formatProperty("app", "Griffon Snarl Notifier"),
            formatProperty("class", "alert"),
            formatProperty("title", title),
            formatProperty("text", message),
            formatProperty("icon", null),
            formatProperty("timeout", "10")]

    HEAD + properties.join('') + "\r\n"
  }

  private String formatProperty(String name, String value) {
    if (!value) {
      return ""
    }
    else {
      return "#?" + name + "=" + value
    }
  }

  private with(closable, closure) {
    try {
      closure(closable)
    } finally {
      try {
        closable.close()
      } catch (Exception e) {

      }
    }
  }
}
