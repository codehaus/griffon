
eventCopyLibsEnd = { jardir ->
    ant.fileset(dir:"${getPluginDirForName('swingx-builder').file}/lib/", includes:"*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}
