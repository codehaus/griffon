/* --------------------------------------------------------------------
   griffon-domain-db4o plugin
   Copyright (C) 2010 Andres Almiray

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
class DomainDb4oGriffonPlugin {
    def version = 0.1
    def dependsOn = [db4o: 0.2, domain: 0.1]
    def griffonVersion = '0.9 > *'
    def license = 'GNU General Public License 2.0'

    def author = 'Andres Almiray'
    def authorEmail = 'aalmiray@users.sourceforge.net'
    def title = 'Domain class support via Db4o'
    def description = '''
Domain class support via Db4o
'''

    // URL to the plugin's documentation
    def documentation = 'http://griffon.codehaus.org/DomainDb4o+Plugin'
}
