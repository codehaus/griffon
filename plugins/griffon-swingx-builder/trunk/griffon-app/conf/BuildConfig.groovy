griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
        mavenRepo ' https://repository.jboss.org/nexus/content/groups/public-jboss'
        flatDir name: 'swingxBuilderPluginLib', dirs: 'lib'
    }
    dependencies {
        compile 'org.codehaus.griffon:swingxbuilder:0.1.6',
                'org.swinglabs:swingx:0.9.3',
                'net.java.dev.timingframework:timingframework:1.0',
                'batik:batik-awt-util:1.7'
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
