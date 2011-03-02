import net.sourceforge.griffon.airbag.AirbagExceptionHandler
import javax.swing.SwingUtilities

class AirbagGriffonAddon {
    def events = [
            "ReadyEnd": {app ->
                def exceptionHandler = new AirbagExceptionHandler()

                Thread.setDefaultUncaughtExceptionHandler(exceptionHandler)

                System.setProperty("sun.awt.exception.handler", AirbagExceptionHandler.class.getName())
            },

            "LogC4jonfigStart": {app ->
                System.setProperty("sun.awt.exception.handler", AirbagExceptionHandler.class.getName())
            }
    ]
}
