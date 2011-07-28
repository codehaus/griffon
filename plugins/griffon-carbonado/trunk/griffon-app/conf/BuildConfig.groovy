griffon.project.dependency.resolution = {
    inherits "global"  
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
        flatDir name: 'carbonadoPluginLib', dirs: 'lib'
    }
    dependencies {
        compile('commons-dbcp:commons-dbcp:1.2.2',
                'commons-pool:commons-pool:1.5.3',
                'hsqldb:hsqldb:1.8.0.10',
                'joda-time:joda-time:1.6.2') {
            transitive = false
        }
        compile 'com.sleepycat:berkeleydb-je:4.0.92',
                'com.amazon:carbonado:1.2.2',
                'com.amazon:carbonado-sleepycat-db:1.2.2',
                'com.amazon:carbonado-sleepycat-je:1.2.2',
                'org.cojen:cojen:2.2.2'
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (@griffon.version@)"
    }
}

griffon.jars.destDir='target/addon'

//griffon.jars.jarName='CarbonadoGriffonAddon.jar'
