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

import org.apache.commons.lang.StringUtils
import org.apache.commons.lang.ClassUtils

/**
 * Created by nick.zhu
 */
class ValidationEnhancer {
    static final def CONSTRAINT_PROPERTY_NAME = "constraints"
    static final def VALIDATION_ENHANCER_PROPERTY_NAME = '__validationEnhancer'
    static final def ERRORS_PROPERTY_NAME = '__errors'

    static def enhance(bean) {
        if (isNotEnhanced(bean)) {
            final def enhancer = new ValidationEnhancer(bean)
            bean.metaClass."${VALIDATION_ENHANCER_PROPERTY_NAME}" = enhancer
            bean.metaClass."${ERRORS_PROPERTY_NAME}" = new Errors(bean)
            bean.metaClass.setErrors << {
                Errors e ->
                def newValue = e
                def oldValue = bean."${ERRORS_PROPERTY_NAME}"
                bean."${ERRORS_PROPERTY_NAME}" = newValue
                firePropertyChange('errors', oldValue, newValue)
            }
            bean.metaClass.getErrors << { bean."${ERRORS_PROPERTY_NAME}" }
            bean.metaClass.hasErrors << { bean.errors.hasErrors() }
        }

        return bean."${VALIDATION_ENHANCER_PROPERTY_NAME}"
    }

    private static def isNotEnhanced(bean) {
        return !bean.metaClass.hasProperty(VALIDATION_ENHANCER_PROPERTY_NAME)
    }

    private List fields

    private def model

    private ValidationEnhancer(bean) {
        model = bean

        bean.metaClass.validate = { fields = null ->
            ValidationEnhancer.enhance(bean)
            doValidate(fields)
        }
    }

    /**
     * This closure gets invoked when validate method on the model is
     * executed each time
     */
    def doValidate = {params = null ->
        this.fields = generateTargetFields(params)

        try {
            model.errors.clear()

            if (hasNoConstraintsDefined())
                return true

            Closure constraints = extractConstraints()
            constraints.delegate = this
            constraints.call()
        } finally {
            this.fields = null
        }

        return !model.hasErrors()
    }

    private List generateTargetFields(params) {
        List targets = null

        if (params instanceof List)
            targets = params
        else if (params != null)
            targets = [params]

        return targets
    }

    private boolean hasNoConstraintsDefined() {
        return !model.hasProperty(CONSTRAINT_PROPERTY_NAME)
    }

    private def extractConstraints() {
        model.getProperty(CONSTRAINT_PROPERTY_NAME)
    }

    /**
     * Method missing here is used to capture all constraint invocation on a model
     *
     * @param name property name
     * @param args constraint config
     */
    def methodMissing(String name, args) {
        if (!model.hasProperty(name)) {
            throw new IllegalStateException("""Invalid constraint configuration detected.
                    Property [${name}] with constraint configured is missing.""")
        }

        def propertyValue = model.getProperty(name)

        boolean valid = processConstraints(args, propertyValue, name)

        return valid
    }

    private boolean processConstraints(args, propertyValue, String name) {
        if (noValidationForField(name))
            return true

        boolean valid = true

        def constraintsMap = args[0]

        constraintsMap.each {constraint, config ->
            valid = performValidation(constraint, propertyValue, config, name)
        }

        return valid
    }

    private boolean noValidationForField(String name) {
        return fields && !fields.contains(name)
    }

    private def performValidation(constraint, propertyValue, config, name) {
        def validator = ConstraintRepository.instance.getValidator(constraint)

        if (validator) {
            return executeValidator(validator, propertyValue, config, name, constraint)
        } else {
            return handleMissingValidator(constraint)
        }
    }

    private def executeValidator(validator, propertyValue, config, name, constraint) {
        def success = false

        if (validator instanceof Closure) {
            success = validator.call(propertyValue, model, config)
        } else {
            success = validator.validate(propertyValue, model, config)
        }

        if (success)
            return true

        model.errors.rejectValue(name,
                buildErrorCode(name, constraint),
                buildDefaultErrorCode(constraint),
                buildErrorArguments(name, model, propertyValue, config))

        return false
    }

    private def handleMissingValidator(constraint) {
        return true
    }

    private def buildDefaultErrorCode(constraint) {
        return "default.${constraint}.message"
    }

    private def buildErrorCode(fieldName, constraint) {
        def className = StringUtils.uncapitalize(getShortClassName(model))

        return "${className}.${fieldName}.${constraint}.message"
    }

    private List buildErrorArguments(String name, model, propertyValue, config) {
        return [name, getShortClassName(model), "${propertyValue}", config]
    }

    private String getShortClassName(model) {
        if (!model)
            return "null"

        return ClassUtils.getShortClassName(model.getClass())
    }

}
