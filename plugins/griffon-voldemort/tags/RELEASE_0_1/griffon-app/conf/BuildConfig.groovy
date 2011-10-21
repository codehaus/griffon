griffon.project.dependency.resolution = {
    inherits "global"
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
        flatDir name: 'voldemortPluginLib', dirs: 'lib'
    }
    dependencies {
        compile 'voldemort:voldemort:0.90'
        compile('commons-codec:commons-codec:1.3',
                'commons-collections:commons-collections:3.2.1',
                'commons-dbcp:commons-dbcp:1.4',
                'commons-pool:commons-pool:1.5.6',
                'commons-io:commons-io:2.0',
                'commons-lang:commons-lang:2.4',
                'com.google.protobuf:protobuf-java:2.4.1',
                'org.codehaus.jackson:jackson-core-asl:1.9.0',
                'org.codehaus.jackson:jackson-mapper-asl:1.9.0',
                'com.google.collections:google-collections:1.0') {
            exclude group: 'org.slf4j'
            excludes 'httpclient', 'junit', 'commons-logging'            
        }
        compile('org.jdom:jdom:1.1') { transitive = false }
        compile('commons-httpclient:commons-httpclient:3.1') {
            excludes 'commons-logging'
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

