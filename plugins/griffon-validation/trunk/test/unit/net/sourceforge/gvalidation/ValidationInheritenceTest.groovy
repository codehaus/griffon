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

import net.sourceforge.gvalidation.models.AnnotatedModel
import net.sourceforge.gvalidation.models.ModelBeanWithBallback
import net.sourceforge.gvalidation.models.ModelBean
import net.sourceforge.gvalidation.models.ChildModelBean
import net.sourceforge.gvalidation.models.GrandChildModelBean

/**
 * Created by nick.zhu
 */
class ValidationInheritenceTest extends GroovyTestCase {
    
    public void testBasicConstraintInheritence() {
        def model = new ChildModelBean()

        model.validate()

        assertTrue "Shoudl have errors", model.hasErrors()
        assertTrue "Id can not be null", model.errors.hasFieldErrors('id')
    }

    public void testConstraintOverrideWithStricterConstraint(){
        def model = new ChildModelBean(id:1,
                email:"", // override to not allow blank
                name:"Name",
                zipCode:"123456")

        model.validate()

        assertTrue "Shoudl have errors", model.hasErrors()
        assertTrue "Email can not be blank", model.errors.hasFieldErrors('email')
    }

    public void testConstraintOverrideWithRelaxedConstraint(){
        def model = new ChildModelBean(id:1,
                email:"email@email.com",
                name:"Name",
                zipCode:"123" // override should not allow more relaxed zipcode 
        )

        model.validate()

        assertTrue "Shoudl have errors", model.hasErrors()
        assertTrue "Short zipCode should not be allowed", model.errors.hasFieldErrors('zipCode')
    }

    public void testBasicMultiLevelConstraintInheritence() {
        def model = new GrandChildModelBean()

        model.validate()

        assertTrue "Shoudl have errors", model.hasErrors()
        assertTrue "Id can not be null", model.errors.hasFieldErrors('id')
        assertTrue "Email should be invalid", model.errors.hasFieldErrors('email')
    }

}
