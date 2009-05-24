//
// This script is executed by Griffon after plugin was installed to project.
// This script is a Gant script so you can use all special variables provided
// by Gant (such as 'baseDir' which points on project base dir). You can
// use 'ant' to access a global instance of AntBuilder
//
// For example you can create directory under project tree:
//
//    ant.mkdir(dir:"${basedir}/griffon-app/jobs")
//

def initStr = new File("${basedir}/griffon-app/lifecycle/Initialize.groovy").text
//println initStr
def splashIsSet = false
if (initStr =~ "SplashScreen") {
  splashIsSet = true
}


if (!splashIsSet) {
    println 'Adding SplashScreen to Initalize.groovy'
    new File("${basedir}/griffon-app/lifecycle/Initialize.groovy").append("""

def splashScreen = SplashScreen.getInstance()

// Setting a splash image
//URL url = this.class.getResource("mySplash.jpg")
//splashScreen.setImage(url)
//
// Setting Status Text
// SplashScreen.getInstance().showStatus("Initializing the Controller")
splashScreen.splash()
splashScreen.waitForSplash()

""")

    println 'Adding SplashScreen to Ready.groovy'
    new File("${basedir}/griffon-app/lifecycle/Ready.groovy").append("""
	
    SplashScreen.getInstance().dispose()

""")


}





