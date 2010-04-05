package net.sourceforge.gvalidation.validator

import net.sourceforge.gvalidation.validator.UrlValidator

/**
 * Created by nick.zhu
 */
class UrlValidatorTest extends GroovyTestCase {

    public void testUrlValidation(){
        UrlValidator url = new UrlValidator(this)

        assertTrue("Should be valid", url.call('http://gvalidation.sf.net', this, true))
        assertTrue("Should be valid", url.call('http://gvalidation.sf.net', this, false))
        assertFalse("Should not be valid", url.call('gvalidation.sf.net', this, true))
    }

}
