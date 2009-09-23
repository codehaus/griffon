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

includeTargets << griffonScript("_GriffonInit")

def checkOptionIsSet = { where, option ->
   boolean optionIsSet = false
   where.each { prefix, v ->
       v.each { key, views ->
           optionIsSet = optionIsSet || option == key
       }
   }
   optionIsSet
}

ConfigSlurper configSlurper = new ConfigSlurper()

// check if JGoodiesFormsAddon needs to be defined
builderConfig = configSlurper.parse(new File("${basedir}/griffon-app/conf/Builder.groovy").toURL())
if(!checkOptionIsSet(builderConfig, "griffon.jgoodies.JGoodiesFormsAddon")) {
    println 'Adding JGoodiesFormsAddon to Builders.groovy'
    new File("${basedir}/griffon-app/conf/Builder.groovy").append("""
root.'griffon.jgoodies.JGoodiesFormsAddon'.view = '*'
""")
}
