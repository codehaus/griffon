griffon.project.dependency.resolution = {
    inherits "global"
    log "warn" 
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
        mavenRepo 'http://repository.codehaus.org'
    }
    dependencies {
        def scalaVersion = '2.8.1'
        build "org.scala-lang:scala-compiler:$scalaVersion",
              "org.scala-lang:scala-library:$scalaVersion"
        compile "org.scala-lang:scala-compiler:$scalaVersion",
                "org.scala-lang:scala-library:$scalaVersion",
                "org.scala-lang:scala-swing:$scalaVersion"
        compile('org.codehaus.groovy.modules:groovytransforms:0.2') { transitive = false }
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
