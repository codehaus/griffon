griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()
        flatDir name: 'swingxtrasBuilderPluginLib', dirs: 'lib'
        mavenCentral()
        mavenRepo 'http://repository.codehaus.org'
        mavenRepo 'http://repository.sonatype.org/content/groups/public'
    }
    dependencies {
        compile('org.codehaus.griffon:swingxtrasbuilder:0.3') {
            excludes 'groovy-all'
        }
        compile 'org.swinglabs:xswingx:0.2',
                'net.java.balloontip:balloontip:20090102',
                'com.l2fprod.common:l2fprod-common-all:6.9.1'
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
