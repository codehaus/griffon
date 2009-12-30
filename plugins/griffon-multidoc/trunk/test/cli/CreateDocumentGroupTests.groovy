import griffon.test.AbstractCliTestCase

/**
 * Test case for the "CreateDocumentGroup" Griffon command.
 */
class CreateDocumentGroupTests extends AbstractCliTestCase {
    void testDefault() {
        execute([ "CreateDocumentGroup" ])

        assertEquals 0, waitForProcess()
        verifyHeader()

        // Make sure that the script was found.
        assertFalse "CreateDocumentGroup script not found.", output.contains("Script not found:")
    }
}
