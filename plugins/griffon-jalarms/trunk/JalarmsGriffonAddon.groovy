/*
 * griffon-jalarms: JAlarms Griffon plugin
 * Copyright 2010 and beyond, Andres Almiray
 *
 * SmartGWT is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License version 3
 * as published by the Free Software Foundation.  SmartGWT is also
 * available under typical commercial license terms - see
 * smartclient.com/license.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */

import com.solab.alarms.AlarmChannel
import com.solab.alarms.AlarmSender

/**
 * @author Andres Almiray
 */
class JalarmsGriffonAddon {
    def doWithSpring = {
        alarmer(com.solab.alarms.AlarmSender) { bean ->
            bean.scope = 'singleton'
            bean.autowire = 'byName'
        }
    }

    def whenSpringReady = { app ->
        def ctx = app.applicationContext
        Map<String, AlarmChannel> channels = ctx.getBeansOfType(AlarmChannel)

        if(channels) {
            List<AlarmChannel> chnls = []
            chnls.addAll(channels.values())
            ctx.alarmer.alarmChannels = chnls
        }
    }
}
