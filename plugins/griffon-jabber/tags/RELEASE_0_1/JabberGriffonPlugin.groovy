class JabberGriffonPlugin {
    def version = "0.1"
    def canBeGlobal = false
    def dependsOn = [xmlrpc: "0.1"]

    def author = "Andres Almiray"
    def authorEmail = "aalmiray@users.sourceforge.net"
    def title = "Jabber client & libraries"
    def description = '''\\
Jabber client & libraries.
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/Jabber+Plugin"
}
