/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class SimpleError {
    def defaultErrorCode
    def errorCode
    def arguments

    public String toString() {
        return "Error[${errorCode}:${defaultErrorCode}] with args[${arguments}]"
    }
}
