package net.sourceforge.gvalidation.validator

/**
 * Created by nick.zhu
 */
class MinValidator extends ValidatorClosure {

    def MinValidator(owner) {
        super(owner)
    }

    def doCall(property, bean, min){
        if(!property)
            return true
        
        def valid = false

        if(min && isSameType(property, min)){
            valid = property > min
        }

        if(!min)
            valid = true

        return valid
    }
    
}

