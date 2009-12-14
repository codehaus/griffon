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
        }
    }
}
