
eventSetClasspath = { rootLoader ->
    if (getPluginDirForName('swingx-builder')?.file?.exists()) {
        ant.fileset(dir:"${getPluginDirForName('swingx-builder').file}/lib/", includes:"*.jar").each {
            rootLoader.addURL(it.toURL())
        }
    }
}

eventCopyLibsEnd = { jardir ->
    ant.fileset(dir:"${getPluginDirForName('swingx-builder').file}/lib/", includes:"*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}