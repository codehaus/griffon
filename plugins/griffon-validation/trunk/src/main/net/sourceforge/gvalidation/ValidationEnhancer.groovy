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
import net.sourceforge.gvalidation.validator.SizeValidator
import net.sourceforge.gvalidation.validator.NullableValidator
import net.sourceforge.gvalidation.validator.NotEqualValidator
import net.sourceforge.gvalidation.validator.EmailValidator
import net.sourceforge.gvalidation.validator.MaxValidator
import net.sourceforge.gvalidation.validator.ClosureValidator
import net.sourceforge.gvalidation.validator.MinSizeValidator
import net.sourceforge.gvalidation.validator.CreditCardValidator
import net.sourceforge.gvalidation.validator.MatchesValidator
import net.sourceforge.gvalidation.validator.BlankValidator
import net.sourceforge.gvalidation.validator.UrlValidator
import net.sourceforge.gvalidation.validator.MaxSizeValidator
import net.sourceforge.gvalidation.validator.RangeValidator
import net.sourceforge.gvalidation.validator.MinValidator
import net.sourceforge.gvalidation.validator.InListValidator
import net.sourceforge.gvalidation.validator.InetAddressValidator

/**
 * Created by nick.zhu
 */
class ValidationEnhancer {
    static final def CONSTRAINT_PROPERTY_NAME = "constraints"

    static def enhance(bean) {
        new ValidationEnhancer(bean)
    }

    def model

    public ValidationEnhancer(bean) {
        model = bean

        bean.metaClass.validate = {
            validate()
        }

        bean.metaClass.errors = new Errors()
        bean.metaClass.hasErrors = { model.errors.hasErrors() }
    }

    def validate() {
        model.errors.clear()

        if (!model.hasProperty(CONSTRAINT_PROPERTY_NAME))
            return true

        Closure constraints = model.getProperty(CONSTRAINT_PROPERTY_NAME)

        constraints.delegate = this

        constraints.call()

        return !model.hasErrors()
    }

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
        boolean valid = true

        def constraintsMap = args[0]

        constraintsMap.each {constraint, config ->
            valid = validate(constraint, propertyValue, config, name)
        }

        return valid
    }

    private def validate(constraint, propertyValue, config, name) {
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
