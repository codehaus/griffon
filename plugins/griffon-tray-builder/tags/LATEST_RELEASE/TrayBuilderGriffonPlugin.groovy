/* --------------------------------------------------------------------
   TrayBuilder
   Copyright (C) 2008-2010 Andres Almiray

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
class TrayBuilderGriffonPlugin {
    def version = '0.5'
    def dependsOn = [:]
    def jdk = '1.6'
    def toolkits = ['swing']
    def griffonVersion = '0.9.2 > *'
    def license = 'GNU General Public License 2.0 (with classpath extension)'

    def author = 'Andres Almiray'
    def authorEmail = 'aalmiray@users.sourceforge.net'
    def title = 'TrayBuilder Plugin'
    def description = '''
Enables Swing components on SystemTray.
'''

    // URL to the plugin's documentation
    def documentation = 'http://griffon.codehaus.org/TrayBuilder+Plugin'
}
