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

def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)
    if(compilingPlugin('tray-builder')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-tray-builder-plugin', dirs: "${trayBuilderPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('tray-builder', [
        conf: 'compile',
        name: 'griffon-tray-builder-addon',
        group: 'org.codehaus.griffon.plugins',
        version: trayBuilderPluginVersion
    ])
}

