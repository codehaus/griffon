
eventCopyLibsEnd = { jardir ->
    ant.fileset(dir:"${getPluginDirForName('gpars').file}/lib/", includes:"*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}
