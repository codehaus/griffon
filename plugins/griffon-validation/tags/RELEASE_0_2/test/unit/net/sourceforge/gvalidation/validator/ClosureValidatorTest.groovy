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
