griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()

        mavenCentral()
        mavenRepo ' https://repository.jboss.org/nexus/content/groups/public-jboss'
    }
    dependencies {
        runtime 'xml-apis:xml-apis-ext:1.3.04',
                'batik:batik-anim:1.7',
                'batik:batik-awt-util:1.7',
                'batik:batik-bridge:1.7',
                'batik:batik-codec:1.7',
                'batik:batik-css:1.7',
                'batik:batik-dom:1.7',
                'batik:batik-ext:1.7',
                'batik:batik-extension:1.7',
                'batik:batik-gui-util:1.7',
                'batik:batik-gvt:1.7',
                'batik:batik-parser:1.7',
                'batik:batik-script:1.7',
                'batik:batik-svg-dom:1.7',
                'batik:batik-svggen:1.7',
                'batik:batik-swing:1.7',
                'batik:batik-transcoder:1.7',
                'batik:batik-util:1.7',
                'batik:batik-xml:1.7'
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
