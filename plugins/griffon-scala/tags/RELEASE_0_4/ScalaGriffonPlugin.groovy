class ScalaGriffonPlugin {
    def version = "0.4"
    def canBeGlobal = true
    def dependsOn = ["lang-bridge": 0.2]

    def author = "Andres Almiray"
    def authorEmail = "aalmiray@users.sourceforge.net"
    def title = "Brings the Scala language compiler and libraries"
    def description = '''\\
This plugin enables the usage of Scala classes in your Griffon application.
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/Scala+Plugin"
}
