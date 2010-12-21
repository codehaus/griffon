griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        flatDir name: 'chartsPluginLib', dirs: 'lib'
    }
    dependencies {
        compile 'com.thecoderscorner:groovychart:0.1BETA',
                'jfree:jfreechart:1.0.13',
                'jfree:jcommon:1.0.16'
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

