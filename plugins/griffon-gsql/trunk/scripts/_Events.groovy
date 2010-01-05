/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Andres Almiray
 */

import org.codehaus.griffon.plugins.GriffonPluginUtils

eventPackagePluginStart = { pluginName, plugin ->
    def destFileName = "lib/griffon-${pluginName}-addon-${plugin.version}.jar"
    ant.delete(dir: destFileName, quiet: false, failOnError: false)
    ant.jar(destfile: destFileName) {
        fileset(dir: classesDirPath) {
            exclude(name: '_*.class')
            exclude(name: '*GriffonPlugin.class')
        }
    }
}

eventCopyLibsEnd = { jardir ->
    ant.fileset(dir: "${getPluginDirForName('gsql').file}/lib/", includes: "*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}

getPluginDirForName = { String pluginName ->
    // pluginsHome = griffonSettings.projectPluginsDir.path
    GriffonPluginUtils.getPluginDirForName(pluginsHome, pluginName)
}

def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if (!isPluginProject) {
        ant.fileset(dir:"${getPluginDirForName('gsql').file}/lib/", includes:"*.jar").each {
            griffonCopyDist(it.toString(), jardir)
        }
    }
}

