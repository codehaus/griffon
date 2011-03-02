package net.sourceforge.griffon.airbag

import org.apache.log4j.Logger
import org.apache.commons.lang.StringUtils
import griffon.util.ApplicationHolder

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class DefaultAirBag implements AirBag {
    private static Logger log = Logger.getLogger(DefaultAirBag.class)
    private static final String EXCEPTION_CAUGHT_EVENT_NAME = 'uncaughtException'

    private app

    def DefaultAirBag(){
        this.app = ApplicationHolder.application
    }

    def DefaultAirBag(app){
        this.app = app
    }

    @Override def deploy(Throwable t) {
        log.warn("Airbag deployed for error[${t}] -> [${t.message}]", t)

        app.event(EXCEPTION_CAUGHT_EVENT_NAME, [t])

        def exceptionName = t.class.getSimpleName()

        app.event("uncaught${exceptionName}", [t])
    }
}
