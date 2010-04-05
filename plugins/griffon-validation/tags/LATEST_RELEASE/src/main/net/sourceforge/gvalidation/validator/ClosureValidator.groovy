package net.sourceforge.gvalidation.validator

/**
 * Created by nick.zhu
 */
class ClosureValidator extends Closure {

    def ClosureValidator(owner) {
        super(owner);
    }

    def doCall(value, bean, closure){
        return closure.call(value, bean)        
    }

}
