package net.sourceforge.gvalidation.validator

/**
 * Created by nick.zhu
 */
class CreditCardValidator extends Closure {

    def CreditCardValidator(owner) {
        super(owner);
    }

    def doCall(propertyValue, bean, isCreditCard){
        if(!propertyValue)
            return true
        
        def valid = true

        if(isCreditCard){
            def validator =  new org.apache.commons.validator.CreditCardValidator()
            valid = validator.isValid(propertyValue)            
        }

        return valid
    }

}
