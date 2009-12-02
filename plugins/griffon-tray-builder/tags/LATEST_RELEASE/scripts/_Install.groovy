//
// This script is executed by Griffon after plugin was installed to project.

// check to see if we already have a TrayBuilderGriffonAddon
ConfigSlurper configSlurper1 = new ConfigSlurper()
def slurpedBuilder1 = configSlurper1.parse(new File("$basedir/griffon-app/conf/Builder.groovy").toURL())
boolean addonIsSet1
slurpedBuilder1.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'TrayBuilderGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding TrayBuilderGriffonAddon to Builders.groovy'
    new File("$basedir/griffon-app/conf/Builder.groovy").append('''
root.'TrayBuilderGriffonAddon'.addon=true
''')
}
