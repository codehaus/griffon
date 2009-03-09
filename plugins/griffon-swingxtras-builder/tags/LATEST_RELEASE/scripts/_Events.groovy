
eventCopyLibsEnd = { jardir ->
    ant.fileset(dir:"${getPluginDirForName('swingxtras-builder').file}/lib/", includes:"*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}
