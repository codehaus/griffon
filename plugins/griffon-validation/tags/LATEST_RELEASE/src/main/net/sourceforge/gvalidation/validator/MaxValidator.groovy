package net.sourceforge.gvalidation.validator

/**
 * Created by nick.zhu
 */
class MaxValidator extends ValidatorClosure {

    def MaxValidator(owner) {
        super(owner);
    }

    def doCall(property, bean, max){
        if(!property)
            return true
        
        def valid = false

        if(max && isSameType(property, max)){
            valid = property < max
        }

        if(!property)
            valid = true

        return valid
    }

}
