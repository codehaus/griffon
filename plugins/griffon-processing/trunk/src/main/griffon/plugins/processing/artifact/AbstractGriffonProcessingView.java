/*
 * Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package griffon.plugins.processing.artifact;

import processing.core.PApplet;

import griffon.core.*;

import groovy.lang.MetaClass;
import groovy.lang.Closure;
import groovy.lang.GroovySystem;
import groovy.lang.GroovyObjectSupport;
import java.util.Map;
import java.util.List;
import java.util.Collections;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;

import org.codehaus.griffon.runtime.core.AbstractGriffonArtifact;
import org.codehaus.griffon.runtime.util.GriffonApplicationHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Andres Almiray
 */
public abstract class AbstractGriffonProcessingView extends PApplet implements GriffonProcessingView {
    private GriffonApplication app;
    private final Logger log;
    
    public AbstractGriffonProcessingView() {
        log = LoggerFactory.getLogger("griffon.app.processing."+getClass().getName());
    }

    public GriffonApplication getApp() {
        return app;
    }

    public void setApp(GriffonApplication app) {
        this.app = app;
    }

    public Object newInstance(Class clazz, String type) {
        return GriffonApplicationHelper.newInstance(app, clazz, type);
    }

    public MetaClass getMetaClass() {
        return AbstractGriffonArtifact.metaClassOf(this);
    }
    
    public void setMetaClass(MetaClass metaClass) {
        GroovySystem.getMetaClassRegistry().setMetaClass(getClass(), metaClass);
    }

    public GriffonClass getGriffonClass() {
        return app.getArtifactManager().findGriffonClass(getClass());
    }

    public boolean isUIThread() {
        return UIThreadManager.getInstance().isUIThread();
    }

    public void execAsync(Runnable runnable) {
        UIThreadManager.getInstance().executeAsync(runnable);
    }

    public void execSync(Runnable runnable) {
        UIThreadManager.getInstance().executeSync(runnable);
    }

    public void execOutside(Runnable runnable) {
        UIThreadManager.getInstance().executeOutside(runnable);
    }

    public Future execFuture(ExecutorService executorService, Closure closure) {
        return UIThreadManager.getInstance().executeFuture(executorService, closure);
    }

    public Future execFuture(Closure closure) {
        return UIThreadManager.getInstance().executeFuture(closure);
    }

    public Future execFuture(ExecutorService executorService, Callable callable) {
        return UIThreadManager.getInstance().executeFuture(executorService, callable);
    }

    public Future execFuture(Callable callable) {
        return UIThreadManager.getInstance().executeFuture(callable);
    }
    
    public Logger getLog() {
        return log;
    }

    public void mvcGroupInit(Map<String, Object> args) {
        // empty
    }

    public void mvcGroupDestroy() {
        // empty
    }

    public MVCGroup buildMVCGroup(String mvcType) {
        return getApp().getMvcGroupManager().buildMVCGroup(mvcType, mvcType, Collections.<String, Object>emptyMap());
    }

    public MVCGroup buildMVCGroup(String mvcType, String mvcName) {
        return getApp().getMvcGroupManager().buildMVCGroup(mvcType, mvcName, Collections.<String, Object>emptyMap());
    }

    public MVCGroup buildMVCGroup(Map<String, Object> args, String mvcType) {
        return getApp().getMvcGroupManager().buildMVCGroup(mvcType, mvcType, args);
    }

    public MVCGroup buildMVCGroup(String mvcType, Map<String, Object> args) {
        return getApp().getMvcGroupManager().buildMVCGroup(mvcType, mvcType, args);
    }

    public MVCGroup buildMVCGroup(Map<String, Object> args, String mvcType, String mvcName) {
        return getApp().getMvcGroupManager().buildMVCGroup(mvcType, mvcName, args);
    }

    public MVCGroup buildMVCGroup(String mvcType, String mvcName, Map<String, Object> args) {
        return getApp().getMvcGroupManager().buildMVCGroup(mvcType, mvcName, args);
    }

    public List<? extends GriffonMvcArtifact> createMVCGroup(String mvcType) {
        return getApp().getMvcGroupManager().createMVCGroup(mvcType, mvcType, Collections.<String, Object>emptyMap());
    }

