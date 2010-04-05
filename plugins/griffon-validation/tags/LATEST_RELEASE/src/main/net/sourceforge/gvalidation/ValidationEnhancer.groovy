package net.sourceforge.gvalidation

import org.apache.log4j.Logger
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

    static def log = Logger.getLogger(ValidationEnhancer)

    static def validators = Collections.unmodifiableMap([
            nullable: new NullableValidator(this),
            blank: new BlankValidator(this),
            email: new EmailValidator(this),
            creditCard: new CreditCardValidator(this),
            inList: new InListValidator(this),
            matches: new MatchesValidator(this),
            max: new MaxValidator(this),
            maxSize: new MaxSizeValidator(this),
            min: new MinValidator(this),
            minSize: new MinSizeValidator(this),
            notEqual: new NotEqualValidator(this),
            range: new RangeValidator(this),
            size: new SizeValidator(this),
            url: new UrlValidator(this),
            inetAddress: new InetAddressValidator(this),
            validator: new ClosureValidator(this)
    ])

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
        log.debug "Executing validation constraint[${constraint}] with input[${config}]"

        def validator = validators[constraint]

        if (validator) {
            return executeValidator(validator, propertyValue, config, name, constraint)
        } else {
            return handleMissingValidator(constraint)
        }
    }

    private def executeValidator(validator, propertyValue, config, name, constraint) {
        def success = validator.call(propertyValue, model, config)

        if (success)
            return true

        log.debug "Rejecting property ${name} by constriant ${constraint}"

        model.errors.rejectValue(name,
                buildErrorCode(name, constraint), buildErrorArguments(name, model, propertyValue))

        return false
    }

    private def handleMissingValidator(constraint) {
        log.warn """Ignoring unknown validator[${constraint}], please check your constraint configuration"""
        return true
    }

    private GString buildErrorCode(fieldName, constraint) {
        def className = StringUtils.uncapitalize(getShortClassName(model))

        return "${className}.${fieldName}.${constraint}.message"
    }

    private List buildErrorArguments(String name, model, propertyValue) {
        return [name, getShortClassName(model), "${propertyValue}"]
    }

    private String getShortClassName(model) {
        if (!model)
            return "null"

        return ClassUtils.getShortClassName(model.getClass())
    }

}
