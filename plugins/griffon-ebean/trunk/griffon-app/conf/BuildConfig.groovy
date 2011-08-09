griffon.project.dependency.resolution = {
    inherits "global"
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
    }
    dependencies {
        compile('org.avaje:ebean:2.7.3') {
            excludes 'servlet-api', 'ant', 'scala-library', 'maven-plugin-api'
        }
        runtime 'javax.persistence:persistence-api:1.0'
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (@griffon.version@)"
    }
}
