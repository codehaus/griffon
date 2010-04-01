package net.sourceforge.gvalidation.validator

/**
 * Created by nick.zhu
 */
class MaxSizeValidator extends Closure {

    def MaxSizeValidator(owner) {
        super(owner);
    }

    def doCall(property, bean, maxSize) {
        if(!property)
            return true
        
        def valid = false

        if (maxSize) {
            def actualSize = 0

            property.each { actualSize++ }

            valid = maxSize >= actualSize
        }

        return valid
    }

}
