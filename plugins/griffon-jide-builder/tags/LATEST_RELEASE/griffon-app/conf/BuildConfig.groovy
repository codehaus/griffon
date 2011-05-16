griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        flatDir name: 'jideBuilderPluginLib', dirs: 'lib'
        mavenCentral()
        mavenRepo 'http://repository.codehaus.org'
        mavenRepo 'http://repository.sonatype.org/content/groups/public'
    }
    dependencies {
        compile('org.codehaus.griffon:jidebuilder:4.0') {
            excludes 'groovy-all', 'svg-salamander'
        }
        compile 'com.kitfox.svg:svg-salamander:1.0'
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
