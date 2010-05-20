@artifact.package@class @artifact.name@ {

    static def message = "@artifact.name@.message"
    static def parameterType = Boolean

    def validate(propertyValue, bean, parameter) {
        // insert your custom constraint logic 
    }

}