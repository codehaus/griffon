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

// check to see if we already have a MongoDBGriffonAddon
ConfigSlurper configSlurper1 = new ConfigSlurper()
def slurpedBuilder1 = configSlurper1.parse(new File("$basedir/griffon-app/conf/Builder.groovy").toURL())
boolean addonIsSet1
slurpedBuilder1.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'MongoDBGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding MongoDBGriffonAddon to Builders.groovy'
    new File("$basedir/griffon-app/conf/Builder.groovy").append('''
root.'MongoDBGriffonAddon'.addon=true
''')
}
// check to see if we already have a TestGriffonAddon
ConfigSlurper configSlurper2 = new ConfigSlurper()
def slurpedBuilder2 = configSlurper2.parse(new File("$basedir/griffon-app/conf/Builder.groovy").toURL())
boolean addonIsSet2
slurpedBuilder2.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet2 = addonIsSet2 || 'TestGriffonAddon' == builder
    }
}

if (!addonIsSet2) {
    println 'Adding TestGriffonAddon to Builder.groovy'
    new File("$basedir/griffon-app/conf/Builder.groovy").append('''
root.'TestGriffonAddon'.addon=true
''')
}