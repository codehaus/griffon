//
// This script is executed by Griffon after plugin was installed to project.

// check to see if we already have a TrayBuilder
ConfigSlurper configSlurper = new ConfigSlurper()
o = configSlurper.parse(new File("${basedir}/griffon-app/conf/Builder.groovy").toURL())
boolean builderIsSet
o.each() { prefix, v ->
    v.each { builder, views ->
        builderIsSet = builderIsSet || 'griffon.builder.tray.TrayBuilder' == builder
    }
}

if (!builderIsSet) {
    println 'Adding TrayBuilder to Builders.groovy'
    new File("${basedir}/griffon-app/conf/Builder.groovy").append("""
root.'griffon.builder.tray.TrayBuilder'.view = '*'
""")
}

