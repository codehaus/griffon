package net.sourceforge.gvalidation.validator

import net.sourceforge.gvalidation.validator.MinValidator

/**
 * Created by nick.zhu
 */

class MinValidatorTest extends GroovyTestCase {

    public void testMinValidation() {
        MinValidator min = new MinValidator(this)

        checkNulls(min)

        validateWithNumbers(min)

        def now = new Date()

        validateWithDates(min, now)

        tryWithDifferentTypes(min, now)
    }

    private def tryWithDifferentTypes(min, Date now) {
        assertFalse("Should not be valid", (boolean) min.call("today", this, now))
    }

    private def validateWithDates(min, Date now) {
        assertFalse("Should not be valid", (boolean) min.call(now - 1, this, now))
        assertTrue("Should be valid", (boolean) min.call(now + 1, this, now))
    }

    private def validateWithNumbers(min) {
        assertFalse("Should not be valid", (boolean) min.call(70, this, 80))
        assertTrue("Should be valid", (boolean) min.call(98, this, 80))
    }

    private def checkNulls(min) {
        assertTrue("Should not be valid", (boolean) min.call("blahblah", this, null))
        assertTrue("Should ignore null", (boolean) min.call(null, this, 80))
    }

    public void testBlanks() {
        MinValidator min = new MinValidator(this)
        
        assertTrue("Should be valid", (boolean) min.call("", this, 1))
    }

    // bug #2984137
    public void testLessThanMin() {
        MinValidator min = new MinValidator(this)

        assertFalse("Should not be valid", (boolean) min.call(0, this, 1))
    }

}