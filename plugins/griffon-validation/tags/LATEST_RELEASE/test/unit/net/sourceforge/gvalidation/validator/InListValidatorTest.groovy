package net.sourceforge.gvalidation.validator

import net.sourceforge.gvalidation.validator.InListValidator

/**
 * Created by nick.zhu
 */

class InListValidatorTest extends GroovyTestCase {

    public void testInListlValidation(){
        InListValidator inList = new InListValidator(this)

        def names = ['Joe', 'John', 'Bob']

        assertFalse("Should not be valid", (boolean) inList.call("blahblah", this, null))
        assertFalse("Should not be in the list", (boolean) inList.call("blahblah", this, names))
        assertTrue("Should ignore null", (boolean) inList.call(null, this, names))
        assertTrue("Should be in the list", (boolean) inList.call("Bob", this, names))
    }

}