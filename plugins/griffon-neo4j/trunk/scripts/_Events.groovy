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

def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if (!isPluginProject) {
        def pluginDir = getPluginDirForName('neo4j')
        if(pluginDir?.file?.exists()) {
            ant.fileset(dir: "${pluginDir.file}/lib/", includes: '*.jar').each {
                griffonCopyDist(it.toString(), jardir)
            }
        }
    }
}

