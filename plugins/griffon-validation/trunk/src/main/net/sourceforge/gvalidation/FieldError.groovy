import net.sourceforge.gvalidation.SimpleError

/**
 * @author Nick Zhu (nzhu@jointsource.com)
 */
class FieldError extends SimpleError {
    def field

    public String toString() {
        return "Error[${errorCode}] with args[${arguments}] on field[${field}]"
    }
}
