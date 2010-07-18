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
def lwjgl_version = "2.5"
for(os in ['linux', 'linux64', 'macosx', 'macosx64', 'windows', 'windows64', 'solaris', 'solaris64']) {
    lwjglJnlpResources << [os: os, nativelibs: ["webstart/lwjgl-${lwjgl_version}-native-${os}.jar"]]
}

packagingType = ''
eventPackageStart = { type ->
    packagingType = type
}

def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if (!isPluginProject) {
        ant.fileset(dir: "${getPluginDirForName('lwjgl').file}/lib", includes: "*.jar").each {
            if(it.name =~ /griffon.lwjgl.addon.*/) {
                griffonCopyDist(it.toString(), jardir)
            }
        }

        if(!(packagingType in ['applet', 'webstart'])) {
            ant.fileset(dir: "${getPluginDirForName('lwjgl').file}/lib", includes: "*.jar").each {
                griffonCopyDist(it.toString(), jardir)
            }
            def lwjglLibDir = "${getPluginDirForName('lwjgl').file}/lib".toString()
            copyPlatformJars(lwjglLibDir, jardir)
            copyNativeLibs(lwjglLibDir, jardir)
        } else {
            if(Environment.current == Environment.DEVELOPMENT) {
                doWithPlatform(platform)
            } else {
                PLATFORMS.each { doWithPlatform(it.key) }
            }
        }
    }
}

doWithPlatform = { platformOs ->
    ant.fileset(dir: "${getPluginDirForName('lwjgl').file}/lib/webstart", includes: "*${platformOs}.jar").each {
        griffonCopyDist(it.toString(), new File(jardir.toString(), 'webstart').absolutePath)
    }
    if(!buildConfig?.griffon?.extensions?.resources) buildConfig.griffon.extensions.resources = new ConfigObject()
    def rs = buildConfig.griffon.extensions.resources[platformOs]
    if(!rs) {
        def co = new ConfigObject()
        co.nativelibs = lwjglJnlpResources.find{it.os == platformOs}.nativelibs
        buildConfig.griffon.extensions.resources[platformOs] = co
    } else {
        if(!rs.nativeLibs) rs.nativeLibs = [] 
        rs.nativeLibs.addAll(nativeLibs)
    }
}
