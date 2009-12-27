import org.codehaus.griffon.util.BuildSettings

includeTargets << griffonScript("_GriffonSettings")

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
println platformOs
try {
    ant.fileset(dir: "${getPluginDirForName('lwjgl').file}/lib/webstart", includes: "*${platformOs}.jar").each {
        griffonCopyDist(it.toString(), new File(jardir.toString(), 'webstart').absolutePath)
    }
    if(!config?.griffon?.extensions?.resources) config.griffon.extensions.resources = new ConfigObject()
    def rs = config.griffon.extensions.resources[platformOs]
    if(!rs) {
        def co = new ConfigObject()
        co.nativelibs = config.lwjgl.jnlp.resources.find{it.os == platformOs}.nativelibs
        config.griffon.extensions.resources[platformOs] = co
    } else {
        if(!rs.nativeLibs) rs.nativeLibs = [] 
        rs.nativeLibs.addAll(nativeLibs)
    }
println config.griffon.extensions.resources
/*
    def rs = config.griffon.extensions.resources.find{ it.os == PLATFORMS[platformOs].webstartName}
    if(!rs) {
        def nativelibs = config.lwjgl.jnlp.resources.find{it.os == platformOs}.nativelibs
        config.griffon.extensions.resources << [
           os: PLATFORMS[platformOs].webstartName,
           nativelibs: nativelibs
        ]
    } else {
       if(!rs.nativeLibs) rs.nativeLibs = [] 
       rs.nativeLibs.addall(nativeLibs)
    }
*/
}catch(x){x.printStackTrace()}
}
