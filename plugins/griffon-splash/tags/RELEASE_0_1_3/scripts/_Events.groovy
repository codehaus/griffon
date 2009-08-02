import griffon.util.GriffonApplicationUtils

eventPackagePluginStart = { pluginName, plugin ->
println "===> Package Plugin Start"
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
    ant.fileset(dir:"${getPluginDirForName('splash').file}/lib/", includes:"*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
	
	
}

eventPluginInstalled = { fullPluginName ->
	//Ant.echo( "XXXXXXXXXXXXXXX ${basedir}/griffon-app/resources")
	//Ant.echo( "YYYYYYYYYYYYYYY ${getPluginDirForName('splash').file}/griffon-app/resources/")
	Ant.copy(todir:"${basedir}/griffon-app/resources") {
            fileset(dir:"${getPluginDirForName('splash').file}/griffon-app/resources/", includes:"**/*.*")
    }
}
