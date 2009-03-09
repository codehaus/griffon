import org.codehaus.griffon.cli.GriffonScriptRunner as GSR

eventSetClasspath = { classLoader ->
    def scalaPlugin = getPluginDirForName("scala")
    if( !scalaPlugin ) return
    ant.fileset(dir:"${scalaPlugin.file}/lib/", includes:"*.jar").each { jar ->
        classLoader.adURL( jar.toURI().toURL() )
    }
}

eventCopyLibsEnd = { jardir ->
    ant.fileset(dir:"${getPluginDirForName('scala').file}/lib/", includes:"*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}

eventCompileStart = { type ->
    if( type != "source" ) return
    def scalaSrc = "${basedir}/src/scala"
    if( !new File(scalaSrc).exists() ) return
    def scalaPlugin = getPluginDirForName("scala")

    ant.path(id : "scalaJarSet") {
       fileset(dir: "${scalaPlugin.file}/lib" , includes : "*.jar")
    }
    ant.taskdef(resource: "scala/tools/ant/antlib.xml",
                classpathref: "scalaJarSet")
    ant.path( id: "scala.compile.classpath" ) {
       path(refid:"griffon.compile.classpath")
       path(refid:"scalaJarSet")
    }
    ant.scalac(destdir: classesDirPath,
               classpathref: "scala.compile.classpath",
               encoding:"UTF-8") {
        src(path: scalaSrc)
    }
}