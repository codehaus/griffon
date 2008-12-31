//
// This script is executed by Griffon after plugin was installed to project.

// check to see if we already have a MacWidgets Builder
ConfigSlurper configSlurper = new ConfigSlurper()
o = configSlurper.parse(new File("${basedir}/griffon-app/conf/Builder.groovy").toURL())
boolean builderIsSet
o.each() { prefix, v ->
    v.each { builder, views ->
        builderIsSet = builderIsSet || 'griffon.builder.macwidgets.MacWidgetsBuilder' == builder
    }
}

if (!builderIsSet) {
    println 'Adding MacWidgetsBuilder to Builders.groovy'
    new File("${basedir}/griffon-app/conf/Builder.groovy").append("""
root.'griffon.builder.macwidgets.MacWidgetsBuilder'.view = '*'
""")
}
