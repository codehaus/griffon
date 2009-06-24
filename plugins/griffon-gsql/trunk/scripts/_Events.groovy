import org.codehaus.griffon.cli.GriffonScriptRunner as GSR
import org.codehaus.griffon.plugins.GriffonPluginUtils

/*
eventSetClasspath = { classLoader ->
    def gsqlPlugin = getPluginDirForName('gsql')
    if( !gsqlPlugin ) return
    ant.fileset(dir:"${gsqlPlugin.file}/lib/", includes:"*.jar").each { jar ->
        classLoader.addURL( jar.file.toURI().toURL() )
    }
}
*/

eventPackagePluginStart = { pluginName, plugin ->
    def destFileName = "lib/griffon-${pluginName}-addon-${plugin.version}.jar"
    ant.delete(dir: destFileName, quiet: false, failOnError: false)
    ant.jar(destfile: destFileName) {
        fileset(dir: classesDirPath) {
            exclude(name:'_*.class')
            exclude(name:'*GriffonPlugin.class')
        }
    }
}

eventCopyLibsEnd = { jardir ->
    ant.fileset(dir:"${getPluginDirForName('gsql').file}/lib/", includes:"*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}

getPluginDirForName = { String pluginName ->
    // pluginsHome = griffonSettings.projectPluginsDir.path
    GriffonPluginUtils.getPluginDirForName(pluginsHome, pluginName)
}
