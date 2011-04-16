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
    static final def FIRE_PROPERTY_CHANGE_METHOD_NAME = 'firePropertyChange'

    def parent
    def fieldErrors = [:]
    def globalErrors = []

    def errorListeners = []

    def Errors() {
        this(null)
    }

    def Errors(parent) {
        this.parent = parent
    }

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
        def oldErrors = cloneErrors()

        globalErrors.add(new SimpleError(errorCode: errorCode, defaultErrorCode: defaultErrorCode, arguments: arguments))

        fireErrorChangedEventOnParent(oldErrors)
    }

    private Errors cloneErrors() {
        return new Errors(parent: parent, fieldErrors: fieldErrors.clone(), globalErros: globalErrors.clone())
    }

    private def fireErrorChangedEventOnParent(Errors oldErrors) {
        if (parent && hasPropertyChangeNotifier()) {
            parent.firePropertyChange('errors', oldErrors, this)
        }
    }

    private def hasPropertyChangeNotifier() {
        return parent?.metaClass.methods?.find {it.name == FIRE_PROPERTY_CHANGE_METHOD_NAME} != null
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

        def oldErrors = cloneErrors()

        def fieldError = new FieldError(field: field, errorCode: errorCode, defaultErrorCode: defaultErrorCode, arguments: arguments)

        fieldErrors[field].add(fieldError)

        errorListeners.each {ErrorListener listener ->
            listener.onFieldErrorAdded(fieldError)
        }

        fireErrorChangedEventOnParent(oldErrors)
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
        def oldErrors = cloneErrors()

        fieldErrors.each {key, error ->
            errorListeners.each {ErrorListener listener ->
                listener.onFieldErrorRemoved(error)
            }
        }

        fieldErrors.clear()
        globalErrors.clear()

        fireErrorChangedEventOnParent(oldErrors)
    }

    def iterator() {
        def allErrors = []
        allErrors.addAll(globalErrors)
        fieldErrors.values().each {
            allErrors.addAll(it)
        }
        return allErrors.iterator()
    }

    public void addListener(ErrorListener errorListener) {
        errorListeners.add(errorListener)
    }

    public boolean hasListener(ErrorListener errorListener) {
        return errorListeners.contains(errorListener)
    }

    public boolean removeListener(ErrorListener errorListener) {
        return errorListeners.remove(errorListener)
    }
}



