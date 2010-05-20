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

package net.sourceforge.gvalidation.validator

import net.sourceforge.gvalidation.validator.ClosureValidator
import net.sourceforge.gvalidation.ValidationEnhancer

/**
 * Created by nick.zhu
 */
class ClosureValidatorTest extends GroovyTestCase {

    public void testBasicClosureBasedValidation(){
        ClosureValidator validator = new ClosureValidator(this)

        assertTrue "Should be valid", validator.call(null, this, {value, obj -> true})
        assertFalse "Should not be valid", validator.call(null, this, {value, obj -> false})
    }
    
    public void testErrorGeneration(){
        def obj = new Object()

        ValidationEnhancer.enhance(obj)

        ClosureValidator validator = new ClosureValidator(obj)

        assertFalse "Should be valid", validator.call(null, obj, {value, bean -> bean.errors.reject('error'); false})

        assertTrue "Error should be generated", obj.hasErrors()
    }
    
}
