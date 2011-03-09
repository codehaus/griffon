griffon.project.dependency.resolution = {
    inherits "global"
    log "warn" 
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        flatDir name: 'feedsPluginLib', dirs: 'lib'
        mavenRepo 'http://repository.sonatype.org/content/groups/public'
    }
    dependencies {
        build   'com.zeusboxstudio:zeusboxstudio-feed-icons:1.0'
        compile 'com.zeusboxstudio:zeusboxstudio-feed-icons:1.0',
                'rome:rome:1.0'
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
