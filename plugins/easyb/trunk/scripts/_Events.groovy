import org.codehaus.griffon.cli.GriffonScriptRunner as GSR

eventSetClasspath = { rootLoader ->
    def addEasybJars = { base ->
        def dir = new File( base, "lib/test" )
        if( dir.exists() ) {
            dir.eachFileMatch( ~/.*\.jar/ ) { f ->
                rootLoader?.addURL(f.toURI().toURL())
            }
        }
    }
    addEasybJars(new File("${basedir}"))
    try{ addEasybJars(getPluginDirForName('easyb').file) }
    catch( ex ) { /* ignore */ }
}

eventCleanEnd = {
    Ant.delete(dir: "${projectWorkDir}/easyb-classes", failonerror: false)
    Ant.delete(dir: "${basedir}/test/easyb-reports", failonerror: false)
}

eventAllTestsStart = { info, unitOnly, integrationOnly ->
   // perform any cleanup before running fest tests here!
}

eventAllTestsEnd = { info, unitOnly, integrationOnly ->
   if( unitOnly || integrationOnly ) return

   // call run-fest after all other tests have run
   GSR.callPluginOrGriffonScript("RunEasyb")
}

eventJarFilesStart = {
   // make sure EasybGriffonPlugin.class is not added to app jar
   Ant.delete(file: "${projectWorkDir}/classes/EasybGriffonPlugin.class", failonerror: false)
}