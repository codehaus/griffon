griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
        mavenRepo 'http://repository.jboss.org/nexus/content/groups/public'
    }
    dependencies {
        compile('org.jboss.weld.se:weld-se-core:1.1.2.Final') {
            excludes 'junit', 'slf4j-simple', 'slf4j-ext'
        }
        compile 'org.slf4j:slf4j-ext:1.6.1'
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
