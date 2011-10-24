

def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)
    if(compilingPlugin('viewaswing')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-viewaswing-plugin', dirs: "${viewaswingPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('viewaswing', [
        conf: 'compile',
        name: 'griffon-viewaswing-addon',
        group: 'org.codehaus.griffon.plugins',
        version: viewaswingPluginVersion
    ])
}
