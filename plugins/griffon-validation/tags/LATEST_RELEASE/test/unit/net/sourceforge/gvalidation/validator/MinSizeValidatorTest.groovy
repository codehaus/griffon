package net.sourceforge.gvalidation.validator

import net.sourceforge.gvalidation.validator.MinSizeValidator

/**
 * Created by nick.zhu
 */

class MinSizeValidatorTest extends GroovyTestCase {

    public void testMinSizeValidation() {
        MinSizeValidator minSize = new MinSizeValidator(this)

        assertTrue("Should be valid", (boolean) minSize.call("blahblah", this, null))
        assertTrue("Should ignore null", (boolean) minSize.call(null, this, 5))

        assertTrue("Should be valid", (boolean) minSize.call("something long", this, 5))
        assertFalse("Should not be valid", (boolean) minSize.call("foo", this, 5))

        assertTrue("Should be valid", (boolean) minSize.call(['a', 'b', 'c'], this, 2))
        assertFalse("Should not be valid", (boolean) minSize.call(['a', 'b'], this, 3))
    }

}