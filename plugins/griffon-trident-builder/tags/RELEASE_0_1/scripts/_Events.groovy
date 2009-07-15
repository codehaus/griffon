eventCopyLibsEnd = { jardir ->
    ant.fileset(dir:"${getPluginDirForName('trident-builder').file}/lib/", includes:"*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}