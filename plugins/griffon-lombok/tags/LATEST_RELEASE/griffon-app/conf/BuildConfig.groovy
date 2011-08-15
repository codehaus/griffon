griffon.project.dependency.resolution = {
    inherits "global"
    log "warn" 
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenRepo 'http://repository.sonatype.org/content/groups/public'
    }
    dependencies {
        compile('org.projectlombok:lombok:0.10.0-RC3') {
            exported = true
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
