packagingType = ''
eventPackageStart = { type ->
    packagingType = type
}

def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if (!isPluginProject) {
        def swtLibDir = "${getPluginDirForName('swt').file}/lib".toString()
        ant.fileset(dir: swtLibDir, includes: "*.jar").each {
            if(it.name =~ /griffon.swt.addon.*/) {
                griffonCopyDist(it.toString(), jardir)
            }
        }

        if(!(packagingType in ['applet', 'webstart'])) {
            ant.fileset(dir: swtLibDir, includes: "*.jar").each {
                griffonCopyDist(it.toString(), jardir)
            }
            copyPlatformJars(swtLibDir, jardir)
            copyNativeLibs(swtLibDir, jardir)
        }
    }
}

