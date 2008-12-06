import org.codehaus.griffon.cli.GriffonScriptRunner as GSR

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