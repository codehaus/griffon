

def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)
    if(compilingPlugin('airbag')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-airbag-plugin', dirs: "${airbagPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('airbag', [
        conf: 'compile',
        name: 'griffon-airbag-addon',
        group: 'org.codehaus.griffon.plugins',
        version: airbagPluginVersion
    ])
}
