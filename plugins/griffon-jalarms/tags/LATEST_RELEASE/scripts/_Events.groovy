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

/**
 * @author Andres Almiray
 */

def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)
    if(compilingPlugin('jalarms')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-jalarms-plugin', dirs: "${jalarmsPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('jalarms', [
        conf: 'compile',
        name: 'griffon-jalarms-addon',
        group: 'org.codehaus.griffon.plugins',
        version: jalarmsPluginVersion
    ])
}

