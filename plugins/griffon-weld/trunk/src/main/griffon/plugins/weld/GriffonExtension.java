/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.plugins.weld;

import griffon.core.GriffonApplication;
import griffon.core.GriffonClass;
import griffon.core.UIThreadManager;
import griffon.util.ApplicationHolder;
import static griffon.util.GriffonNameUtils.isBlank;
import groovy.lang.Script;
import groovy.util.ConfigObject;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.*;
import java.util.*;

/**
 * @author Andres Almiray
 */
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

    private static final Map<String, Object> ADDITIONAL_BEANS = new LinkedHashMap<String, Object>();
    
    public static void registerBean(String beanName, Object bean) {
        if(!isBlank(beanName) && bean != null) {
            ADDITIONAL_BEANS.put(beanName, bean);
        }
    }

    void afterBeanDiscovery(@Observes AfterBeanDiscovery abd, BeanManager bm) {
        GriffonApplication app = ApplicationHolder.getApplication();
        abd.addBean(new BeanFactory(bm, app.getClass(), "app", app));
        abd.addBean(new BeanFactory<ConfigObject>(bm, ConfigObject.class, "appConfig", app.getConfig()));
        abd.addBean(new BeanFactory(bm, app.getArtifactManager().getClass(), "artifactManager", app.getArtifactManager()));
        abd.addBean(new BeanFactory(bm, app.getAddonManager().getClass(), "addonManager", app.getAddonManager()));
        abd.addBean(new BeanFactory<UIThreadManager>(bm, UIThreadManager.class, "uiThreadManager", UIThreadManager.getInstance()));

        for(GriffonClass griffonClass: app.getArtifactManager().getAllClasses()) {
            abd.addBean(new BeanFactory(bm, griffonClass.getClass(), griffonClass.getPropertyName() + "Class", griffonClass));
        }

        for(Map.Entry<String, Object> entry : ADDITIONAL_BEANS.entrySet()) {
            abd.addBean(new BeanFactory(bm, entry.getValue().getClass(), entry.getKey(), entry.getValue()));
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
