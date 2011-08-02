griffon.project.dependency.resolution = {
    // inherit Griffon' default dependencies
    inherits "global"
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        flatDir name: 'jmsPluginLib', dirs: 'lib'
        mavenCentral()
        mavenRepo 'http://repository.springsource.com/maven/bundles/release'
    }
    dependencies {
        def springVersion = '3.0.5.RELEASE'
        compile 'javax.jms:javax.jms:1.1.0'
        compile("org.springframework:org.springframework.jms:$springVersion") {
            excludes 'com.springsource.org.apache.commons.logging' 
        }
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (@griffon.version@)"
    }
}
