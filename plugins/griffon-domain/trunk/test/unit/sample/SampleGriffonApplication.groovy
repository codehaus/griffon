package sample

import griffon.core.*

class SampleGriffonApplication {
    @Delegate private final BaseGriffonApplication _base

    SampleGriffonApplication() {
        _base = new BaseGriffonApplication(this)
        this.metaClass.artifactManager = ArtifactManager.instance
        ArtifactManager.instance.app = this
        ArtifactManager.instance.with {
            registerArtifactHandler(new ModelArtifactHandler())
            registerArtifactHandler(new ViewArtifactHandler())
            registerArtifactHandler(new ControllerArtifactHandler())
            registerArtifactHandler(new ServiceArtifactHandler())
        }
    }

    Object createApplicationContainer() {
        null
    }
}
