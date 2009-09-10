import org.codehaus.griffon.cli.GriffonScriptRunner as GSR
import org.codehaus.griffon.plugins.GriffonPluginUtils

eventPackagePluginStart = { pluginName, plugin ->
    def destFileName = "lib/griffon-${pluginName}-addon-${plugin.version}.jar"
    ant.delete(dir: destFileName, quiet: false, failOnError: false)
    ant.jar(destfile: destFileName) {
        fileset(dir: classesDirPath) {
            exclude(name: '_*.class')
            exclude(name: '*GriffonPlugin.class')
        }
    }
}

eventCopyLibsEnd = { jardir ->
    ant.fileset(dir: "${getPluginDirForName('gsql').file}/lib/", includes: "*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}

getPluginDirForName = { String pluginName ->
    // pluginsHome = griffonSettings.projectPluginsDir.path
    GriffonPluginUtils.getPluginDirForName(pluginsHome, pluginName)
}
