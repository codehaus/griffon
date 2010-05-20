/*
 * Copyright 2010 the original author or authors.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package net.sourceforge.gvalidation

/**
 * Created by nick.zhu
 */
class ValidationEnhancerTest extends GroovyTestCase {

    public void testModelConstraintInvocation() {
        ModelBean model = new ModelBean()

        ValidationEnhancer validator = new ValidationEnhancer(model)

        boolean result = model.validate()

        assertFalse("Validation result should be false", result)
        assertTrue("Model should have error", model.hasErrors())
        assertTrue("id field should have error", model.errors.hasFieldErrors('id'))

        def fieldError = model.errors.getFieldError('id')

        assertEquals("Error code is not correct", "modelBean.id.nullable.message", fieldError.errorCode)
        assertEquals("Default error code is not correct", "default.nullable.message", fieldError.defaultErrorCode)
        assertEquals("Error arg field is not correct", "id", fieldError.arguments[0])
        assertEquals("Error arg bean is not correct", "ModelBean", fieldError.arguments[1])
        assertEquals("Error arg constraint is not correct", "null", fieldError.arguments[2])
        assertEquals("Error arg constraint config is not correct", false, fieldError.arguments[3])
    }

    public void testErrorCorrection() {
        ModelBean model = new ModelBean()

        ValidationEnhancer validator = new ValidationEnhancer(model)

        boolean result = model.validate()

        assertFalse("Validation result should be false", result)

        model.id = "Soemthing"

        result = model.validate()

        assertTrue("Validation result should not be false", result)
    }

    public void testValidationWithNoConstraint() {
        NoConstraintModelBean model = new NoConstraintModelBean()

        ValidationEnhancer validator = new ValidationEnhancer(model)

        boolean result = model.validate()

        assertTrue("Validation result should be true", result)
    }

    public void testValidationWithInvalidConstraint() {
        InvalidConstraintModelBean model = new InvalidConstraintModelBean()

        ValidationEnhancer validator = new ValidationEnhancer(model)

        try {
            boolean result = model.validate()
            fail("Exception should be thrown with invalid constraint configuration")
        } catch (IllegalStateException ex) {
            // good
        }
    }

    public void testUnknownConstraintIsIgnored() {
        UnknownConstraintModelBean model = new UnknownConstraintModelBean()

        ValidationEnhancer validator = new ValidationEnhancer(model)

        boolean result = model.validate()

        assertTrue("Validation result should be true", result)
    }

    public void testNullAndBlankTolerance(){
        NullToleranceModelBean model = new NullToleranceModelBean()

        ValidationEnhancer.enhance(model)

        boolean result = model.validate()

        model.errors.each{
            println it
        }

        assertTrue("Validation result should be true", result)
    }

}


class ModelBean {
    String id

    static constraints = {
        id(nullable: false)
    }
}

class NoConstraintModelBean {
}

class InvalidConstraintModelBean {
    String id

    static constraints = {
        missing(nullable: false)
    }
}

class UnknownConstraintModelBean {
    String id

    static constraints = {
        id(missing: false)
    }
}

class NullToleranceModelBean {
    String nullable
    String blank = ''

    static constraints = {
        nullable(
                nullable: true,
                blank: true,
                creditCard: true,
                email: true,
                inetAddress: true,
                inList: ["a"],
                matches: /a-zA-Z/,
                maxSize: 10,
                max: 10,
                minSize: 5,
                min: 5,
                notEqual: "a",
                range: 1..10,
                size: 1..10,
                url: true
        )
        blank(
                nullable: false,
                blank: true,
                creditCard: true,
                email: true,
                inetAddress: true,
                inList: ["a"],
                matches: /a-zA-Z/,
                maxSize: 10,
                max: 10,
                minSize: 5,
                min: 5,
                notEqual: "a",
                range: 1..10,
                size: 1..10,
                url: true
        )
    }
}