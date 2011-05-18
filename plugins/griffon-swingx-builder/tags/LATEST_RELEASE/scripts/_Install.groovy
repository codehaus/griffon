//
// This script is executed by Griffon after plugin was installed to project.

// check to see if we already have a SwingX Builder
ConfigSlurper configSlurper = new ConfigSlurper()
o = configSlurper.parse(new File("${basedir}/griffon-app/conf/Builder.groovy").toURL())
boolean builderIsSet
o.each() { prefix, v ->
    v.each { builder, views ->
        builderIsSet = builderIsSet || 'groovy.swing.SwingXBuilder' == builder
    }
}

if (!builderIsSet) {
    println 'Adding SwingXBuilder to Builders.groovy'
    new File("${basedir}/griffon-app/conf/Builder.groovy").append("""
jx {
    'groovy.swing.SwingXBuilder' {
        view = '*'
    }
}
""")
}

