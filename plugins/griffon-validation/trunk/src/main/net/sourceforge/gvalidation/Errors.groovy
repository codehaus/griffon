/*
 * Copyright 2010 the original author or authors.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

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

    def reject(errorCode, defaultErrorCode) {
        reject(errorCode, defaultErrorCode, [])
    }

    def reject(errorCode, List arguments) {
        reject(errorCode, null, arguments)                                                    
    }

    def reject(errorCode, defaultErrorCode, List arguments) {
        globalErrors.add(new SimpleError(errorCode: errorCode, defaultErrorCode: defaultErrorCode, arguments: arguments))
    }

    def hasGlobalErrors() {
        return !globalErrors.isEmpty()
    }

    def rejectValue(field, errorCode) {
        rejectValue(field, errorCode, [])
    }

    def rejectValue(field, errorCode, defaultErrorCode) {
        rejectValue(field, errorCode, defaultErrorCode, [])
    }

    def rejectValue(field, errorCode, List arguments) {
         rejectValue(field, errorCode, null, arguments)   
    }

    def rejectValue(field, errorCode, defaultErrorCode, List arguments) {
        if (!fieldErrors[field])
            fieldErrors[field] = []

        fieldErrors[field].add new FieldError(field: field, errorCode: errorCode, defaultErrorCode: defaultErrorCode, arguments: arguments)
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
    def defaultErrorCode
    def errorCode
    def arguments

    public String toString() {
        return "Error[${errorCode}:${defaultErrorCode}] with args[${arguments}]"
    }
}