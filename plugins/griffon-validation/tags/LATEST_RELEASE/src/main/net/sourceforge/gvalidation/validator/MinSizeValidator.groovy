package net.sourceforge.gvalidation.validator

/**
 * Created by nick.zhu
 */

class MinSizeValidator extends Closure {

    def MinSizeValidator(owner) {
        super(owner);
    }

    def doCall(property, bean, minSize) {
        if(!property)
            return true
        
        def valid = true

        if (minSize) {
            def actualSize = 0

            property?.each { actualSize++ }

            valid = minSize <= actualSize
        }

        return valid
    }

}