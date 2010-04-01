package net.sourceforge.gvalidation.validator

/**
 * Created by nick.zhu
 */
class UrlValidator extends Closure {
    static def org.apache.commons.validator.UrlValidator validator = new org.apache.commons.validator.UrlValidator ()

    def UrlValidator(owner) {
        super(owner);
    }

    def doCall(value, bean, isUrl) {
        if(!value)
            return true
        
        def valid = true

        if (isUrl) {
            valid = validator.isValid(value)
        }

        return valid
    }

}
