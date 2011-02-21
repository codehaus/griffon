package net.sourceforge.gvalidation.artifact

import org.junit.Test
import static org.junit.Assert.*
import griffon.core.GriffonApplication

/**
 * @author Nick Zhu
 */
class ConstraintArtifactHanlderTest {

    @Test
    public void ensureArtifactClassCreation() {
        def artifactHandler = new ConstraintArtifactHandler([] as GriffonApplication)

        def artifactClass = artifactHandler.newGriffonClassInstance(this.getClass())

        assertNotNull("Artifact class can not be null", artifactClass)
        assertTrue("Artifact class type is incorrect", artifactClass instanceof GriffonConstraintClass)
    }

}
