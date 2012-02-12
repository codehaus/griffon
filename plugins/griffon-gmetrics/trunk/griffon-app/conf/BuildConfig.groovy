griffon.project.dependency.resolution = {
    inherits 'global'
    log 'warn'
    repositories {
        griffonHome()
        mavenCentral()
    }
    dependencies {
        build('org.gmetrics:GMetrics:0.5') { transitive = false }
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (@griffon.version@)"
    }
}
