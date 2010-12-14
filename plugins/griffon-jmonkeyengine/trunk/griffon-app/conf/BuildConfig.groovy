griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()

        mavenCentral()
        mavenRepo 'http://nifty-gui.sourceforge.net/nifty-maven-repo'
        flatDir name: 'jmonkeyenginePluginLib', dirs: 'lib'
    }
    dependencies {
        runtime('lessvoid:nifty:1.2') { exclude 'xpp3' }
        runtime 'xpp3:xpp3_min:1.1.4c',
                'de.jarnbjo:j-ogg-all:1.0',
                'com.bulletphysics:jbullet:20101010',
                'cz.advel:stack-alloc:20101010',
                'javax.vecmath:vecmath:1.4.0',
                'com.jcraft:jorbis:0.0.17',
                'com.fluendo:jheora:0.2.2',
                'lessvoid:nifty-default-controls:1.2',
                'lessvoid:nifty-style-black:1.2'
        compile 'com.jme:jmonkeyengine:3.0-alpha'//,
                // 'com.jme:jmonkeyengine-lwjgl-natives:3.0-alpha'
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
