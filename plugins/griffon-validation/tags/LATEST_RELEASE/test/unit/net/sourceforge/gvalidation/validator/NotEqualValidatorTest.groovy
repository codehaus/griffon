package net.sourceforge.gvalidation.validator

import net.sourceforge.gvalidation.validator.NotEqualValidator

/**
 * Created by nick.zhu
 */
class NotEqualValidatorTest extends GroovyTestCase {

    public void testNotEqual(){
        NotEqualValidator notEqual = new NotEqualValidator(this)

        assertTrue("Should be valid", notEqual.call("bob", this, "alice"))
        assertFalse("Should be valid", notEqual.call("bob", this, "bob"))
        assertFalse("Should be valid", notEqual.call(10, this, 10))
        assertTrue("Should be valid", notEqual.call(9, this, 10))
        assertTrue("Should be valid", notEqual.call(9, this, "bob"))
    }

}
