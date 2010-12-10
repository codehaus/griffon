package griffon.plugins.ratpack.artifact;

import griffon.core.GriffonApplication;
import org.codehaus.griffon.runtime.core.DefaultGriffonClass;

/**
 * @author Andres Almiray
 */
public class DefaultGriffonRatpackAppClass extends DefaultGriffonClass implements GriffonRatpackAppClass {
    public DefaultGriffonRatpackAppClass(GriffonApplication app, Class clazz) {
        super(app, clazz, GriffonRatpackAppClass.TYPE, GriffonRatpackAppClass.TRAILING);
    }
}
