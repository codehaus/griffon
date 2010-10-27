/*
 * Copyright 2009-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

loadSpecTestTypeClass = { ->
    def doLoad = { -> 
        def spockPluginDir = pluginSettings.getPluginDirForName('spock')
        if(!spockPluginDir) return
        ant.fileset(dir: "${spockPluginDir.file}/dist/", includes: "*-test.jar").each { f ->
            addUrlIfNotPresent classLoader, f.file
        }
        ant.fileset(dir: "${spockPluginDir.file}/lib/", includes: "*jar").each { f ->
            addUrlIfNotPresent classLoader, f.file
        }

        classLoader.loadClass('griffon.spock.test.GriffonSpecTestType')
    }

    try {
        doLoad()
    } catch (ClassNotFoundException e) {
        includeTargets << griffonScript("_GriffonCompile")
        compile()
        doLoad()
    }
}

eventAllTestsStart = {
    def specTestTypeClass = loadSpecTestTypeClass()
    unitTests << specTestTypeClass.newInstance('spock', 'unit')
    integrationTests << specTestTypeClass.newInstance('spock', 'integration')
}

