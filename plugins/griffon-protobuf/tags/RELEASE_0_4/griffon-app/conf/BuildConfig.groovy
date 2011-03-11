griffon.project.dependency.resolution = {
    inherits("global") 
    log "warn" 
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
        flatDir name: 'protobufPluginLib', dirs: 'lib'
    }
    dependencies {
        build   'com.google.protobuf:protobuf-java:2.4.0a'
        compile 'com.google.protobuf:protobuf-java:2.4.0a'
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
