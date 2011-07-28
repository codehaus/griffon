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
        build('org.spockframework:spock-core:0.5-groovy-1.8') {
            excludes 'groovy-all', 'asm', 'ant'
        }
        test('org.spockframework:spock-core:0.5-groovy-1.8') {
            excludes 'groovy-all', 'asm', 'ant'
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
