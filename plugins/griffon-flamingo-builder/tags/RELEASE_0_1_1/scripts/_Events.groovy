
eventCopyLibsEnd = { jardir ->
    ant.fileset(dir:"${getPluginDirForName('flamingo-builder').file}/lib/", includes:"*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}