package net.sourceforge.gvalidation.validator

import net.sourceforge.gvalidation.validator.MatchesValidator

/**
 * Created by nick.zhu
 */

class MatchesValidatorTest extends GroovyTestCase {

    public void testMatchesValidation(){
        MatchesValidator matches = new MatchesValidator(this)

        def regex = /[a-zA-Z]+/
        
        assertFalse("Should not be valid", (boolean) matches.call("blahblah", this, null))
        assertFalse("Should not be valid", (boolean) matches.call("789abd", this, regex))
        assertTrue("Should be valid", (boolean) matches.call("somethingValid", this, regex))
        assertTrue("Should ignore null", (boolean) matches.call(null, this, regex))        
    }

}