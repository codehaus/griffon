class MongoDBGriffonPlugin {
    def version = '0.1.1'
    def dependsOn = [guice:'0.1.1']
    def griffonVersion = '0.3 > *'

    // TODO Fill in these fields
    def author = "James Williams"
    def authorEmail = "james.l.williams@gmail.com"
    def title = "MongoDB plugin"
    def description = '''\\
Adds MongoDB functionality to Griffon.
'''

    // URL to the plugin's documentation
    def documentation = "http://griffon.codehaus.org/MongoDB+Plugin"
}
