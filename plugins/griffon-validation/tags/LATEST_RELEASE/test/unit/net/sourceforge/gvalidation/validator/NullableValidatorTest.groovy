package net.sourceforge.gvalidation.validator

import net.sourceforge.gvalidation.validator.NullableValidator

/**
 * Created by nick.zhu
 */
class NullableValidatorTest extends GroovyTestCase {

    public void testNullableCheck(){
        NullableValidator nullable = new NullableValidator(this)

        assertTrue("Should allow null", (Boolean) nullable.call(null, this, true))
        assertFalse("Should not allow null", (Boolean) nullable.call(null, this, false))

        assertTrue("Should allow not null", (Boolean) nullable.call(" ", this, true))
        assertTrue("Should allow not null", (Boolean) nullable.call(" ", this, false))
    }

}
