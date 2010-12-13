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
        compile('org.easyb:easyb:0.9.8') { excludes 'commons-logging', 'groovy-all'; export = false }
        test('org.easyb:easyb:0.9.8') { excludes 'commons-logging', 'groovy-all' }
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
