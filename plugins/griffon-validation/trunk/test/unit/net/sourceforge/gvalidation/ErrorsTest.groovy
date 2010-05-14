package net.sourceforge.gvalidation

/**
 * Created by nick.zhu
 */
class ErrorsTest extends GroovyTestCase {

    public void testHasErrors() {
        Errors errors = new Errors()

        errors.reject("errorCode")

        assertTrue "Should have error", errors.hasErrors()

        errors.clear()

        assertFalse "Should not have any error", errors.hasErrors()

        errors.rejectValue("field", "errorCode")

        assertTrue "Should have error", errors.hasErrors()
    }

    public void testReject() {
        Errors errors = new Errors()

        errors.reject("errorCode")

        assertTrue "Should have global error", errors.hasGlobalErrors()
    }

    public void testRejectWithDefaultMessage() {
        Errors errors = new Errors()

        errors.reject("errorCode", "defaultErrorCode")

        assertTrue "Should have global error", errors.hasGlobalErrors()
        errors.each{
            assertEquals "Default error code is not correct", "defaultErrorCode", it.defaultErrorCode
        }
    }

    public void testRejectWithArgs() {
        Errors errors = new Errors()

        errors.reject("errorCode", [10, "arg2"])

        def globalErrors = errors.getGlobalErrors()

        assertEquals "Error code is incorrect", globalErrors.first().errorCode, "errorCode"
        assertEquals "Error args is incorrect", 10, globalErrors.first().arguments[0]
        assertEquals "Error args is incorrect", "arg2", globalErrors.first().arguments[1]
    }

    public void testRejectValue() {
        Errors errors = new Errors()

        errors.rejectValue("field", "errorCode")

        assertTrue "Should have field error", errors.hasFieldErrors()
        assertTrue "Field should have error", errors.hasFieldErrors("field")
    }

    public void testRejectValueWithArgs() {
        Errors errors = new Errors()

        errors.rejectValue("field", "errorCode", [10, "arg2"])

        def fieldError = errors.getFieldError("field")

        assertEquals "Field error field value is incorrect", fieldError.field, "field"
        assertEquals "Error code is incorrect", fieldError.errorCode, "errorCode"
        assertEquals "Field error args is incorrect", fieldError.arguments[0], 10
        assertEquals "Field error args is incorrect", fieldError.arguments[1], "arg2"
    }

    public void testIterator() {
        def pass = false

        Errors errors = new Errors()

        errors.rejectValue("field", "errorCode")

        errors.each {
            if ("errorCode" == it.errorCode)
                pass = true
        }

        assertTrue "Should have passed", pass
    }

    public void testMultipleFieldReject() {
        Errors errors = new Errors()

        errors.rejectValue("field", "errorCode1")
        errors.rejectValue("field", "errorCode2")

        def fieldErrors = errors.getFieldErrors("field")

        assertEquals("Multiple field errors should have been generated", 2, fieldErrors.size())
    }

    public void testMultipleGlobalReject() {
        Errors errors = new Errors()

        errors.reject("errorCode1")
        errors.reject("errorCode2")

        def globalErrors = errors.getGlobalErrors()

        assertEquals("Multiple field errors should have been generated", 2, globalErrors.size())
    }

}
