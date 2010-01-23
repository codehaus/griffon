package griffon.domain

import griffon.core.GriffonApplication

@singleton
class DomainClassEnhancer {
    final enhance(GriffonApplication app, DomainClassEnhancer enhancerDelegate) {
        app.artifactManager.domainArtifacts.each { domain ->

        }
    }
}
