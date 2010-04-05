package net.sourceforge.gvalidation.validator

/**
 * Created by nick.zhu
 */
class ValidatorClosure extends Closure {

    def ValidatorClosure(owner) {
        super(owner)
    }

    def isSameType(left, right) {
        left.getClass().isInstance(right)
    }

}
