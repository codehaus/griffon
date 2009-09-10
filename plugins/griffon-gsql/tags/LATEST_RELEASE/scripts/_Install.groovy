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

ConfigSlurper configSluper = new ConfigSlurper()
o = configSlurper.parse(new File("${basedir}/griffon-app/conf/Builder.groovy").toURL())
boolean addonIsSet
o.each() { prefix, v ->
    v.each { key, views ->
        addonIsSet = addonIsSet || 'griffon.gsql.GsqlAddon' == key
    }
}

if (!addonIsSet) {
    println 'Adding GSQLAddon to Builders.groovy'
    new File("${basedir}/griffon-app/conf/Builder.groovy").append("""
root.'griffon.gsql.GsqlAddon'.controller = '*'
""")
}

if(!new File("${basedir}/griffon-app/conf/DataSource.groovy").exists()) {
   createArtifact(
      name: "DataSource",
      suffix: "",
      type: "DataSource",
      path: "griffon-app/conf")
}

if(!new File("${basedir}/griffon-app/conf/BootStrapGsql.groovy").exists()) {
   createArtifact(
      name: "BootStrapGsql",
      suffix: "",
      type: "BootStrapGsql",
      path: "griffon-app/conf")
}

if(!new File("${basedir}/griffon-app/conf/Schema.groovy").exists()) {
   createArtifact(
      name: "Schema",
      suffix: "",
      type: "Schema",
      path: "griffon-app/conf")
}
