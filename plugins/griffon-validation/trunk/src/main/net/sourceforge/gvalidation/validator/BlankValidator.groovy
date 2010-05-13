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
        if (allowBlank)
            return true

        if (propertyValue && !(propertyValue instanceof String))
            return true

        return StringUtils.isNotBlank(propertyValue)
    }
}
