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

lwjglJnlpResources = []
def lwjgl_version = '2.6'
for(os in ['linux', 'macosx', 'windows', 'solaris']) {
    lwjglJnlpResources << [os: os, nativelibs: ["webstart/lwjgl-${lwjgl_version}-native-${os}.jar"]]
}

packagingType = ''
eventPackageStart = { type ->
    packagingType = type
}

def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if(compilingPlugin('lwjgl')) return
    if(!(packagingType in ['applet', 'webstart'])) {
        def lwjglLibDir = "${getPluginDirForName('lwjgl').file}/lib".toString()
        copyNativeLibs(lwjglLibDir, jardir)
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
    if(compilingPlugin('lwjgl')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-lwjgl-plugin', dirs: "${lwjglPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('lwjgl', [
        conf: 'compile',
        name: 'griffon-lwjgl-addon',
        group: 'org.codehaus.griffon.plugins',
        version: lwjglPluginVersion
    ])
}

doWithPlatform = { platformOs ->
    def origPlatformOs = platformOs
    if(platformOs.endsWith('64')) platformOs -= '64'

    ant.fileset(dir: "${getPluginDirForName('lwjgl').file}/lib/webstart", includes: "*${platformOs}.jar").each {
        griffonCopyDist(it.toString(), new File(jardir.toString(), 'webstart').absolutePath)
    }
    if(!buildConfig?.griffon?.extensions?.resources) buildConfig.griffon.extensions.resources = new ConfigObject()
    def rs = buildConfig.griffon.extensions.resources[origPlatformOs]
    if(!rs) {
        def co = new ConfigObject()
        co.nativelibs = lwjglJnlpResources.find{it.os == platformOs}.nativelibs
        buildConfig.griffon.extensions.resources[origPlatformOs] = co
    } else {
        if(!rs.nativeLibs) rs.nativeLibs = [] 
        rs.nativeLibs.addAll(nativeLibs)
    }
}
