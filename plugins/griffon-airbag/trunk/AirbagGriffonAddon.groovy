import griffon.core.GriffonApplication
import net.sourceforge.griffon.airbag.AirbagExceptionHandler

class AirbagGriffonAddon {
    def addonInit(GriffonApplication app) {
        Thread.setDefaultUncaughtExceptionHandler(new AirbagExceptionHandler())
        System.setProperty("sun.awt.exception.handler", AirbagExceptionHandler.class.getName())
    }
}
