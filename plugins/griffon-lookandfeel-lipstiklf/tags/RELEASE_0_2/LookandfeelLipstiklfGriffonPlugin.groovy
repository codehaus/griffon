/* --------------------------------------------------------------------
   Lookandfeel Lipstiklf
   Copyright (C) 2011 Andres Almiray

   This library is free software; you can redistribute it and/or
   modify it under the terms of the GNU General Public License as
   published by the Free Software Foundation; either version 2 of the
   License, or (at your option) any later version.

   This library is distributed in the hope that it will be useful,
   but WITHOUT ANY WARRANTY; without even the implied warranty of
   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
   General Public License for more details.

   You should have received a copy of the GNU General Public License
   along with this library; if not, see <http://www.gnu.org/licenses/>.
   ---------------------------------------------------------------------
*/

/**
 * @author Andres Almiray
 */
class LookandfeelLipstiklfGriffonPlugin {
    // the plugin version
    def version = "0.2"
    // the version or versions of Griffon the plugin is designed for
    def griffonVersion = '0.9.3 > *' 
    // the other plugins this plugin depends on
    def dependsOn = [lookandfeel: '0.5']
    // resources that are included in plugin packaging
    def pluginIncludes = []
    // the plugin license
    def license = 'GNU Public License 2.1'
    // Toolkit compatibility. No value means compatible with all
    // Valid values are: swing, javafx, swt, pivot, gtk
    def toolkits = ['swing']
    // Platform compatibility. No value means compatible with all
    // Valid values are:
    // linux, linux64, windows, windows64, macosx, macosx64, solaris
    def platforms = []

    def author = 'Andres Almiray'
    def authorEmail = 'aalmiray@users.sourceforge.net'
    def title = 'Lipstiklf Look & Feel'
    def description = '''
Lipstiklf Look & Feel
http://sourceforge.net/projects/lipstiklf
'''

    // URL to the plugin's documentation
    def documentation = 'http://griffon.codehaus.org/LookandfeelLipstiklf+Plugin'
}
