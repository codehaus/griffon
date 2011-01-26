griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        flatDir name: 'cssBuilderPluginLib', dirs: 'lib'
    }
    dependencies {
        runtime 'net.sourceforge.cssparser:cssparser:0.9.5',
                'org.w3c.css:sac:1.3',
                'org.codehaus.griffon:cssbuilder:0.4'
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
