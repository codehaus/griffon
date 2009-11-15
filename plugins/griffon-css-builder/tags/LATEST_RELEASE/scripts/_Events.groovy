eventCopyLibsEnd = { jardir ->
    ant.fileset(dir:"${getPluginDirForName('css-builder').file}/lib/", includes:"*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}
