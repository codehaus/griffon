/* --------------------------------------------------------------------
   griffon-db4o plugin
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

includeTargets << griffonScript("_GriffonInit")
includeTargets << griffonScript("_GriffonCreateArtifacts")

// check to see if we already have a Db4oGriffonAddon
boolean addonIsSet1
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'Db4oGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding Db4oGriffonAddon to Builder.groovy'
    builderConfigFile.append('''
root.'Db4oGriffonAddon'.addon=true
''')
}

if(!(config.flatten().'griffon.db4o.injectInto')) {
    configFile.append('''
griffon.db4o.injectInto = ['controller']
''')
}

if(!new File("${basedir}/griffon-app/conf/Db4oConfig.groovy").exists()) {
   createArtifact(
      name: "Db4oConfig",
      suffix: "",
      type: "Db4oConfig",
      path: "griffon-app/conf")
}

if(!new File("${basedir}/griffon-app/conf/BootstrapDb4o.groovy").exists()) {
   createArtifact(
      name: "BootstrapDb4o",
      suffix: "",
      type: "BootstrapDb4o",
      path: "griffon-app/conf")
}
