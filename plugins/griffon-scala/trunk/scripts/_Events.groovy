import org.codehaus.griffon.cli.GriffonScriptRunner as GSR
import org.codehaus.griffon.plugins.GriffonPluginUtils

includeTargets << griffonScript("Init")
includePluginScript("scala", "_ScalaCommon")

eventCopyLibsEnd = { jardir ->
    if(compilingScalaPlugin()) return
    adjustScalaHome()
    ant.echo(message: "[scala] Copying Scala jar files from ${scalaHome}/lib")

    ant.fileset(dir:"${scalaHome}/lib/", includes:"*.jar").each {
        griffonCopyDist(it.toString(), jardir)
    }
}

eventCompileStart = { type ->
    if(compilingScalaPlugin()) return
    if(type != "source") return
    compileScalaSrc()
}
 
/*
eventTestCompileStart = { type ->
    if(compilingScalaPlugin()) return
    if(type != "scala") return
    compileScalaTest()
}

eventTestPhasesStart = { phasesToRun ->
    if("other" in phasesToRun && (new File("${basedir}/test/scala")).exists() && !("scala" in otherTests)) {
        otherTests << "scala"
    }
}
*/

private boolean compilingScalaPlugin() { getPluginDirForName("scala") == null }
