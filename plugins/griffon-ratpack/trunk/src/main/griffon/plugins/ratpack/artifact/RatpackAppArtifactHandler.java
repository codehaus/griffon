package griffon.plugins.ratpack.artifact;

import org.codehaus.griffon.runtime.core.ArtifactHandlerAdapter;
import griffon.core.GriffonClass;
import griffon.core.GriffonApplication;

/**
 * @author Andres Almiray
 */
public class RatpackAppArtifactHandler extends ArtifactHandlerAdapter {
    public RatpackAppArtifactHandler(GriffonApplication app) {
        super(app, GriffonRatpackAppClass.TYPE, GriffonRatpackAppClass.TRAILING);
    }

    protected GriffonClass newGriffonClassInstance(Class clazz) {
        return new DefaultGriffonRatpackAppClass(getApp(), clazz);
    }
}
