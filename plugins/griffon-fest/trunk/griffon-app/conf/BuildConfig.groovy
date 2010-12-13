griffon.project.dependency.resolution = {
    inherits("global") 
    log "warn"
    repositories {
        griffonPlugins()
        griffonHome()
        griffonCentral()

        mavenRepo 'http://repository.codehaus.org'
        mavenCentral()
    }
    dependencies {
        compile('org.easytesting:fest-swing:1.2.1') {
            excludes 'junit'
            export = false
        }
        compile('org.easytesting:fest-swing-junit-4.5:1.2.1') {
            excludes 'junit'
            export = false
        }
        compile('junit:junit:4.8.2') { export = false }
        test('org.easytesting:fest-swing:1.2.1') { excludes 'junit' }
        test('org.easytesting:fest-swing-junit:1.2.1') { excludes 'junit', 'ant-junit' }
        test('org.easytesting:fest-swing-junit-4.5:1.2.1') { excludes 'junit' }
        test 'junit:junit:4.8.2'
    }
}

griffon {
    doc {
        logo = '<a href="http://griffon.codehaus.org" target="_blank"><img alt="The Griffon Framework" src="../img/griffon.png" border="0"/></a>'
        sponsorLogo = "<br/>"
        footer = "<br/><br/>Made with Griffon (@griffon.version@)"
    }
}
