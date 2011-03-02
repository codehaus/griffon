package net.sourceforge.griffon.airbag

import griffon.util.GriffonExceptionHandler

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class AirbagExceptionHandler extends GriffonExceptionHandler {
    AirBag airBag

    def AirbagExceptionHandler(){
        airBag = new DefaultAirBag()
    }

    @Override
    void uncaughtException(Thread t, Throwable e) {
        super.uncaughtException(t, e)

        airBag.deploy(e)
    }

}
