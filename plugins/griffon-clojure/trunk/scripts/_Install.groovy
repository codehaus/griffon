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

// check if ClojureAddon needs to be defined
/*
o = configSlurper.parse(new File("${basedir}/griffon-app/conf/Builder.groovy").toURL())
boolean addonIsSet
o.each() { prefix, v ->
    v.each { key, views ->
        addonIsSet = addonIsSet || 'griffon.clojure.ClojureAddon' == key
    }
}
if (!addonIsSet) {
    println 'Adding ClojureAddon to Builders.groovy'
    new File("${basedir}/griffon-app/conf/Builder.groovy").append("""
root.'griffon.clojure.ClojureAddon'.controller = '*'
""")
}
*/

def checkOptionIsSet = { where, option ->
   boolean optionIsSet = false
   where.each { prefix, v ->
       v.each { key, views ->
           optionIsSet = optionIsSet || option == key
       }
   }
   optionIsSet
}

builderConfig = configSlurper.parse(new File("${basedir}/griffon-app/conf/Builder.groovy").toURL())
if(!checkOptionIsSet(builderConfig, "griffon.clojure.ClojureAddon")) {
    println 'Adding ClojureAddon to Builders.groovy'
    new File("${basedir}/griffon-app/conf/Builder.groovy").append("""
root.'griffon.clojure.ClojureAddon'.controller = '*'
""")
}

// append hints for config options if not present
appConfig = configSlurper.parse(new File("${basedir}/griffon-app/conf/Application.groovy").toURL())
if(!checkOptionIsSet(appConfig, "griffon.clojure.dynamicProperty")) {
    new File("${basedir}/griffon-app/conf/Application.groovy").append("""
griffon.clojure.dynamicPropertyName = "clj"
""")
}
if(!checkOptionIsSet(appConfig, "griffon.clojure.injectInto")) {
    new File("${basedir}/griffon-app/conf/Application.groovy").append("""
griffon.clojure.injectInto = ["controller"]
""")
}

ant.mkdir(dir: "${basedir}/src/clojure")
ant.mkdir(dir: "${basedir}/griffon-app/resources/clj")
