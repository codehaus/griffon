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

// check to see if we already have a RatpackGriffonAddon
boolean addonIsSet1
builderConfig.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'RatpackGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding RatpackGriffonAddon to Builder.groovy'
    builderConfigFile.append('''
root.'RatpackGriffonAddon'.addon=true
''')
}

ant.mkdir(dir: "${basedir}/resources/ratpack/templates")
ant.mkdir(dir: "${basedir}/resources/ratpack/public")
