griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
        flatDir name: 'codeCoveragePluginLib', dirs: 'lib'
    }
    dependencies {
        build 'net.sourceforge.cobertura:cobertura:1.9.8',
              'oro:oro:2.0.8',
              'asm:asm:3.0',
              'asm:asm-tree:3.0'
        test 'net.sourceforge.cobertura:cobertura:1.9.8',
             'oro:oro:2.0.8',
             'asm:asm:3.0',
             'asm:asm-tree:3.0'
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (@griffon.version@)"
    }
}

