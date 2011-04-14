griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
        flatDir name: 'syntaxtextPluginLib', dirs: 'lib'
    }
    dependencies {
        compile 'com.fifesoft:rsyntaxtextarea:1.5.1',
                'com.fifesoft:autocomplete:1.5.1',
                'com.fifesoft:spellchecker:1.5.0'
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
