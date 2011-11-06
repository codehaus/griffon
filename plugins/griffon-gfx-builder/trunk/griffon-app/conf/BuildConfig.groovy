griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()

        mavenLocal()
        mavenCentral()
    }
    dependencies {
        compile('org.codehaus.griffon:gfxbuilder-core:0.5.2')   { excludes 'groovy-all' }
        compile('org.codehaus.griffon:gfxbuilder-svg:0.5.2')    { excludes 'groovy-all' }
        compile('org.codehaus.griffon:gfxbuilder-swingx:0.5.2') { excludes 'groovy-all' }
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (0.9)"
    }
}

griffon.jars.destDir='target/addon'
griffon.plugin.pack.additional.sources = ['src/gdsl']