    public List<? extends GriffonMvcArtifact> createMVCGroup(Map<String, Object> args, String mvcType) {
        return getApp().getMvcGroupManager().createMVCGroup(mvcType, mvcType, args);
    }

    public List<? extends GriffonMvcArtifact> createMVCGroup(String mvcType, Map<String, Object> args) {
        return getApp().getMvcGroupManager().createMVCGroup(mvcType, mvcType, args);
    }

    public List<? extends GriffonMvcArtifact> createMVCGroup(String mvcType, String mvcName) {
        return getApp().getMvcGroupManager().createMVCGroup(mvcType, mvcName, Collections.<String, Object>emptyMap());
    }

    public List<? extends GriffonMvcArtifact> createMVCGroup(Map<String, Object> args, String mvcType, String mvcName) {
        return getApp().getMvcGroupManager().createMVCGroup(mvcType, mvcName, args);
    }

    public List<? extends GriffonMvcArtifact> createMVCGroup(String mvcType, String mvcName, Map<String, Object> args) {
        return getApp().getMvcGroupManager().createMVCGroup(mvcType, mvcName, args);
    }

    public void destroyMVCGroup(String mvcName) {
        getApp().getMvcGroupManager().destroyMVCGroup(mvcName);
    }

    public void withMVCGroup(String mvcType, Closure handler) {
        getApp().getMvcGroupManager().withMVCGroup(mvcType, mvcType, Collections.<String, Object>emptyMap(), handler);
    }

    public void withMVCGroup(String mvcType, String mvcName, Closure handler) {
        getApp().getMvcGroupManager().withMVCGroup(mvcType, mvcName, Collections.<String, Object>emptyMap(), handler);
    }

    public void withMVCGroup(String mvcType, Map<String, Object> args, Closure handler) {
        getApp().getMvcGroupManager().withMVCGroup(mvcType, mvcType, args, handler);
    }

    public void withMVCGroup(Map<String, Object> args, String mvcType, Closure handler) {
        getApp().getMvcGroupManager().withMVCGroup(mvcType, mvcType, args, handler);
    }

    public void withMVCGroup(String mvcType, String mvcName, Map<String, Object> args, Closure handler) {
        getApp().getMvcGroupManager().withMVCGroup(mvcType, mvcName, args, handler);
    }

    public void withMVCGroup(Map<String, Object> args, String mvcType, String mvcName, Closure handler) {
        getApp().getMvcGroupManager().withMVCGroup(mvcType, mvcName, args, handler);
    }

    public <M extends GriffonModel, V extends GriffonView, C extends GriffonController> void withMVCGroup(String mvcType, MVCClosure<M, V, C> handler) {
        getApp().getMvcGroupManager().withMVCGroup(mvcType, mvcType, Collections.<String, Object>emptyMap(), handler);
    }

    public <M extends GriffonModel, V extends GriffonView, C extends GriffonController> void withMVCGroup(String mvcType, String mvcName, MVCClosure<M, V, C> handler) {
        getApp().getMvcGroupManager().withMVCGroup(mvcType, mvcName, Collections.<String, Object>emptyMap(), handler);
    }

    public <M extends GriffonModel, V extends GriffonView, C extends GriffonController> void withMVCGroup(String mvcType, Map<String, Object> args, MVCClosure<M, V, C> handler) {
        getApp().getMvcGroupManager().withMVCGroup(mvcType, mvcType, args, handler);
    }

    public <M extends GriffonModel, V extends GriffonView, C extends GriffonController> void withMVCGroup(Map<String, Object> args, String mvcType, MVCClosure<M, V, C> handler) {
        getApp().getMvcGroupManager().withMVCGroup(mvcType, mvcType, args, handler);
    }

    public <M extends GriffonModel, V extends GriffonView, C extends GriffonController> void withMVCGroup(String mvcType, String mvcName, Map<String, Object> args, MVCClosure<M, V, C> handler) {
        getApp().getMvcGroupManager().withMVCGroup(mvcType, mvcName, args, handler);
    }

    public <M extends GriffonModel, V extends GriffonView, C extends GriffonController> void withMVCGroup(Map<String, Object> args, String mvcType, String mvcName, MVCClosure<M, V, C> handler) {
        getApp().getMvcGroupManager().withMVCGroup(mvcType, mvcName, args, handler);
    }
}
