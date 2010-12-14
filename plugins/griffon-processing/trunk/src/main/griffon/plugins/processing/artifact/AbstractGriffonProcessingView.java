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

import griffon.core.GriffonApplication;
import griffon.core.GriffonClass;
import griffon.util.UIThreadHelper;

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

import org.codehaus.griffon.runtime.util.GriffonApplicationHelper;
import org.codehaus.griffon.runtime.core.AbstractGriffonArtifact;

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
        return UIThreadHelper.getInstance().isUIThread();
    }

    public void execAsync(Runnable runnable) {
        UIThreadHelper.getInstance().executeAsync(runnable);
    }

    public void execSync(Runnable runnable) {
        UIThreadHelper.getInstance().executeSync(runnable);
    }

    public void execOutside(Runnable runnable) {
        UIThreadHelper.getInstance().executeOutside(runnable);
    }

    public Future execFuture(ExecutorService executorService, Closure closure) {
        return UIThreadHelper.getInstance().executeFuture(executorService, closure);
    }

    public Future execFuture(Closure closure) {
        return UIThreadHelper.getInstance().executeFuture(closure);
    }

    public Future execFuture(ExecutorService executorService, Callable callable) {
        return UIThreadHelper.getInstance().executeFuture(executorService, callable);
    }

    public Future execFuture(Callable callable) {
        return UIThreadHelper.getInstance().executeFuture(callable);
    }
    
    public Logger getLog() {
        return log;
    }

    public void mvcGroupInit(Map<String, ?> args) {
        // empty
    }

    public void mvcGroupDestroy() {
        // empty
    }

    public Map<String, ?> buildMVCGroup(String mvcType) {
        return GriffonApplicationHelper.buildMVCGroup(getApp(), Collections.emptyMap(), mvcType, mvcType);
    }

    public Map<String, ?> buildMVCGroup(String mvcType, String mvcName) {
        return GriffonApplicationHelper.buildMVCGroup(getApp(), Collections.emptyMap(), mvcType, mvcName);
    }

    public Map<String, ?> buildMVCGroup(Map<String, ?> args, String mvcType) {
        return GriffonApplicationHelper.buildMVCGroup(getApp(), args, mvcType, mvcType);
    }

    public Map<String, ?> buildMVCGroup(Map<String, ?> args, String mvcType, String mvcName) {
        return GriffonApplicationHelper.buildMVCGroup(getApp(), args, mvcType, mvcName);
    }

    public List<?> createMVCGroup(String mvcType) {
        return (List<?>) GriffonApplicationHelper.createMVCGroup(getApp(), mvcType);
    }

    public List<?> createMVCGroup(Map<String, ?> args, String mvcType) {
        return (List<?>) GriffonApplicationHelper.createMVCGroup(getApp(), args, mvcType);
    }

    public List<?> createMVCGroup(String mvcType, Map<String, ?> args) {
        return (List<?>) GriffonApplicationHelper.createMVCGroup(getApp(), args, mvcType);
    }

    public List<?> createMVCGroup(String mvcType, String mvcName) {
        return (List<?>) GriffonApplicationHelper.createMVCGroup(getApp(), mvcType, mvcName);
    }

    public List<?> createMVCGroup(Map<String, ?> args, String mvcType, String mvcName) {
        return (List<?>) GriffonApplicationHelper.createMVCGroup(getApp(), args, mvcType, mvcName);
    }

    public List<?> createMVCGroup(String mvcType, String mvcName, Map<String, ?> args) {
        return (List<?>) GriffonApplicationHelper.createMVCGroup(getApp(), args, mvcType, mvcName);
    }

    public void destroyMVCGroup(String mvcName) {
        GriffonApplicationHelper.destroyMVCGroup(getApp(), mvcName);
    }
}
