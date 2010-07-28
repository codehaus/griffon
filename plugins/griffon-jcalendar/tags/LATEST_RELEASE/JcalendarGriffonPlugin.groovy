/*
 * griffon-jcalendar: JCalendar Griffon plugin
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

/**
 * @author Andres Almiray
 */
class JcalendarGriffonPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Griffon the plugin is designed for
    def griffonVersion = '0.9 > *' 
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are included in plugin packaging
    def pluginIncludes = []
    // the plugin license
    def license = 'GNU LGPL 2.1'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    def toolkits = ['swing']
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    def platforms = []

    def author = 'Andres Almiray'
    def authorEmail = 'aalmiray@users.sourceforge.net'
    def title = 'Date & Calendar support'
    def description = '''
Date & Calendar support
http://www.toedter.com/en/jcalendar/
'''

    def documentation = 'http://griffon.codehaus.org/Jcalendar+Plugin'
}
