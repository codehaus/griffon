/*
 * Copyright 2010 the original author or authors.
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

packagingType = ''
eventPackageStart = { type ->
    packagingType = type
}

serialJnlpResources = []
for(os in ['linux', 'macosx', 'windows']) {
    serialJnlpResources << [os: os, nativelibs: ["webstart/serial-native-${os}.jar"]]
}

def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if (!isPluginProject) {
        def serialLibDir = "${getPluginDirForName('serial').file}/lib".toString()
        if(!(packagingType in ['applet', 'webstart'])) {
            ant.fileset(dir: serialLibDir, includes: "*.jar").each {
                griffonCopyDist(it.toString(), jardir)
            }
            copyPlatformJars(serialLibDir, jardir)
            copyNativeLibs(serialLibDir, jardir)
        } else {
            def env = System.getProperty(BuildSettings.ENVIRONMENT)
            if(env == BuildSettings.ENV_DEVELOPMENT) {
                doWithPlatform(platform)
            } else {
                PLATFORMS.each { doWithPlatform(it.key) }
            }
        }
    }
}

doWithPlatform = { platformOs ->
    ant.fileset(dir: "${getPluginDirForName('serial').file}/lib/webstart", includes: "*${platformOs}.jar").each {
        griffonCopyDist(it.toString(), new File(jardir.toString(), 'webstart').absolutePath)
    }
    if(!config?.griffon?.extensions?.resources) config.griffon.extensions.resources = new ConfigObject()
    def rs = config.griffon.extensions.resources[platformOs]
    if(!rs) {
        def co = new ConfigObject()
        co.nativelibs = serialJnlpResources.find{it.os == platformOs}.nativelibs
        config.griffon.extensions.resources[platformOs] = co
    } else {
        if(!rs.nativeLibs) rs.nativeLibs = [] 
        rs.nativeLibs.addAll(nativeLibs)
    }
}

