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

/**
 * Created by nick.zhu
 */
class ValidatableASTTransformationTest extends GroovyTestCase {

    public void testIgnoreNonAnnotatedModel() {
        def model = generateModel("ModelBean.groovy")

        try {
            model.validate()
            fail("Method should not have been injected, exception should have thrown")
        } catch (MissingMethodException ex) {
            // succeed
        }
    }

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

    private def generateModel(fileName = "AnnotatedModel.groovy") {
        def file = new File("test/unit/net/sourceforge/gvalidation/models/${fileName}")
        assert file.exists()

        TranformTestHelper invoker = new TranformTestHelper(new ValidatableASTTransformation(), CompilePhase.SEMANTIC_ANALYSIS)
        def modelClass = invoker.parse(file)
        def model = modelClass.newInstance()

        return model
    }


}
