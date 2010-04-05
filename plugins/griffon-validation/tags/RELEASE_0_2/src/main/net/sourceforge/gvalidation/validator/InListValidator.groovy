package net.sourceforge.gvalidation.validator

/**
 * Created by nick.zhu
 */
class InListValidator extends Closure {

    def InListValidator(owner) {
        super(owner);
    }


    def doCall(property, model, list){
        if(!property)
            return true
        
        def valid = false

        if(list){
            valid = list.contains(property)
        }

        return valid
    }

}

