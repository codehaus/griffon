package net.sourceforge.gvalidation.validator

import org.apache.commons.lang.StringUtils

/**
 * Created by nick.zhu
 */
class EmailValidator extends Closure {

    def EmailValidator(owner) {
        super(owner);
    }

    def doCall(propertyValue, bean, isEmail) {
        if(!propertyValue)
            return true
        
        def valid = true

        if (isEmail) {
            def validator = org.apache.commons.validator.EmailValidator.getInstance()
            valid = validator.isValid(propertyValue)
        }

        return valid
    }

}
