def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)
    if(compilingPlugin('jtreemap')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-jtreemap-plugin', dirs: "${jtreemapPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('jtreemap', [
        conf: 'compile',
        name: 'griffon-jtreemap-addon',
        group: 'org.codehaus.griffon.plugins',
        version: jtreemapPluginVersion
    ])
}

