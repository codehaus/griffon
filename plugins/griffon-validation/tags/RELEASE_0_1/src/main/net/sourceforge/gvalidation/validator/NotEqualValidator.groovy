package net.sourceforge.gvalidation.validator

/**
 * Created by nick.zhu
 */
class NotEqualValidator extends Closure {

    def NotEqualValidator(owner) {
        super(owner);
    }

    def doCall(value, bean, compareTo){
        def valid = false

        valid = value != compareTo

        return valid
    }

}
