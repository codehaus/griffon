/*
 * griffon-lookandfeel-quaqua: Quaqua Look&Feel for Griffon
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

packagingType = ''
eventPackageStart = { type ->
    packagingType = type
}

quaquaJnlpResources = []
for(os in ['macosx', 'macosx64']) {
    quaquaJnlpResources << [os: os, nativelibs: ["webstart/lookandfeel-quaqua-native-${os}.jar"]]
}

def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if (!isPluginProject) {
        def quaquaLibDir = "${getPluginDirForName('lookandfeel-quaqua').file}/lib".toString()
        if(!(packagingType in ['applet', 'webstart'])) {
            ant.fileset(dir: quaquaLibDir, includes: "*.jar").each {
                griffonCopyDist(it.toString(), jardir)
            }
            copyPlatformJars(quaquaLibDir, jardir)
            copyNativeLibs(quaquaLibDir, jardir)
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
    ant.fileset(dir: "${getPluginDirForName('lookandfeel-quaqua').file}/lib/webstart", includes: "*${platformOs}.jar").each {
        griffonCopyDist(it.toString(), new File(jardir.toString(), 'webstart').absolutePath)
    }
    if(!buildConfig?.griffon?.extensions?.resources) buildConfig.griffon.extensions.resources = new ConfigObject()
    def rs = buildConfig.griffon.extensions.resources[platformOs]
    if(!rs) {
        def co = new ConfigObject()
        co.nativelibs = quaquaJnlpResources.find{it.os == platformOs}.nativelibs
        buildConfig.griffon.extensions.resources[platformOs] = co
    } else {
        if(!rs.nativeLibs) rs.nativeLibs = []
        rs.nativeLibs.addAll(nativeLibs)
    }
}
