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

import net.sourceforge.gvalidation.models.ModelBean
import net.sourceforge.gvalidation.models.UnknownConstraintModelBean
import net.sourceforge.gvalidation.models.NullToleranceModelBean
import net.sourceforge.gvalidation.models.NoConstraintModelBean
import net.sourceforge.gvalidation.models.InvalidConstraintModelBean
import net.sourceforge.gvalidation.models.CustomConstraintModelBean
import net.sourceforge.gvalidation.models.BindableModelBean
import java.beans.PropertyChangeListener

/**
 * Created by nick.zhu
 */
class ValidationEnhancerTest extends GroovyTestCase {

    public void testErrorPropertyChange(){
        BindableModelBean model = new BindableModelBean()

        ValidationEnhancer.enhance(model)

        def firedEvent = false
        model.addPropertyChangeListener({e->
            assertEquals(e.propertyName, "errors"); firedEvent = true
        } as PropertyChangeListener)

        model.errors.reject('testError')

        assertTrue "Property change event should have been fired", firedEvent
    }

    public void testErrorGetterSetterInjection(){
        BindableModelBean model = new BindableModelBean()

        ValidationEnhancer.enhance(model)

        def methods = model.metaClass.methods

        assertNotNull("Dynamic setter is not generated",methods.find{it.name == "setErrors"})

        def firedEvent = false
        model.addPropertyChangeListener({e->
            assertEquals(e.propertyName, "errors"); firedEvent = true
        } as PropertyChangeListener)

        model.errors = new Errors()

        assertTrue "Property change event should have been fired", firedEvent
    }

    public void testModelEnhancementReturn() {
        ModelBean model = new ModelBean()

        def enhancer = ValidationEnhancer.enhance(model)

        assertTrue "Enhancer instance returned in incorrect", enhancer instanceof ValidationEnhancer
    }

    public void testModelConstraintInvocation() {
        ModelBean model = new ModelBean()

        ValidationEnhancer.enhance(model)

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

        ValidationEnhancer.enhance(model)

        boolean result = model.validate()

        assertFalse("Validation result should be false", result)

        model.id = "Soemthing"

        result = model.validate()

        assertTrue("Validation result should not be false", result)
    }

    public void testValidationWithNoConstraint() {
        NoConstraintModelBean model = new NoConstraintModelBean()

        ValidationEnhancer.enhance(model)

        boolean result = model.validate()

        assertTrue("Validation result should be true", result)
    }

    public void testValidationWithInvalidConstraint() {
        InvalidConstraintModelBean model = new InvalidConstraintModelBean()

        ValidationEnhancer.enhance(model)

        try {
            boolean result = model.validate()
            fail("Exception should be thrown with invalid constraint configuration")
        } catch (IllegalStateException ex) {
            // good
        }
    }

    public void testUnknownConstraintIsIgnored() {
        UnknownConstraintModelBean model = new UnknownConstraintModelBean()

        ValidationEnhancer.enhance(model)

        boolean result = model.validate()

        assertTrue("Validation result should be true", result)
    }

    public void testNullAndBlankTolerance() {
        NullToleranceModelBean model = new NullToleranceModelBean()

        ValidationEnhancer.enhance(model)

        boolean result = model.validate()

        model.errors.each {
            println it
        }

        assertTrue("Validation result should be true", result)
    }

    public void testCustomValidator() {
        ConstraintRepository.instance.register('magic',
                [validate: {property, bean, parameter -> false}])

        def model = new CustomConstraintModelBean()

        ValidationEnhancer.enhance(model)

        boolean result = model.validate()

        assertFalse "Custom validation should have failed", result

        def fieldError = model.errors.getFieldError('number')

        assertEquals("Error code is not correct", "customConstraintModelBean.number.magic.message", fieldError.errorCode)
        assertEquals("Default error code is not correct", "default.magic.message", fieldError.defaultErrorCode)
    }

    public void testFieldLevelValidation() {
        def model = new ModelBean(email: 'invalidEmail')

        ValidationEnhancer.enhance(model)

        boolean result = model.validate(['email'])

        assertFalse "Field validation should have failed", result

        assertFalse model.errors.hasFieldErrors('id')
        assertTrue model.errors.hasFieldErrors('email')

        def fieldError = model.errors.getFieldError('email')

        assertEquals("Error code is not correct", "modelBean.email.email.message", fieldError.errorCode)
        assertEquals("Default error code is not correct", "default.email.message", fieldError.defaultErrorCode)
    }

    public void testFieldLevelValidationWithMultipleExecution() {
        def model = new ModelBean(email: 'invalidEmail')

        ValidationEnhancer.enhance(model)

        model.validate(['email'])

        boolean result = model.validate(['id'])

        assertFalse "Field validation should have failed", result

        def fieldError = model.errors.getFieldError('id')

        assertEquals("Error code is not correct", "modelBean.id.nullable.message", fieldError.errorCode)
        assertEquals("Default error code is not correct", "default.nullable.message", fieldError.defaultErrorCode)

        model.email = "somebody@email.com"

        assertTrue "Email should be valid", model.validate(['email'])
    }

    public void testFieldLevelValidationWithMultipleFields() {
        def model = new ModelBean(email: 'invalidEmail')

        ValidationEnhancer.enhance(model)

        boolean result = model.validate(['id', 'email'])

        assertFalse "Field validation should have failed", result

        assertTrue model.errors.hasFieldErrors('id')
        assertTrue model.errors.hasFieldErrors('email')
    }

    public void testFieldLevelValidationWithSingleString() {
        def model = new ModelBean(email: 'invalidEmail')

        ValidationEnhancer.enhance(model)

        boolean result = model.validate('email')

        assertFalse "Field validation should have failed", result

        assertFalse model.errors.hasFieldErrors('id')
        assertTrue model.errors.hasFieldErrors('email')
    }

}