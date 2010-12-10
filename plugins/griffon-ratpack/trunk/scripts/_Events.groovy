
def eventClosure1 = binding.variables.containsKey('eventSetClasspath') ? eventSetClasspath : {cl->}
eventSetClasspath = { cl ->
    eventClosure1(cl)
    if(compilingPlugin('ratpack')) return
    griffonSettings.dependencyManager.flatDirResolver name: 'griffon-ratpack-plugin', dirs: "${ratpackPluginDir}/addon"
    griffonSettings.dependencyManager.addPluginDependency('ratpack', [
        conf: 'compile',
        name: 'griffon-ratpack-addon',
        group: 'org.codehaus.griffon.plugins',
        version: ratpackPluginVersion
    ])
    griffonSettings.dependencyManager.addPluginDependency('ratpack', [
        conf: 'build',
        name: 'griffon-ratpack-cli',
        group: 'org.codehaus.griffon.plugins',
        version: ratpackPluginVersion
    ])
}

eventCollectArtifacts = { artifactsInfo ->
    if(!artifactsInfo.find{ it.type == 'ratpack' }) {
        artifactsInfo << [type: 'ratpack', path: 'ratpack', suffix: 'RatpackApp']
    }
}

eventStatsStart = { pathToInfo ->
    if(!pathToInfo.find{ it.path == 'ratpack'} ) {
        pathToInfo << [name: 'Ratpack Apps', path: 'ratpack', filetype: ['.groovy']]
    }
}

includeTargets << griffonScript('_GriffonCompile')

ratpackCliDir = new File("${griffonSettings.projectWorkDir}/cli-classes")

eventCleanEnd = {
    if(!compilingPlugin('ratpack')) return
    ant.delete(dir: ratpackCliDir)
}

eventCompileEnd = {
    if(!compilingPlugin('ratpack')) return
    ant.mkdir(dir: ratpackCliDir)

    ant.path(id:'ratpack.compile.classpath') {
        path(refid: "griffon.compile.classpath")
        pathElement(location: classesDirPath)
    }
    compileSources(ratpackCliDir, 'ratpack.compile.classpath') {
        src(path: "${basedir}/src/cli")
        javac(classpathref: 'ratpack.compile.classpath', debug: 'yes')
    }
    ant.copy(todir: ratpackCliDir) {
        fileset(dir: "${basedir}/src/cli") {
            exclude(name: '**/*.java')
            exclude(name: '**/*.groovy')
            exclude(name: '**/.svn')
        }
    }
}

eventPackageAddonEnd = {
    if(!compilingPlugin('ratpack')) return

    cliJarName = "griffon-${pluginName}-cli-${plugin.version}.jar"
    ant.jar(destfile: "$addonJarDir/$cliJarName") {
        fileset(dir: ratpackCliDir)
    }
}
