griffon.project.dependency.resolution = {
    inherits "global"
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        mavenCentral()
        mavenRepo 'http://repository.springsource.com/maven/bundles/release'
    }
    dependencies {
        def clojureVersion = '1.2.0'
        build "org.clojure:clojure:$clojureVersion",
              "org.clojure:clojure-contrib:$clojureVersion",
              "jline:jline:0.9.94",
              "org.fusesource.jansi:jansi:1.4"
        compile "org.clojure:clojure:$clojureVersion",
                "org.clojure:clojure-contrib:$clojureVersion"

        def springVersion = '3.0.5.RELEASE'
        compile("org.springframework:org.springframework.core:$springVersion") {
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
