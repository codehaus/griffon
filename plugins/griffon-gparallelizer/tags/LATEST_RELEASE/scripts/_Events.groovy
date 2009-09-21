
eventCopyLibsEnd = { jardir ->
    ant.fileset(dir:"${getPluginDirForName('gparallelizer').file}/lib/", includes:"*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}
