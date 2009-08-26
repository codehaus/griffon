import griffon.util.GriffonApplicationUtils

eventPackagePluginStart = { pluginName, plugin ->
    if( !GriffonApplicationUtils.isJdk16 ) {
        ant.fail("Plugin $pluginName requires Jdk1.6 or above to be packaged, current Jdk is ${System.getProperty('java.version')}")
    }
    def destFileName = "lib/$pluginName-${plugin.version}.jar"
    ant.delete(dir: destFileName, quiet: true, failOnError: false)
    ant.copy(todir: classesDirPath) {
        fileset(dir:"${basedir}/src/main", includes:"**/*.xml")
    }
    ant.jar(destfile: destFileName) {
        fileset(dir: classesDirPath) {
            exclude(name:'_*.class')
            exclude(name:'*GriffonPlugin.class')
        }
    }
}

eventCopyLibsEnd = { jardir ->
    ant.fileset(dir: "${getPluginDirForName('transitions').file}/lib/", includes: "*.jar", excludes: "trident*").each {
        griffonCopyDist(it.toString(), jardir)
    }
}

