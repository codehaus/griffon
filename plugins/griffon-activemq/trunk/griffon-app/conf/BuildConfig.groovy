griffon.project.dependency.resolution = {
    // inherit Griffon' default dependencies
    inherits "global"
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
        mavenRepo "http://download.java.net/maven/2/"
        mavenRepo "http://repository.jboss.org/nexus/content/groups/public-jboss/"
    }
    dependencies {
        compile("org.apache.activemq:activemq-core:5.5.0") {
            transitive = false
        }
        compile("org.apache.activemq:kahadb:5.5.0") {
            transitive = false
        }
        compile("org.apache.activemq.protobuf:activemq-protobuf:1.1") {
            transitive = false
        }
        compile("org.apache.activemq:activeio-core:3.1.2") {
            transitive = false
        }
        compile "org.apache.geronimo.specs:geronimo-j2ee-management_1.1_spec:1.0.1"
        compile "org.apache.geronimo.specs:geronimo-jms_1.1_spec:1.1.1"
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (@griffon.version@)"
    }
}
