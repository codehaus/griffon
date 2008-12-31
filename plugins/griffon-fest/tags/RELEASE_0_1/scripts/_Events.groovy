import org.codehaus.griffon.cli.GriffonScriptRunner as GSR

eventCleanEnd = {
    Ant.delete(dir: "${projectWorkDir}/fest-classes", failonerror: false)
    Ant.delete(dir: "${basedir}/test/fest-reports", failonerror: false)
}

eventAllTestsStart = { info, unitOnly, integrationOnly ->
   // perform any cleanup before running fest tests here!
}

eventAllTestsEnd = { info, unitOnly, integrationOnly ->
   if( unitOnly || integrationOnly ) return

   // call run-fest after all other tests have run
   GSR.callPluginOrGriffonScript("RunFest")
}

eventJarFilesStart = {
   // make sure FestGriffonPlugin.class is not added to app jar
   Ant.delete(file: "${projectWorkDir}/classes/FestGriffonPlugin.class", failonerror: false)
}