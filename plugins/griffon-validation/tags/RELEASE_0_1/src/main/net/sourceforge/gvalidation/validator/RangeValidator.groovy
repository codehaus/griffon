package net.sourceforge.gvalidation.validator

/**
 * Created by nick.zhu
 */
class RangeValidator extends Closure {

    def RangeValidator(owner) {
        super(owner);
    }

    def doCall(value, bean, input){
        if(!value)
            return true

        def valid = false

        if(input && input instanceof Range){
            Range range = (Range) input
            valid = range.contains(value)
        }

        return valid
    }

}
