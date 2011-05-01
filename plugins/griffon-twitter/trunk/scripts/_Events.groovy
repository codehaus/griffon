

def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)
    if(compilingPlugin('twitter')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-twitter-plugin', dirs: "${twitterPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('twitter', [
        conf: 'compile',
        name: 'griffon-twitter-addon',
        group: 'org.codehaus.griffon.plugins',
        version: twitterPluginVersion
    ])
}
