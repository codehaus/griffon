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

if(!metadata['addon.gsql']) {
   metadata['addon.gsql'] = 'griffon.gsql.GsqlAddon'
   metadataFile.withOutputStream { out ->
     metadata.store out, 'utf-8'
   }
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
