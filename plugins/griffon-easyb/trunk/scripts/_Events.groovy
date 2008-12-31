import org.codehaus.griffon.cli.GriffonScriptRunner as GSR

eventCleanEnd = {
    ant.delete(dir: "${projectWorkDir}/easyb-classes", failonerror: false)
    ant.delete(dir: "${basedir}/test/easyb-reports", failonerror: false)
}

eventAllTestsStart = { info, unitOnly, integrationOnly ->
   // perform any cleanup before running fest tests here!
}

eventAllTestsEnd = { info, unitOnly, integrationOnly ->
   if( unitOnly || integrationOnly ) return

   // call run-fest after all other tests have run
   GSR.callPluginOrGriffonScript("RunEasyb")
}

eventCopyLibsEnd = { jardir ->
   ant.delete(dir:jardir, includes: "**/easyb.*")
}