clover {
    debug = false;
    on = false;
    core.version = 'com.cenqua.clover:clover:3.0.2';
    license.path = "clover.license"
}

griffon.project.dependency.resolution = {
    // inherit Griffon' default dependencies
    inherits "global"
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenRepo "https://maven.atlassian.com/content/groups/public/"
        mavenRepo "https://maven.atlassian.com/private-snapshot/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.
         build(clover.core.version, changing:false)
         compile(clover.core.version, changing:false)
         runtime(clover.core.version, changing:false)
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (@griffon.version@)"
    }
}
