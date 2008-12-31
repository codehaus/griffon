eventCopyLibsEnd = { jardir ->
    ant.fileset(dir:"${getPluginDirForName('macwidgets-builder').file}/lib/", includes:"*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}