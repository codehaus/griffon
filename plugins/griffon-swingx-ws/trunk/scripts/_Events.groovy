def eventClosure1 = binding.variables.containsKey('eventCopyLibsEnd') ? eventCopyLibsEnd : {jardir->}
eventCopyLibsEnd = { jardir ->
    eventClosure1(jardir)
    if (!isPluginProject) {
        ant.fileset(dir:"${getPluginDirForName('swingx-ws').file}/lib", includes:"*.jar").each {
            griffonCopyDist(it.toString(), jardir)
        }
        if(!(buildConfig?.swingxws?.exclude?.swingx)) {
            ant.fileset(dir:"${getPluginDirForName('swingx-ws').file}/lib/swingx", includes:"*.jar").each {
                griffonCopyDist(it.toString(), jardir)
            }
        }
    }
}
