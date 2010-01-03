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

import griffon.test.support.GriffonFestTestHelper
import org.codehaus.griffon.cli.support.GriffonRootLoader

includeTargets << griffonScript("Init")
includeTargets << griffonScript("Package")

eventPackagePluginStart = { pluginName, plugin ->
    def destFileName = "lib/griffon-fest-support.jar"
    ant.delete(dir: destFileName, quiet: false, failOnError: false)
    ant.jar(destfile: destFileName) {
        fileset(dir: classesDirPath) {
            exclude(name: '_*.class')
            exclude(name: '*GriffonPlugin.class')
        }
    }
}

eventTestPhasesStart = { phasesToRun ->
    parseArguments()
    if(argsMap.fest) {
        if(!argsMap.unit) phasesToRun.remove("unit")
        if(!argsMap.integration) phasesToRun.remove("integration")
        if(!argsMap.other) phasesToRun.remove("other")
        if(!phasesToRun.contains("fest")) phasesToRun << "fest"
    }
}

binding.festTests = ["fest"]

binding.festTestsPreparation = {
    createConfig()
    classLoader.parent.addURL(classesDir.toURI().toURL())
    classLoader.parent.addURL("file:${basedir}/griffon-app/resources/".toURL())
    classLoader.parent.addURL("file:${basedir}/griffon-app/i18n/".toURL())
    return new GriffonFestTestHelper(griffonSettings, classLoader, resolveResources, config)
}

binding.festTestsCleanUp = { }
