griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
        flatDir name: 'jideBuilderPluginLib', dirs: 'lib'
    }
    dependencies {
        compile 'com.jidesoft:jide-oss:2.6.2',
                'com.kitfox.svg:svg-salamander:1.0',
                'org.codehaus.griffon:jidebuilder:2.2'
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
