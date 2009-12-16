package griffon.jme.app

import com.jme.app.SimpleGame
// import griffon.util.BaseGriffonApplication
import griffon.util.GriffonApplicationHelper
import griffon.util.IGriffonApplication
import griffon.util.GriffonExceptionHandler
import griffon.util.EventRouter

class SimpleGameGriffonApplication extends SimpleGame implements IGriffonApplication {
    // @Delegate private final BaseGriffonApplication _base

    List appFrames  = []
    SimpleGameDelegate gameDelegate

    SimpleGameGriffonApplication() {
       super()
       // _base = new BaseGriffonApplication(this)
    }

    void bootstrap() {
        applicationProperties = new Properties()
        applicationProperties.load(getClass().getResourceAsStream('/application.properties'))
        GriffonApplicationHelper.prepare(this)
        gameDelegate = resolveGameDelegate()
        event("BootstrapEnd",[this])
    }

    void realize() {
        GriffonApplicationHelper.startup(this)
    }

    void show() {
        GriffonApplicationHelper.callReady(this)
        start()
    }

/*
    void shutdown() {
        stop()
        _base.shutdown()
        System.exit(0)
    }
*/

    Object createApplicationContainer() {
        def appContainer = GriffonApplicationHelper.createJFrameApplication(this)
        try {
            appFrames += appContainer
        } catch (Throwable ignored) {
            // if it doesn't have a window closing event, ignore it
        }
        return appContainer
    }

    private SimpleGameDelegate resolveGameDelegate() {
        String delegateClassName = config?.jme?.simpleGameDelegate ?: 'griffon.jme.app.SimpleGameDelegate'
        try {
            Class delegateClass = getClass().classLoader.loadClass(delegateClassName)
            return delegateClass.getDeclaredConstructor([SimpleGameGriffonApplication] as Class[]).newInstance([this] as Object[])
        } catch(Exception e) {
            return new SimpleGameDelegate(this)
        }
    }

    protected void simpleInitGame() {
        event("InitGameStart", [])
        gameDelegate.simpleInitGame() 
        event("InitGameEnd", [])
    }

    protected void simpleUpdate() {
        gameDelegate.simpleUpdate() 
    }

    static void main(String[] args) {
        GriffonExceptionHandler.registerExceptionHandler()
        SimpleGameGriffonApplication sgga = new SimpleGameGriffonApplication()
        sgga.bootstrap()
        sgga.realize()
        sgga.show()
    }

    // ==================================================

    Map<String, ?> addons = [:]
    Map<String, String> addonPrefixes = [:]

    Map<String, Map<String, String>> mvcGroups = [:]
    Map models      = [:]
    Map views       = [:]
    Map controllers = [:]
    Map builders    = [:]
    Map groups      = [:]

    Binding bindings = new Binding()
    Properties applicationProperties
    ConfigObject config
    ConfigObject builderConfig
    Object eventsConfig

    private final EventRouter eventRouter = new EventRouter()

    // define getter/setter otherwise it will be treated as a read-only property
    // because only the getter was defined in IGriffonApplication
    Properties getApplicationProperties() {
        return applicationProperties
    }
    void setApplicationProperties(Properties applicationProperties) {
        this.applicationProperties = applicationProperties
    }

    Class getConfigClass() {
        return getClass().classLoader.loadClass("Application")
    }

    Class getBuilderClass() {
        return getClass().classLoader.loadClass("Builder")
    }

    Class getEventsClass() {
        try{
           return getClass().classLoader.loadClass("Events")
        } catch( ignored ) {
           // ignore - no global event handler will be used
        }
        return null
    }

    void initialize() {
        GriffonApplicationHelper.runScriptInsideEDT("Initialize", this)
    }

    void ready() {
        event("ReadyStart",[this])
        GriffonApplicationHelper.runScriptInsideEDT("Ready", this)
        event("ReadyEnd",[this])
    }

    void shutdown() {
        event("ShutdownStart",[this])
        stop()
        List mvcNames = []
        mvcNames.addAll(groups.keySet())
        mvcNames.each {
            GriffonApplicationHelper.destroyMVCGroup(this, it)
        }
        GriffonApplicationHelper.runScriptInsideEDT("Shutdown", this)
        System.exit()
    }

    void startup() {
        event("StartupStart",[this])
        GriffonApplicationHelper.runScriptInsideEDT("Startup", this)
        event("StartupEnd",[this])
    }

    void event( String eventName, List params = [] ) {
        eventRouter.publish(eventName, params)
    }

    void addApplicationEventListener( listener ) {
       eventRouter.addEventListener(listener)
    }

    void removeApplicationEventListener( listener ) {
       eventRouter.removeEventListener(listener)
    }

    void addApplicationEventListener( String eventName, Closure listener ) {
       eventRouter.addEventListener(eventName,listener)
    }

    void removeApplicationEventListener( String eventName, Closure listener ) {
       eventRouter.removeEventListener(eventName,listener)
    }

    void addMvcGroup(String mvcType, Map<String, String> mvcPortions) {
       mvcGroups[mvcType] = mvcPortions
    }
}
