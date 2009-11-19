import griffon.test.AbstractCliTestCase

/**
 * Test case for the "protoc" Griffon command.
 */
class ProtocTests extends AbstractCliTestCase {
    void testDefault() {
        execute([ "protoc" ])

        assertEquals 0, waitForProcess()
        verifyHeader()

        // Make sure that the script was found.
        assertFalse "Protoc script not found.", output.contains("Script not found:")
    }
}
