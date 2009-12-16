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

// check to see if we already have a JmonkeyengineGriffonAddon
ConfigSlurper configSlurper1 = new ConfigSlurper()
def slurpedBuilder1 = configSlurper1.parse(new File("$basedir/griffon-app/conf/Builder.groovy").toURL())
boolean addonIsSet1
slurpedBuilder1.each() { prefix, v ->
    v.each { builder, views ->
        addonIsSet1 = addonIsSet1 || 'JmonkeyengineGriffonAddon' == builder
    }
}

if (!addonIsSet1) {
    println 'Adding JmonkeyengineGriffonAddon to Builders.groovy'
    new File("$basedir/griffon-app/conf/Builder.groovy").append('''
root.'JmonkeyengineGriffonAddon'.addon=true
''')
}

def simpleGameAppClass = "griffon.jme.app.SimpleGameGriffonApplication"
def simpleGameDelegate = "MySimpleGameDelegate"
def buildconf = configSlurper1.parse(new File("$basedir/griffon-app/conf/Config.groovy").toURL())
if(!(simpleGameAppClass in buildconf.flatten().'griffon.application.mainClass')) {
    println "Setting '$simpleGameAppClass' as main class"
    new File("$basedir/griffon-app/conf/Config.groovy").append("""
griffon.application.mainClass = "$simpleGameAppClass"
""")
}
def appconf = configSlurper1.parse(new File("$basedir/griffon-app/conf/Application.groovy").toURL())
if(!(simpleGameDelegate in appconf.flatten().'jme.simpleGameDelegate')) {
    println "Setting '$simpleGameDelegate' as jme.simpleGameDelegate"
    new File("$basedir/griffon-app/conf/Application.groovy").append("""
jme.simpleGameDelegate = "$simpleGameDelegate"
""")
}

ant.copy(file: "${getPluginDirForName('jmonkeyengine').file}/src/templates/MySimpleGameDelegate.groovy", todir: "${basedir}/src/main")
