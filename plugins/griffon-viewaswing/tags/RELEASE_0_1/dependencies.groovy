griffon.project.dependency.resolution = {
    // inherit Griffon' default dependencies
    inherits("global") {
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
        mavenRepo "http://download.java.net/maven/2/"
		mavenRepo "http://www.viewaframework.org/m2-repo"
    }
    dependencies {
      /* This dependency only has swingx and jxlayer as transition modules */
		runtime 'org.viewaframework:viewaframework-swing:1.0.7'
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (0.9.3)"
		authors ="Mario Garcia"
		license = "Apache 2"
		version= "0.1"
    }
}

griffon.jars.destDir='target/addon'

//griffon.jars.jarName='ViewaswingGriffonAddon.jar'
