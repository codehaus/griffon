class FxBuilderGriffonPlugin {
    def version = "0.3"
    def canBeGlobal = false
    def dependsOn = ["lang-bridge": 0.2]

    def author = "Andres Almiray"
    def authorEmail = "aalmiray@user.sourceforge.net"
    def title = "Enables JavaFX on your Griffon application"
    def description = '''\\
Enables JavaFX on your Griffon application. Brings the FxBuilder and dependencies libraries, enables
the usage of the JavaFX Script compiler on your application.
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/FxBuilder+Plugin"
}
