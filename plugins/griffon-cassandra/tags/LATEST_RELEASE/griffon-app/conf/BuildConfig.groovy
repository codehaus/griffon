griffon.project.dependency.resolution = {
    inherits "global"
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        flatDir name: 'cassandraPluginLib', dirs: 'lib'
        mavenCentral()
    }
    dependencies {
        compile('org.apache.cassandra:cassandra-jdbc:1.0.5-SNAPSHOT',
                'org.apache.cassandra:cassandra-thrift:1.0.2',
                'org.apache.cassandra:cassandra-clientutil:1.0.2') {
            exclude 'junit'            
        }
        compile('commons-dbcp:commons-dbcp:1.4',
                'commons-pool:commons-pool:1.5.6') {
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
