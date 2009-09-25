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
builderConfig = configSlurper.parse(new File("${basedir}/griffon-app/conf/Builder.groovy").toURL())
if(!checkOptionIsSet(builderConfig, "griffon.gsql.GsqlAddon")) {
    println 'Adding GsqlAddon to Builders.groovy'
    new File("${basedir}/griffon-app/conf/Builder.groovy").append("""
root.'griffon.gsql.GsqlAddon'.controller = '*'
""")
}

// append hints for config options if not present
appConfig = configSlurper.parse(new File("${basedir}/griffon-app/conf/Application.groovy").toURL())
if(!checkOptionIsSet(appConfig, "griffon.gsql.injectInto")) {
    new File("${basedir}/griffon-app/conf/Application.groovy").append("""
griffon.gsql.injectInto = ["controller"]
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
