package net.sourceforge.gvalidation.validator

import net.sourceforge.gvalidation.validator.SizeValidator

/**
 * Created by nick.zhu
 */
class SizeValidatorTest extends GroovyTestCase {

    public void testSizeValidation() {
        SizeValidator size = new SizeValidator(this)

        assertTrue("Should be valid", size.call("hello", this, 3..10))
        assertTrue("Should be valid", size.call(['a', 'b'], this, 1..3))

        assertFalse("Should not be valid", size.call("hello", this, 3..4))
        assertFalse("Should not be valid", size.call(['a', 'b', 'c', 'd'], this, 1..3))

        assertFalse("Should not be valid", size.call(['a', 'b', 'c', 'd'], this, 3))
    }

}
