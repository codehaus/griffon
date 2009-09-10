class TransitionsGriffonPlugin {
    def version = "0.1.1"
    def canBeGlobal = false
    def dependsOn = ["trident-builder": 0.1]
    def jdk = "1.6"

    def author = "Andres Almiray"
    def authorEmail = "aalmiray@users.sourceforge.net"
    def title = "Adds animated transitions capabilities"
    def description = '''\\
Adds animated transitions capabilities
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/Transitions+Plugin"
}
