package net.sourceforge.gvalidation.validator

import net.sourceforge.gvalidation.validator.MaxSizeValidator

/**
 * Created by nick.zhu
 */

class MaxSizeValidatorTest extends GroovyTestCase {

    public void testMaxSizeValidation() {
        MaxSizeValidator maxSize = new MaxSizeValidator(this)

        assertFalse("Should not be valid", (boolean) maxSize.call("blahblah", this, null))
        assertTrue("Should be valid, since null is considered 0 length", (boolean) maxSize.call(null, this, 5))

        assertFalse("Should not be valid", (boolean) maxSize.call("something long", this, 5))
        assertTrue("Should be valid", (boolean) maxSize.call("valid", this, 5))

        assertFalse("Should not be valid", (boolean) maxSize.call(['a', 'b', 'c'], this, 2))
        assertTrue("Should be valid", (boolean) maxSize.call(['a', 'b'], this, 3))
    }

}