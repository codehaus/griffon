includeTargets << griffonScript("Init")
includePluginScript('eclipse-support', 'EclipseUpdate')

eventPluginInstalled = { fullPluginName ->
    updateEclipseClasspathFile(fullPluginName)
}
eventPluginUninstalled = { msg ->
    updateEclipseClasspathFile()
}
