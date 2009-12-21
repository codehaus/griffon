packagingType = ''
eventPackageStart = { type ->
    packagingType = type
}

def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if (!isPluginProject) {
        def pivotLibDir = "${getPluginDirForName('pivot').file}/lib".toString()
        ant.fileset(dir: pivotLibDir, includes: "*.jar").each {
            griffonCopyDist(it.toString(), jardir)
        }
    }
}
