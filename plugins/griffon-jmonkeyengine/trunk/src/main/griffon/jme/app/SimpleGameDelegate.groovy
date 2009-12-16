package griffon.jme.app

class SimpleGameDelegate {
    private final SimpleGameGriffonApplication app

    SimpleGameDelegate(SimpleGameGriffonApplication app) {
        this.app = app
    } 

    void simpleInitGame() {}
    void simpleUpdate() {}

    def methodMissing(String methodName, args) {
        app.invokeMethod(methodName, args)
    }

    def propertyMissing(String propertyName) {
        app.getProperty(propertyName)
    }

    void propertyMissing(String propertyName, value) {
        app.setProperty(propertyName, value)
    }
}
