griffon.project.dependency.resolution = {
    inherits("global")
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()

        mavenRepo 'http://repository.springsource.com/maven/bundles/release'
    }
    dependencies {
        def springVersion = '3.0.3.RELEASE'
        compile("org.springframework:org.springframework.core:$springVersion") { transitive = false }
        compile("org.springframework:org.springframework.beans:$springVersion") { transitive = false }
        compile("org.springframework:org.springframework.context:$springVersion") { transitive = false }
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
