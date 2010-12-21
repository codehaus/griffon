griffon.project.dependency.resolution = {
    inherits "global"
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()

        flatDir name: 'joglPluginLib', dirs: 'lib'
    }
    dependencies {
        compile 'com.jogamp:jogl.all:249',
                'com.jogamp:nativewindow.all:249',
                'com.jogamp:newt.all:249',
                'com.jogamp:gluegen-rt:233',
                'com.jogamp:jocl:225'
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
