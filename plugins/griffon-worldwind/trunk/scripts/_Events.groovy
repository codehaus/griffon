packagingType = ''
eventPackageStart = { type ->
    packagingType = type
}

def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if (!isPluginProject) {
        ant.fileset(dir: "${getPluginDirForName('worldwind').file}/lib", includes: "*.jar").each {
            if(it.name =~ /griffon.worldwind.addon.*/) {
                griffonCopyDist(it.toString(), jardir)
            }
        }

        if(!(packagingType in ['applet', 'webstart'])) {
            ant.fileset(dir: "${getPluginDirForName('worldwind').file}/lib", includes: "*.jar").each {
                griffonCopyDist(it.toString(), jardir)
            }
            def worldwindLibDir = "${getPluginDirForName('worldwind').file}/lib".toString()
            copyPlatformJars(worldwindLibDir, jardir)
            copyNativeLibs(worldwindLibDir, jardir)
        }
    }
}
