griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        flatDir name: 'lookandfeelJgoodiesLooksPluginLib', dirs: 'lib'
    }
    dependencies {
        compile 'com.jgoodies:jgoodies-looks:2.4.0',
                'com.jgoodies:jgoodies-common:1.1.1'
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
