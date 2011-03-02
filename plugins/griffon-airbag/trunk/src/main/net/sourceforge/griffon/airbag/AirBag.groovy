package net.sourceforge.griffon.airbag

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
interface AirBag {
    def deploy(Throwable t);
}
