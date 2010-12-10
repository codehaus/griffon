package griffon.plugins.ratpack.artifact;

import griffon.core.GriffonArtifact;
import groovy.lang.Closure;

/**
 * @author Andres Almiray
 */
public interface GriffonRatpackApp extends GriffonArtifact {
    Closure getRoutes();
}
