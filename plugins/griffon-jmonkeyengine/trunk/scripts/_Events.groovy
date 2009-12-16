packagingType = ''
eventPackageStart = { type ->
    packagingType = type
}

def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if (!isPluginProject) {
        ant.fileset(dir: "${getPluginDirForName('jmonkeyengine').file}/lib", includes: "*.jar").each {
            griffonCopyDist(it.toString(), jardir)
        }
        if(buildconfig?.jmonkeyengine?.collada?.include) {
            ant.fileset(dir: "${getPluginDirForName('jmonkeyengine').file}/lib/collada", includes: "*.jar").each {
                griffonCopyDist(it.toString(), jardir)
            }
        }
        if(buildconfig?.jmonkeyengine?.swt?.include) {
            ant.fileset(dir: "${getPluginDirForName('jmonkeyengine').file}/lib/swt", includes: "*.jar").each {
                griffonCopyDist(it.toString(), jardir)
            }
        }
    }
}
