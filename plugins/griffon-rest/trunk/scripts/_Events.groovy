eventCopyLibsEnd = { jardir ->
    if (!isPluginProject) {
        ant.fileset(dir:"${getPluginDirForName('rest').file}/lib/", includes:"*.jar").each {
            griffonCopyDist(it.toString(), jardir)
        }
    }
}

