package org.codehaus.griffon.runtime.domain

import griffon.core.GriffonApplication
import griffon.domain.GriffonDomain
import griffon.domain.GriffonDomainClass
import griffon.domain.orm.Criterion
import griffon.domain.metaclass.*

class SampleDomainHandler implements DomainHandler {
    final GriffonApplication app
    final Map dynamicMethods

    SampleDomainHandler(GriffonApplication app) {
        this.app = app

        dynamicMethods = [
            fetch: new SampleFetchMethod(this),
            findAll: new SampleFindAllMethod(this)
        ]
    }

    def invokeInstance(Object target, String methodName, Object... args) {
        dynamicMethods[methodName].invoke(target, methodName, args)
    }

    def invokeStatic(Class clazz, String methodName, Object... args) {
        dynamicMethods[methodName].invoke(clazz, methodName, args)
    }
}

class SampleFetchMethod extends AbstractFetchPersistentMethod {
    SampleFetchMethod(DomainHandler domainHandler) {
        super(domainHandler)
    }

    protected GriffonDomain fetch(GriffonDomainClass domain, Object key) {
        return new Book(title: String.valueOf(key))
    }
}

class SampleFindAllMethod extends AbstractFindAllPersistentMethod {
    SampleFindAllMethod(DomainHandler domainHandler) {
        super(domainHandler)
    }

    protected Collection findAll(GriffonDomainClass domain) {
        [1]
    }

    protected Collection findByProperties(GriffonDomainClass domain, Map properties) {
        [properties]
    }

    protected Collection findByExample(GriffonDomainClass domain, Object example) {
        [4]
    }

    protected Collection findByCriterion(GriffonDomainClass domain, Criterion criterion, Map options) {
        [3]
    }
}