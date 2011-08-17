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
        compile('net.sourceforge.jeuclid:jeuclid-core16:3.1.9',
                'net.sourceforge.jeuclid:jeuclid-core:3.1.9') {
            transitive = false
        }
        compile('org.apache.xmlgraphics:xmlgraphics-commons:1.4') {
            excludes 'commons-logging'
        }
        compile 'xml-apis:xml-apis-ext:1.3.04',
                'batik:batik-svg-dom:1.7',
                'batik:batik-svggen:1.7',
                'batik:batik-dom:1.7',
                'batik:batik-xml:1.7',
                'batik:batik-util:1.7',
                'batik:batik-ext:1.7'
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
