package net.sourceforge.gvalidation.validator

import net.sourceforge.gvalidation.validator.BlankValidator

/**
 * Created by nick.zhu
 */
class BlankValidatorTest extends GroovyTestCase {

    public void testBlankValidation(){
        BlankValidator blank = new BlankValidator(this)

        assertTrue("Should allow blank", (boolean) blank.call("", this, true))
        assertTrue("Should allow null", (boolean) blank.call(null, this, true))
        assertFalse("Should not allow null", (boolean) blank.call(null, this, false))
        assertFalse("Should not allow blank", (boolean) blank.call("", this, false))
        assertFalse("Should not allow blank", (boolean) blank.call(" ", this, false))
        assertTrue("Should be successful", (boolean) blank.call(" something ", this, false))
    }

}
