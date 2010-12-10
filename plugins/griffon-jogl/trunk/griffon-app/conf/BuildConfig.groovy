griffon.project.dependency.resolution = {
    // inherit Griffon' default dependencies
    inherits("global") {
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()

        flatDir name: 'joglPluginLib', dirs: 'lib'
    }
    dependencies {
        def joglVersion = 'jsr231-2.0.0'
        compile "com.jogamp:jogl-all:$joglVersion",
                "com.jogamp:gluegen-rt:$joglVersion",
                "com.jogamp:nativewindow.all:$joglVersion",
                "com.jogamp:newt.all:$joglVersion"
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
