griffon.project.dependency.resolution = {
    inherits("global") 
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()

        mavenCentral()
        flatDir name: 'ratpackPluginLib', dirs: 'lib'
    }
    dependencies {
        runtime 'com.bleedingwolf.ratpack:ratpack:0.1',
                'javax.servlet:servlet-api:2.5',
                'org.json:json:20090211'
        runtime('org.mortbay.jetty:jetty:6.1.26') { excludes 'slf4j-api', 'servlet-api' }
        runtime('org.mortbay.jetty:jetty-util:6.1.26') { excludes 'slf4j-api', 'servlet-api' }
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
