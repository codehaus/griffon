package griffon.plugins.weld;

import griffon.core.GriffonApplication;
import griffon.core.GriffonClass;
import griffon.util.ApplicationHolder;
import griffon.util.UIThreadHelper;
import groovy.lang.Script;
import groovy.util.ConfigObject;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.util.Arrays;
import java.util.List;

public class GriffonExtension implements Extension {
    private static final List<String> SKIP_LIST = Arrays.asList(
            "Application",
            "Builder",
            "Config",
            "Events",
            "Initialize",
            "Startup",
            "Ready",
            "Shutdown",
            "Stop"
    );

    void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
        GriffonApplication app = ApplicationHolder.getApplication();
        abd.addBean(new BeanFactory(bm, app.getClass(), "app", app));
        abd.addBean(new BeanFactory<ConfigObject>(bm, ConfigObject.class, "appConfig", app.getConfig()));
        abd.addBean(new BeanFactory(bm, app.getAddonManager().getClass(), "artifactManager", app.getArtifactManager()));
        abd.addBean(new BeanFactory<UIThreadHelper>(bm, UIThreadHelper.class, "uiThreadHelper", UIThreadHelper.getInstance()));

        for(GriffonClass griffonClass: app.getArtifactManager().getAllClasses()) {
            abd.addBean(new BeanFactory(bm, griffonClass.getClass(), griffonClass.getPropertyName() + "Class", griffonClass));
        }
    }

    <X> void processAnnotatedType(@Observes final ProcessAnnotatedType<X> pat, BeanManager beanManager) {
        AnnotatedType<X> annotatedType = pat.getAnnotatedType();
        Class<X> clazz = annotatedType.getJavaClass();

        GriffonApplication app = ApplicationHolder.getApplication();
        if (SKIP_LIST.contains(clazz.getCanonicalName()) ||
            Script.class.isAssignableFrom(clazz) ||
            app.getArtifactManager().findGriffonClass(clazz) != null) {
            pat.veto();
            return;
        }
    }
}
