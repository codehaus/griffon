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
includeTargets << griffonScript("_GriffonCreateArtifacts")

// check to see if we already have a GormGriffonAddon
ConfigSlurper configSlurper1 = new ConfigSlurper()
def slurpedBuilder1 = configSlurper1.parse(new File("$basedir/griffon-app/conf/Builder.groovy").toURL())
boolean addonIsSet1
slurpedBuilder1.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'GormGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding GormGriffonAddon to Builders.groovy'
    new File("$basedir/griffon-app/conf/Builder.groovy").append('''
root.'GormGriffonAddon'.addon=true
''')
}

ant.mkdir(dir: "${basedir}/griffon-app/domain")

if(!new File("${basedir}/griffon-app/conf/DataSource.groovy").exists()) {
   createArtifact(
      name: "DataSource",
      suffix: "",
      type: "DataSource",
      path: "griffon-app/conf")
}

if(!new File("${basedir}/griffon-app/conf/BootstrapGorm.groovy").exists()) {
   createArtifact(
      name: "BootstrapGorm",
      suffix: "",
      type: "BootstrapGorm",
      path: "griffon-app/conf")
}
