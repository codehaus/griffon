class ClojureGriffonPlugin {
    def version = "0.3-SNAPSHOT"
    def canBeGlobal = false
    def dependsOn = ["lang-bridge": 0.2]

    def author = "Andres Almiray"
    def authorEmail = "aalmiray@users.sourceforge.net"
    def title = "Enables Clojure"
    def description = '''\\
Enables the usage of Clojure on your Griffon application.
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/Clojure+Plugin"
}
