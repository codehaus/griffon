package griffon.plugins.ratpack.artifact;

import org.codehaus.griffon.runtime.core.AbstractGriffonArtifact;

/**
 * @author Andres Almiray
 */
public abstract class AbstractGriffonRatpackApp extends AbstractGriffonArtifact implements GriffonRatpackApp {
    protected String getArtifactType() {
        return GriffonRatpackAppClass.TYPE;
    }
}
