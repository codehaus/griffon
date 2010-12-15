package griffon.plugins.jzy3d.artifact;

import griffon.core.GriffonApplication;
import org.codehaus.griffon.runtime.core.DefaultGriffonClass;

/**
 * @author Andres Almiray
 */
public class DefaultGriffonChart3DClass extends DefaultGriffonClass implements GriffonChart3DClass {
    public DefaultGriffonChart3DClass(GriffonApplication app, Class clazz) {
        super(app, clazz, GriffonChart3DClass.TYPE, GriffonChart3DClass.TRAILING);
    }
}
