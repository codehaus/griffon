package net.sourceforge.gvalidation.validator

/**
 * Created by nick.zhu
 */
class InetAddressValidatorTest extends GroovyTestCase {

    public void testInetAddressValidation() {
        InetAddressValidator inetAddress = new InetAddressValidator(this)

        assertTrue("Should be a valid address", inetAddress.call("codehaus.org", this, true))
        assertTrue("Should be a valid address", inetAddress.call("127.0.0.1", this, true))
        assertFalse("Should not be a valid address", inetAddress.call("codehaus", this, true))
    }

}
