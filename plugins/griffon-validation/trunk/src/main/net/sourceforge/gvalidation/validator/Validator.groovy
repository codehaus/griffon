package net.sourceforge.gvalidation.validator

/**
 * Generic base interface for all validator implementation
 *
 * @author Nick Zhu (nzhu@jointsource.com)
 */
public interface Validator {

    /**
     * Validate method
     *
     * @param propertyValue value of the property to be validated
     * @param bean object owner of the property
     * @param parameter configuration parameter for the constraint
     * @return return true if validation passes otherwise false
     */
    def validate(propertyValue, bean, parameter)


}