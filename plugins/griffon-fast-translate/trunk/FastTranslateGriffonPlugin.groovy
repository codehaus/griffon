class FastTranslateGriffonPlugin {
    def version = 0.2
    def griffonVersion = "0.3 > *" 
    def canBeGlobal = false
    def dependsOn = [gparallelizer:"0.3"]
    def license = "Apache License 2.0"
    
    def author = "Hamlet D'Arcy"
    def authorEmail = "hamletdrc@gmail.com"
    def title = "Provides fast translation services for human language. Backed by Google Translate."
    def description = '''
The fast-translate plugin provides a fast human language translation service. The service make parallel calls to the Internet based Google Translate service, so an Internet connection is required. 
'''

    def documentation = "http://griffon.codehaus.org/FastTranslate+Plugin"
}
