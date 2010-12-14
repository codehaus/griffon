griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()

        mavenCentral()
        flatDir name: 'processingPluginLib', dirs: 'lib'
    }
    dependencies {
        def processingVersion = '1.2.1'
        compile "org.processing:processing-core:$processingVersion"
        runtime "org.processing:processing-net:$processingVersion",
                "org.processing:processing-opengl:$processingVersion",
                "org.processing:processing-pdf:$processingVersion",
                "org.processing:processing-dxf:$processingVersion",
                "org.processing:processing-serial:$processingVersion",
                "org.processing:processing-video:$processingVersion"
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
