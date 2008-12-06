
eventCopyLibsEnd = { jardir ->
    ant.fileset(dir:"${getPluginDirForName('jide-builder').file}/lib/", includes:"*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}
