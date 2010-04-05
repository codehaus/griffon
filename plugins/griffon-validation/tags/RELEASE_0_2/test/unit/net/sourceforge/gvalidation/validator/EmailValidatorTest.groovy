package net.sourceforge.gvalidation.validator

import net.sourceforge.gvalidation.validator.EmailValidator

/**
 * Created by nick.zhu
 */
class EmailValidatorTest extends GroovyTestCase {

    public void testEmailValidation(){
        EmailValidator email = new EmailValidator(this)

        assertTrue("Should be skipped", (boolean) email.call("blah", this, false))
        assertFalse("Should not be a valid email address", (boolean) email.call("blah", this, true))
        assertTrue("Should be a valid email address", (boolean) email.call("blah@somesite.com", this, true))
    }
    
}
