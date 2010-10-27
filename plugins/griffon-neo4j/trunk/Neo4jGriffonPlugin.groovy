/*
    griffon-neo4j plugin
    Copyright (C) 2010 Andres Almiray

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/**
 * @author Andres Almiray
 */
class Neo4jGriffonPlugin {
    def version = 0.3
    def dependsOn = [:]
    def griffonVersion = '0.9.1 > *'
    def license = 'GNU Affero General Public License v3'

    def author = 'Andres Almiray'
    def authorEmail = 'aalmiray@users.sourceforge.net'
    def title = 'Neo4j support'
    def description = '''
Neo4j support
'''

    // URL to the plugin's documentation
    def documentation = 'http://griffon.codehaus.org/Neo4j+Plugin'
}
