griffon.project.dependency.resolution = {
    inherits "global"
    log "warn"
    repositories {
        mavenCentral()
        mavenRepo 'https://repository.jboss.org/nexus/content/groups/public-jboss'
    }
    dependencies {
        build 'org.apache.avro:avro:1.4.1'
        runtime('org.apache.avro:avro:1.4.1') { transitive = false }
        runtime('org.codehaus.jackson:jackson-mapper-asl:1.6.2')
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (@griffon.version@)"
    }
}
