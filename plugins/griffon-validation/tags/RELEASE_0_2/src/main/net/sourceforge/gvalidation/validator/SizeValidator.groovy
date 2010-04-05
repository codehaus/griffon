package net.sourceforge.gvalidation.validator

/**
 * Created by nick.zhu
 */
class SizeValidator extends Closure {

    def SizeValidator(owner) {
        super(owner);
    }

    def doCall(value, bean, input) {
        if(!value)
            return true
        
        def valid = false

        if (input && input instanceof Range) {
            Range range = (Range) input
            
            def actualSize = 0

            value?.each { actualSize++ }

            valid = range.contains(actualSize)
        }

        return valid
    }

}
