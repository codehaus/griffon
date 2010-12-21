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

import griffon.util.Environment

includeTargets << griffonScript("_GriffonSettings")

joglCompatJnlpResources = []
def joglCompat_version = '1.1.1'
for(os in ['linux', 'macosx', 'windows']) {
    joglCompatJnlpResources << [os: os, nativelibs: ["webstart/jogl-compat-natives-${joglCompat_version}-${os}.jar"]]
}

packagingType = ''
eventPackageStart = { type ->
    packagingType = type
}

def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if(compilingPlugin('jogl-compat')) return
    if(!(packagingType in ['applet', 'webstart'])) {
        def joglCompatLibDir = "${getPluginDirForName('jogl-compat').file}/lib".toString()
        copyNativeLibs(joglCompatLibDir, jardir)
    } else {
        if(Environment.current == Environment.DEVELOPMENT) {
            doWithPlatform(platform)
        } else {
            PLATFORMS.each { doWithPlatform(it.key) }
        }
    }
}

def eventClosure2 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure2(cl)
    if(compilingPlugin('jogl-compat')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-jogl-compat-plugin', dirs: "${joglCompatPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('jogl-compat', [
        conf: 'compile',
        name: 'griffon-jogl-compat-addon',
        group: 'org.codehaus.griffon.plugins',
        version: joglCompatPluginVersion
    ])
}

doWithPlatform = { platformOs ->
    def origPlatformOs = platformOs
    if(platformOs.endsWith('64')) platformOs -= '64'

    ant.fileset(dir: "${getPluginDirForName('jogl-compat').file}/lib/webstart", includes: "*${platformOs}.jar").each {
        griffonCopyDist(it.toString(), new File(jardir.toString(), 'webstart').absolutePath)
    }
    if(!buildConfig?.griffon?.extensions?.resources) buildConfig.griffon.extensions.resources = new ConfigObject()
    def rs = buildConfig.griffon.extensions.resources[origPlatformOs]
    if(!rs) {
        def co = new ConfigObject()
        co.nativelibs = joglCompatJnlpResources.find{it.os == platformOs}.nativelibs
        buildConfig.griffon.extensions.resources[origPlatformOs] = co
    } else {
        if(!rs.nativeLibs) rs.nativeLibs = [] 
        rs.nativeLibs.addAll(nativeLibs)
    }
}
