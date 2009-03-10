import org.codehaus.griffon.cli.GriffonScriptRunner as GSR
import org.codehaus.griffon.plugins.GriffonPluginUtils

eventSetClasspath = { classLoader ->
    def wizardPlugin = getPluginDirForName('wizard')
    if( !wizardPlugin ) return
    ant.fileset(dir:"${wizardPlugin.file}/lib/", includes:"*.jar").each { jar ->
        classLoader.addURL( jar.file.toURI().toURL() )
    }
}

eventPackagePluginStart = { pluginName, plugin ->
    def destFileName = "lib/$pluginName-${plugin.version}.jar"
    ant.delete(dir: destFileName, quiet: false, failOnError: false)
    ant.jar(destfile: destFileName) {
        fileset(dir: classesDirPath) {
            exclude(name:'CreateWizardPage*')
            exclude(name:'_*.class')
            exclude(name:'*GriffonPlugin.class')
        }
    }
}

eventCopyLibsEnd = { jardir ->
    ant.fileset(dir:"${getPluginDirForName('wizard').file}/lib/", includes:"*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}

eventCompileStart = { type ->
    if( type != "source" ) return
    def wizardSrc = new File("${basedir}/griffon-app/wizards")
    if( !wizardSrc.exists() ) return
    String classpathId = "griffon.compile.classpath"
    compileSources(classpathId) {
        src(path: "${basedir}/griffon-app/wizards")
    }
}

getPluginDirForName = { String pluginName ->
    // pluginsHome = griffonSettings.projectPluginsDir.path
    GriffonPluginUtils.getPluginDirForName(pluginsHome, pluginName)
}
