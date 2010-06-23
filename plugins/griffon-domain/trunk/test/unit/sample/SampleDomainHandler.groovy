package sample

import griffon.core.GriffonApplication
import griffon.core.ArtifactInfo
import griffon.domain.DomainHandler
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

    protected Object fetch(ArtifactInfo artifactInfo, Object key) {
        return key
    }
}

class SampleFindAllMethod extends AbstractFindAllPersistentMethod {
    SampleFindAllMethod(DomainHandler domainHandler) {
        super(domainHandler)
    }
    protected Collection findAll(ArtifactInfo artifactInfo, Class clazz) {
        [1]
    }

    protected Collection findByProperties(ArtifactInfo artifactInfo, Map properties) {
        [properties]
    }

    protected Collection findByExample(ArtifactInfo artifactInfo, Object example) {
        [4]
    }

    protected Collection findByCriterion(ArtifactInfo artifactInfo, Criterion criterion, Map options) {
        [3]
    }
}

