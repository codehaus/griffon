
eventCopyLibsEnd = { jardir ->
    if (!isPluginProject) {
        def pluginDir = getPluginDirForName('swingx-builder')
        if(pluginDir?.file?.exists()) {
            ant.fileset(dir: "${pluginDir.file}/lib/", includes: '*.jar').each {
                griffonCopyDist(it.toString(), jardir)
            }
        }
    }
}
