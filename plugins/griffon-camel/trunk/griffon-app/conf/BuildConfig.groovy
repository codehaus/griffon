griffon.project.dependency.resolution = {
    inherits "global"
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
    }
    dependencies {
        String camelVersion = '2.5.0'
        compile("org.apache.camel:camel-core:$camelVersion",
                "org.apache.camel:camel-core-xml:$camelVersion",
                "org.apache.camel:camel-spring:$camelVersion",
                "org.apache.camel:camel-groovy:$camelVersion",
                "org.apache.camel:camel-stream:$camelVersion",
                'org.fusesource.commonman:commons-management:1.0') {
            transitive = false
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

griffon.jars.destDir='target/addon'
