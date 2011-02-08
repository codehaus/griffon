griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
        mavenRepo 'http://repository.springsource.com/maven/bundles/release'
    }
    dependencies {
        compile 'com.google.code.jcouchdb:jcouchdb:1.0.1-1'
        compile("org.springframework:org.springframework.core:3.0.5.RELEASE") { transitive = false }
        compile('net.sf.ezmorph:ezmorph:1.0.6') { excludes 'junit' }
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
