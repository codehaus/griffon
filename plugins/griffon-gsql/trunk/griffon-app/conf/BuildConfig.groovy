griffon.project.dependency.resolution = {
    inherits("global") 
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenLocal()
    }
    dependencies {
        compile('commons-dbcp:commons-dbcp:1.2.2',
                'commons-logging:commons-logging:1.1.1',
                'commons-pool:commons-pool:1.5.3',
                'hsqldb:hsqldb:1.8.0.10') {
            transitive = false
        }
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
griffon.plugin.pack.additional.sources = ['src/gdsl']
