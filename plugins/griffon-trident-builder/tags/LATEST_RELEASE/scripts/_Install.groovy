//
// This script is executed by Griffon after plugin was installed to project.

// check to see if we already have a Trident Builder
ConfigSlurper configSlurper = new ConfigSlurper()
o = configSlurper.parse(new File("${basedir}/griffon-app/conf/Builder.groovy").toURL())
boolean builderIsSet
o.each() { prefix, v ->
    v.each { builder, views ->
        builderIsSet = builderIsSet || 'griffon.builder.trident.TridentBuilder' == builder
    }
}

if (!builderIsSet) {
    println 'Adding TridentBuilder to Builders.groovy'
    new File("${basedir}/griffon-app/conf/Builder.groovy").append("""
root.'griffon.builder.trident.TridentBuilder'.view = '*'
""")
}