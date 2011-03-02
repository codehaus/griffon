package net.sourceforge.griffon.airbag

import griffon.spock.UnitSpec
import spock.lang.Specification

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class DefaultAirBagSpec extends UnitSpec {

    def 'Air bag should trigger app generic exception event'() {
        given:

        def eventTriggered = false
        def expectedError = new IllegalAccessError()
        def receivedError
        def expectedEventName = DefaultAirBag.EXCEPTION_CAUGHT_EVENT_NAME
        def receivedEventName

        when:

        DefaultAirBag airBag = new DefaultAirBag([event: {
            name, arg ->
            if (name == expectedEventName) {
                receivedEventName = name
                eventTriggered = true
                receivedError = arg[0]
            }
        }])

        airBag.deploy expectedError

        then:

        eventTriggered == true
        expectedError == receivedError
        expectedEventName == receivedEventName
    }

    def 'Air bag should trigger exception specific event'() {
        given:

        def eventTriggered = false
        def expectedError = new IllegalAccessError()
        def receivedError
        def expectedEventName = 'uncaughtIllegalAccessError'
        def receivedEventName

        when:

        DefaultAirBag airBag = new DefaultAirBag([event: {
            name, arg ->
            if (name == expectedEventName) {
                receivedEventName = name
                eventTriggered = true
                receivedError = arg[0]
            }
        }])

        airBag.deploy expectedError

        then:

        eventTriggered == true
        expectedError == receivedError
        expectedEventName == receivedEventName
    }

}
