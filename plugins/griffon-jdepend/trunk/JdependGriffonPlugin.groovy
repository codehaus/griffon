class JdependGriffonPlugin {
    def version = "0.2-SNAPSHOT"
    def canBeGlobal = true
    def dependsOn = [:]

    def author = "Andres Almiray"
    def authorEmail = "aalmiray@users.sourceforge.net"
    def title = "Runs JDepend metrics on your Griffon application"
    def description = '''
JDepend traverses Java class file directories and generates design quality metrics for each Java package.
JDepend allows you to automatically measure the quality of a design in terms of its extensibility, reusability, 
and maintainability to manage package dependencies effectively.
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/Jdepend+Plugin"
}
