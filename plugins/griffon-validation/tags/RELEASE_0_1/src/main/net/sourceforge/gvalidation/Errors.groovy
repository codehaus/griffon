package net.sourceforge.gvalidation

/**
 * Created by nick.zhu
 */
class Errors {
    def fieldErrors = [:]
    def globalErrors = []

    def reject(errorCode) {
        reject(errorCode, [])
    }

    def reject(errorCode, arguments) {
        globalErrors.add(new SimpleError(errorCode: errorCode, arguments: arguments))
    }

    def hasGlobalErrors() {
        return !globalErrors.isEmpty()
    }

    def rejectValue(field, errorCode) {
        rejectValue(field, errorCode, [])
    }

    def rejectValue(field, errorCode, arguments) {
        if (!fieldErrors[field])
            fieldErrors[field] = []

        fieldErrors[field].add new FieldError(field: field, errorCode: errorCode, arguments: arguments)
    }

    def hasFieldErrors() {
        return !fieldErrors.isEmpty()
    }

    def hasFieldErrors(field) {
        return fieldErrors[field] && fieldErrors[field].size() > 0
    }

    def getFieldError(field) {
        return fieldErrors[field]?.first()
    }

    def getFieldErrors(field) {
        return fieldErrors[field]
    }

    def hasErrors() {
        return hasGlobalErrors() || hasFieldErrors()
    }

    def clear() {
        fieldErrors.clear()
        globalErrors.clear()
    }

    def iterator() {
        def allErrors = []
        allErrors.addAll(globalErrors)
        fieldErrors.values().each {
            allErrors.addAll(it)
        }
        return allErrors.iterator()
    }
}

class FieldError extends SimpleError {
    def field

    public String toString() {
        return "Error[${errorCode}] with args[${arguments}] on field[${field}]"
    }
}

class SimpleError {
    def errorCode
    def arguments

    public String toString() {
        return "Error[${errorCode}] with args[${arguments}]"
    }
}