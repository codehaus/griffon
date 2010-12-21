griffon.project.dependency.resolution = {
    inherits("global")
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()

        mavenCentral()
        mavenRepo 'http://repository.sonatype.org/content/groups/public'
        flatDir name: 'lwjglPluginLib', dirs: 'lib'
    }
    dependencies {
        compile 'net.alchim31.3rd.org.7-zip:lzma:4.65',
                'org.lwjgl:lwjgl:2.6',
                'org.lwjgl:lwjgl_util:2.6',
                'org.lwjgl:lwjgl_util_applet:2.6',
                'net.java.games:jinput:1.6',
                'com.apple:AppleJavaExtensions:1.4'
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
