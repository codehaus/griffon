griffon.project.dependency.resolution = {
    // inherit Griffon' default dependencies
    inherits("global") {
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()

        mavenRepo name: 'FEST main', root: 'http://repository.codehaus.org/', m2compatible: true
        mavenCentral()
    }
    dependencies {
        compile('org.easytesting:fest-swing-junit-4.5:1.2.1') { excludes 'junit' }
        compile 'junit:junit:4.8.2'
        text('org.easytesting:fest-swing-junit-4.5:1.2.1') { excludes 'junit' }
        test 'junit:junit:4.8.2'
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (@griffon.version@)"
    }
}
