package net.sourceforge.gvalidation.validator

/**
 * Created by nick.zhu
 */
class InetAddressValidator extends Closure {

    def InetAddressValidator(owner) {
        super(owner);
    }

    def doCall(value, bean, isInetAddress) {
        if(!value)
            return true

        def valid = true

        if (isInetAddress) {
            try {
                InetAddress.getByName(value)

                valid = true
            } catch (UnknownHostException ex) {
                valid = false
            }
        }

        return valid
    }

}
