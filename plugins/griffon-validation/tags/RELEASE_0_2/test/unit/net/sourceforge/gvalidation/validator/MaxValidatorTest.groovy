package net.sourceforge.gvalidation.validator

import net.sourceforge.gvalidation.validator.MaxValidator

/**
 * Created by nick.zhu
 */

class MaxValidatorTest extends GroovyTestCase {

    public void testMaxlValidation() {
        MaxValidator max = new MaxValidator(this)

        checkNulls(max)

        validateWithNumbers(max)

        def now = new Date()

        validateWithDates(max, now)

        tryWithDifferentTypes(max, now)
    }

    private def tryWithDifferentTypes(MaxValidator max, Date now) {
        assertFalse("Should not be valid", (boolean) max.call("today", this, now))
    }

    private def validateWithDates(MaxValidator max, Date now) {
        assertFalse("Should not be valid", (boolean) max.call(now + 1, this, now))
        assertTrue("Should be valid", (boolean) max.call(now - 1, this, now))
    }

    private def validateWithNumbers(MaxValidator max) {
        assertFalse("Should not be valid", (boolean) max.call(90, this, 80))
        assertTrue("Should be valid", (boolean) max.call(78, this, 80))
    }

    private def checkNulls(MaxValidator max) {
        assertFalse("Should not be valid", (boolean) max.call("blahblah", this, null))
        assertTrue("Should be valid", (boolean) max.call(null, this, 80))
    }

}