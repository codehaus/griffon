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

package net.sourceforge.gvalidation.annotation

import org.codehaus.groovy.control.CompilePhase
import org.codehaus.groovy.tools.ast.TranformTestHelper
import net.sourceforge.gvalidation.models.BindableModelBean
import java.beans.PropertyChangeListener
import net.sourceforge.gvalidation.Errors

/**
 * Created by nick.zhu
 */
class ValidatableASTTransformationTest extends GroovyTestCase {

    public void testValidateAllInjection() {
        def model = generateModel()

        boolean result = model.validate()

        assertFalse "Validation should have failed", result
        assertTrue("Model should have error", model.hasErrors())
        assertTrue("id field should have error", model.errors.hasFieldErrors('id'))
    }

    public void testSelectiveValidationInjection() {
        def model = generateModel()

        boolean result = model.validate('email')

        assertFalse "Validation should have failed", result
        assertTrue("Model should have error", model.hasErrors())
        assertTrue("email field should have error", model.errors.hasFieldErrors('email'))
        assertFalse("id field should have error", model.errors.hasFieldErrors('id'))
    }

    public void testGoodValidateAll() {
        def model = generateModel()

        model.id = "goodID"
        model.email = "someone@email.com"
        model.zipCode = 123456

        boolean result = model.validate()

        assertTrue "Validation should have passed", result
        assertFalse("Model should not have error", model.hasErrors())
    }

    public void testGoodSelectiveValidation() {
        def model = generateModel()

        model.email = "someone@email.com"

        boolean result = model.validate(['email'])

        assertTrue "Validation should have passed", result
        assertFalse("Model should not have error", model.hasErrors())
    }

    public void testErrorPropertyChange(){
        def model = generateModel()

        def firedEvent = false
        model.addPropertyChangeListener({e->
            assertEquals(e.propertyName, "errors"); firedEvent = true
        } as PropertyChangeListener)

        model.errors.reject('testError')

        assertTrue "Property change event should have been fired", firedEvent
    }

    public void testErrorGetterSetterInjection(){
        def model = generateModel()

        def methods = model.metaClass.methods

        assertNotNull("Dynamic setter is not generated", methods.find{it.name == "setErrors"})

        def firedEvent = false
        model.addPropertyChangeListener({e->
            assertEquals(e.propertyName, "errors"); firedEvent = true
        } as PropertyChangeListener)

        model.errors = new Errors()

        assertTrue "Property change event should have been fired", firedEvent
    }

    private def generateModel(fileName = "AnnotatedModel.groovy") {
        def file = new File("test/unit/net/sourceforge/gvalidation/models/${fileName}")
        assert file.exists()

        TranformTestHelper invoker = new TranformTestHelper(new ValidatableASTTransformation(), CompilePhase.SEMANTIC_ANALYSIS)
        def modelClass = invoker.parse(file)
        def model = modelClass.newInstance()

        return model
    }


}
