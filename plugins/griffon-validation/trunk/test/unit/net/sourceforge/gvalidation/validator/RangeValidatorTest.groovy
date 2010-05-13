package net.sourceforge.gvalidation.validator

import net.sourceforge.gvalidation.validator.RangeValidator

/**
 * Created by nick.zhu
 */
class RangeValidatorTest extends GroovyTestCase {

    public void testRangeValidation() {
        RangeValidator range = new RangeValidator(this)

        assertTrue("Should be valid", range.call(5, this, 0..10))
        assertFalse("Should not be valid", range.call(5, this, 0..<5))

        def now = new Date()
        assertTrue("Should be valid", range.call(now + 5, this, now..now + 10))
        assertFalse("Should not be valid", range.call(now + 5, this, now..now + 3))

        assertFalse("Should not be valid", range.call(now, this, now))
    }

    // tracker bug #2983281
    public void testRangeValidationWithInt() {
        RangeValidator range = new RangeValidator(this)

        assertTrue("Should be valid", range.call(5, this, 1..65535))
        assertFalse("Should not be valid", range.call(-5, this, 1..65535))  
    }

}
