/*
 * Copyright 2011 the original author or authors.
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

/**
 * @author Andres Almiray
 */

includeTargets << griffonScript('Init')

ant.property(environment: 'env')
syntheticaHome = ant.antProject.properties.'env.SYNTHETICA_HOME'
if(!syntheticaHome) {
    println '''Cannot locate Synthetica libraries.
Define an environment variable $SYNTHETICA_HOME that points to a directory
where Synthetica jar files can be located and try again.
'''
    System.exit(1)
}

syntheticaLafDir = new File(syntheticaHome, 'lookandfeel')
if(!syntheticaLafDir.exists()) {
    println '''$SYNTHETICA_HOME/lookandfeel does not exist.
Please create this directory and place all Synthetica L&F jar files there.
'''
    System.exit(1)
}

def nameVersionPattern = ~/^([\w][\w\.-]*)-([0-9][\w\.0-9\-]*)\.jar$/
def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)

    griffonSettings.dependencyManager.flatDirResolver name: 'synthetica-local', dirs: syntheticaLafDir

    syntheticaLafDir.eachFileMatch(~/.*\.jar/) { jarFile ->
        def matcher = nameVersionPattern.matcher(jarFile.name)
        matcher.find()
        griffonSettings.dependencyManager.addPluginDependency('lookandfeel-synthetica', [
            conf: 'build',
            name: matcher.group(1),
            group: 'de.javasoft.synthetica',
            version: matcher.group(2)
        ])
        griffonSettings.dependencyManager.addPluginDependency('lookandfeel-synthetica', [
            conf: 'compile',
            name: matcher.group(1),
            group: 'de.javasoft.synthetica',
            version: matcher.group(2)
        ])
        addUrlIfNotPresent classLoader, jarFile
    }

    if(compilingPlugin('lookandfeel-synthetica')) {
        def syntheticaLafLibs = ant.fileset(dir: syntheticaLafDir, includes: '*.jar')
        ant.project.getReference('griffon.compile.classpath').addFileset(syntheticaLafLibs)
        return
    }

    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-lookand-feelsynthetica-plugin', dirs: "${lookandfeelSyntheticaPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('lookandfeel-synthetica', [
        conf: 'compile',
        name: 'griffon-lookandfeel-synthetica-addon',
        group: 'org.codehaus.griffon.plugins',
        version: lookandfeelSyntheticaPluginVersion
    ])
}
