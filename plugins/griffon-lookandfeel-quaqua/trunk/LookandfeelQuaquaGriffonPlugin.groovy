/*
 * griffon-lookandfeel-quaqua: Quaqua Look&Feel for Griffon
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
class LookandfeelQuaquaGriffonPlugin {
    def version = 0.2
    def griffonVersion = '0.9.3 > *'
    def dependsOn = [lookandfeel: 0.r51]
    def toolkits = ['swing']
    def platforms = ['macosx', 'macosx64']
    def license = 'GNU LGPL 2.1'

    def author = 'Andres Almiray'
    def authorEmail = 'aalmiray@users.sourceforge.net'
    def title = 'Quaqua Swing Look & Feel'
    def description = '''
Quaqua Swing Look & Feel.
'''

    // URL to the plugin's documentation
    def documentation = 'http://griffon.codehaus.org/Lookandfeel+Quaqua+Plugin'
}

