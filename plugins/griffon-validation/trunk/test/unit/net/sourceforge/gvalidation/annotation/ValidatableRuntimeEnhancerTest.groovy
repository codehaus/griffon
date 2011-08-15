/*
 * Copyright 2010 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.sourceforge.gvalidation.annotation

import net.sourceforge.gvalidation.BaseTestCase
import net.sourceforge.gvalidation.models.AnnotatedModel

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class ValidatableRuntimeEnhancerTest extends BaseTestCase {

    void testModelRealTimeEnhancement() {
        def model = generateModel()

        ValidatableRuntimeEnhancer.instance.enhance(model)

        assertEquals('Property change listener is not registered', 1, model.propertyChangeListeners.size())

        model.email = "bademail"

        assertTrue("Validation should have been performed", model.errors.hasFieldErrors('email'))
    }

    void testModelWithoutRealTimeEnhancement() {
        def model = generateModel('ModelBean.groovy')

        ValidatableRuntimeEnhancer.instance.enhance(model)

        assertEquals('Property change listener should not be registered', 0, model.propertyChangeListeners.size())

        model.email = "bademail"

        assertFalse("Validation should not have been performed", model.errors.hasFieldErrors('email'))
    }

    void testModelWithoutAnnotationEnhancement() {
        def model = generateModel('PlainModelBean.groovy')

        ValidatableRuntimeEnhancer.instance.enhance(model)
    }

}
