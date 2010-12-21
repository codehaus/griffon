//
// This script is executed by Griffon after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
//
//    ant.mkdir(dir:"${basedir}/griffon-app/jobs")
//

import org.codehaus.griffon.resolve.IvyDependencyManager
import griffon.util.GriffonUtil

// Workaround for GRAILS-5755
if (GriffonUtil.griffonVersion == '0.9') {
    // Update the cached dependencies in griffonSettings, and add new jars to the root loader
    ['compile', 'build', 'test', 'runtime'].each { type ->
        def existing = griffonSettings."${type}Dependencies"
        def all = griffonSettings.dependencyManager.resolveDependencies(IvyDependencyManager."${type.toUpperCase()}_CONFIGURATION").allArtifactsReports.localFile
        def toAdd = all - existing
        if (toAdd) {
            existing.addAll(toAdd)
            if (type in ['build', 'test']) {
                toAdd.each {
                    griffonSettings.rootLoader.addURL(it.toURL())
                }
            }
        }
    }
}

