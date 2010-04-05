package net.sourceforge.gvalidation.validator

import net.sourceforge.gvalidation.validator.CreditCardValidator

/**
 * Created by nick.zhu
 */

class CreditCardlValidatorTest extends GroovyTestCase {

    public void testCreditCardValidation() {
        CreditCardValidator creditCard = new CreditCardValidator(this)

        assertTrue("Should be skipped", (boolean) creditCard.call("blahblah", this, false))
        assertFalse("Should not be a valid credit card number", (boolean) creditCard.call("blahblah", this, true))
        assertTrue("Should be a valid credit card number", (boolean) creditCard.call("378282246310005", this, true))
    }

}