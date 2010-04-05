package net.sourceforge.gvalidation.validator

import org.apache.commons.lang.StringUtils

/**
 * Created by nick.zhu
 */
class BlankValidator extends Closure {

    def BlankValidator(owner) {
        super(owner);
    }

    def doCall(propertyValue, bean, allowBlank) {
        if(propertyValue == null)
            return true

        if (allowBlank) {
            return propertyValue != null
        } else {
            return StringUtils.isNotBlank(propertyValue)
        }
    }
}
