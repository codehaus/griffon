griffon.project.dependency.resolution = {
    inherits("global") 
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()

        mavenCentral()
    }
    dependencies {
        compile('org.codehaus.groovy:groovy-xmlrpc:0.7') {
            transitive = false
        }
        compile 'jivesoftware:smack:3.1.0',
                'jivesoftware:smackx:3.1.0'
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (@griffon.version@)"
    }
}