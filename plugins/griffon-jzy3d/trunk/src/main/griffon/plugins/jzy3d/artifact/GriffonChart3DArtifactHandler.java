package griffon.plugins.jzy3d.artifact;

import org.codehaus.griffon.runtime.core.ArtifactHandlerAdapter;
import griffon.core.GriffonClass;
import griffon.core.GriffonApplication;

/**
 * @author Andres Almiray
 */
public class GriffonChart3DArtifactHandler extends ArtifactHandlerAdapter {
    public GriffonChart3DArtifactHandler(GriffonApplication app) {
        super(app, GriffonChart3DClass.TYPE, GriffonChart3DClass.TRAILING);
    }

    protected GriffonClass newGriffonClassInstance(Class clazz) {
        return new DefaultGriffonChart3DClass(getApp(), clazz);
    }
}
