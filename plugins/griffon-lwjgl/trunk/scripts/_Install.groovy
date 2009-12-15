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

// check to see if we already have a LwjglGriffonAddon
ConfigSlurper configSlurper1 = new ConfigSlurper()
def slurpedBuilder1 = configSlurper1.parse(new File("$basedir/griffon-app/conf/Builder.groovy").toURL())
boolean addonIsSet1
slurpedBuilder1.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'LwjglGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding LwjglGriffonAddon to Builders.groovy'
    new File("$basedir/griffon-app/conf/Builder.groovy").append('''
root.'LwjglGriffonAddon'.addon=true
''')
}

def lwjgl_version = "2.2.1"
def buildconf = configSlurper1.parse(new File("$basedir/griffon-app/conf/Config.groovy").toURL())
if(!buildconf.flatten().'lwjgl.jnlp.resources') {
    println "Adding LWJGL jnlp extension to configuration"
    def output = "lwjgl.jnlp.resources = ["
    for(os in ['linux',' macosx', 'windows', 'solaris']) {
        output += """\n    [os: os, nativelibs: ["webstart/lwjgl-${lwjgl_version}-native-${os}.jar"],"""
    } 
    output += "\n]\n"
    new File("$basedir/griffon-app/conf/Config.groovy").append(output)
}
