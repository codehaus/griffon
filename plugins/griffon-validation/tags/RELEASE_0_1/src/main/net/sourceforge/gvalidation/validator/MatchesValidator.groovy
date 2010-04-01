package net.sourceforge.gvalidation.validator

/**
 * Created by nick.zhu
 */
class MatchesValidator extends Closure {

    def MatchesValidator(owner) {
        super(owner);
    }

    def doCall(property, bean, regex){
        if(!property)
            return true

        def valid = false

        if(regex){
            valid = property ==~ regex  
        }

        return valid 
    }

}
