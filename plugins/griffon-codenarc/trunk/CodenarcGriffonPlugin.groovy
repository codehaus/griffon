class CodenarcGriffonPlugin {
    def version = 0.2
    def canBeGlobal = false
    def dependsOn = [:]

    def author = "Andres Almiray"
    def authorEmail = "aalmiray@users.sourceforge.net"
    def title = "CodeNarc plugin"
    def description = '''\\
Runs CodeNarc static analysis rules for Groovy source.
Based on the original Grails codenarc plugin by Burt Beckwith.
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/Codenarc+Plugin"
}
