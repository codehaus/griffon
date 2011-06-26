/*
 * Copyright 2009-2011 the original author or authors.
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

package org.codehaus.griffon.runtime.slideware;

import griffon.core.*;
import griffon.util.GriffonExceptionHandler;
import griffon.plugins.slideware.GriffonSlide;
import griffon.plugins.slideware.GriffonSlideClass;

import groovy.lang.MetaClass;
import groovy.lang.ExpandoMetaClass;
import groovy.lang.Closure;
import groovy.lang.Script;
import groovy.lang.GroovySystem;
import groovy.util.FactoryBuilderSupport;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ExecutorService;

import java.util.Map;
import java.util.List;
import java.util.Collections;

import org.codehaus.griffon.runtime.util.GriffonApplicationHelper;
import org.codehaus.griffon.runtime.builder.UberInterceptorMetaClass;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Base implementation of the GriffonSlide interface for Script based slides
 *
 * @author Andres Almiray
 */
public abstract class AbstractGriffonSlideScript extends Script implements GriffonSlide {
    private GriffonApplication app;
    private FactoryBuilderSupport builder;
    private final Logger log;
    private MetaClass _metaClass;
    
    public AbstractGriffonSlideScript() {
        log = LoggerFactory.getLogger("griffon.app."+ GriffonSlideClass.TYPE +"."+ getClass().getName());
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
        if(_metaClass == null) {
            Class clazz = getClass();
            _metaClass = GroovySystem.getMetaClassRegistry().getMetaClass(clazz);
            if(!(_metaClass instanceof ExpandoMetaClass) || !(_metaClass instanceof UberInterceptorMetaClass)) {
                _metaClass = new ExpandoMetaClass(clazz, true, true);
                log.debug("Upgrading MetaClass to "+_metaClass);   
                _metaClass.initialize();
                GroovySystem.getMetaClassRegistry().setMetaClass(clazz, _metaClass);
            }
        }
        return _metaClass;
    }
 
    public void setMetaClass(MetaClass metaClass) {
        _metaClass = metaClass;
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

    public void mvcGroupInit(Map<String, Object> args) {
        // empty
    }

    public void mvcGroupDestroy() {
        // empty
    }

    public Map<String, Object> buildMVCGroup(String mvcType) {
        return GriffonApplicationHelper.buildMVCGroup(getApp(), Collections.emptyMap(), mvcType, mvcType);
    }

    public Map<String, Object> buildMVCGroup(String mvcType, String mvcName) {
        return GriffonApplicationHelper.buildMVCGroup(getApp(), Collections.emptyMap(), mvcType, mvcName);
    }

    public Map<String, Object> buildMVCGroup(Map<String, Object> args, String mvcType) {
        return GriffonApplicationHelper.buildMVCGroup(getApp(), args, mvcType, mvcType);
    }

    public Map<String, Object> buildMVCGroup(Map<String, Object> args, String mvcType, String mvcName) {
        return GriffonApplicationHelper.buildMVCGroup(getApp(), args, mvcType, mvcName);
    }

    public List<? extends GriffonMvcArtifact> createMVCGroup(String mvcType) {
        return GriffonApplicationHelper.createMVCGroup(getApp(), mvcType);
    }

    public List<? extends GriffonMvcArtifact> createMVCGroup(Map<String, Object> args, String mvcType) {
        return GriffonApplicationHelper.createMVCGroup(getApp(), args, mvcType);
    }

    public List<? extends GriffonMvcArtifact> createMVCGroup(String mvcType, Map<String, Object> args) {
        return GriffonApplicationHelper.createMVCGroup(getApp(), args, mvcType);
    }

    public List<? extends GriffonMvcArtifact> createMVCGroup(String mvcType, String mvcName) {
        return GriffonApplicationHelper.createMVCGroup(getApp(), mvcType, mvcName);
    }

    public List<? extends GriffonMvcArtifact> createMVCGroup(Map<String, Object> args, String mvcType, String mvcName) {
        return GriffonApplicationHelper.createMVCGroup(getApp(), args, mvcType, mvcName);
    }

    public List<? extends GriffonMvcArtifact> createMVCGroup(String mvcType, String mvcName, Map<String, Object> args) {
        return GriffonApplicationHelper.createMVCGroup(getApp(), args, mvcType, mvcName);
    }

    public void destroyMVCGroup(String mvcName) {
        GriffonApplicationHelper.destroyMVCGroup(getApp(), mvcName);
    }

    public FactoryBuilderSupport getBuilder() {
        return builder;
    }

    public void setBuilder(FactoryBuilderSupport builder) {
        this.builder = builder;
    }
    
    public Logger getLog() {
        return log;
    }

    public void withMVCGroup(String mvcType, Closure handler) {
        withMVCGroup(mvcType, mvcType, Collections.<String, Object>emptyMap(), handler);
    }

    public void withMVCGroup(String mvcType, String mvcName, Closure handler) {
        withMVCGroup(mvcType, mvcName, Collections.<String, Object>emptyMap(), handler);
    }

    public void withMVCGroup(String mvcType, Map<String, Object> args, Closure handler) {
        withMVCGroup(mvcType, mvcType, args, handler);
    }

    public void withMVCGroup(String mvcType, String mvcName, Map<String, Object> args, Closure handler) {
        try {
            List<? extends GriffonMvcArtifact> group = createMVCGroup(mvcType, mvcName, args);
            handler.call(group.toArray(new Object[3]));
        } finally {
            try {
                destroyMVCGroup(mvcName);
            } catch(Exception x) {
                if(getLog().isWarnEnabled()) getApp().getLog().warn("Could not destroy group ["+mvcName+"] of type "+mvcType, GriffonExceptionHandler.sanitize(x));
            }
        }
    }

    public <M extends GriffonModel, V extends GriffonView, C extends GriffonController> void withMVCGroup(String mvcType, MVCClosure<M, V, C> handler) {
        withMVCGroup(mvcType, mvcType, Collections.<String, Object>emptyMap(), handler);
    }

    public <M extends GriffonModel, V extends GriffonView, C extends GriffonController> void withMVCGroup(String mvcType, String mvcName, MVCClosure<M, V, C> handler) {
        withMVCGroup(mvcType, mvcName, Collections.<String, Object>emptyMap(), handler);
    }

    public <M extends GriffonModel, V extends GriffonView, C extends GriffonController> void withMVCGroup(String mvcType, Map<String, Object> args, MVCClosure<M, V, C> handler) {
        withMVCGroup(mvcType, mvcType, args, handler);
    }

    public <M extends GriffonModel, V extends GriffonView, C extends GriffonController> void withMVCGroup(String mvcType, String mvcName, Map<String, Object> args, MVCClosure<M, V, C> handler) {
        try {
            List<? extends GriffonMvcArtifact> group = createMVCGroup(mvcType, mvcName, args);
            handler.call((M) group.get(0), (V) group.get(1), (C) group.get(2));
        } finally {
            try {
                destroyMVCGroup(mvcName);
            } catch(Exception x) {
                if(getLog().isWarnEnabled()) getApp().getLog().warn("Could not destroy group ["+mvcName+"] of type "+mvcType, GriffonExceptionHandler.sanitize(x));
            }
        }
    }
}
