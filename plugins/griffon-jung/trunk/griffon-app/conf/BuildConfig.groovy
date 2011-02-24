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
        compile 'net.sf.jung:jung-api:2.0.1',
                'net.sf.jung:jung-io:2.0.1',
                'net.sf.jung:jung-jai:2.0.1',
                'net.sf.jung:jung-algorithms:2.0.1',
                'net.sf.jung:jung-visualization:2.0.1'
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
griffon.plugin.pack.additional.sources = ['src/gdsl']
