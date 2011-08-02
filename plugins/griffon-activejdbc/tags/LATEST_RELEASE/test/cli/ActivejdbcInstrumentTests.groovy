/**
 * Test case for the "activejdbc-instrument" Griffon command.
 */
class ActivejdbcInstrumentTests extends AbstractCliTestCase {
    void testDefault() {
        execute([ "activejdbc-instrument" ])

        assertEquals 0, waitForProcess()
        verifyHeader()

        // Make sure that the script was found.
        assertFalse "ActivejdbcInstrument script not found.", output.contains("Script not found:")
    }
}
