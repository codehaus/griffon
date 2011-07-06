/*
 * Copyright (c) 2010 Griffon Slick - Andres Almiray. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 *  o Neither the name of Griffon Slick - Andres Almiray nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.codehaus.griffon.runtime.slick;

import griffon.core.*;
import griffon.plugins.slick.GriffonGameState;
import griffon.plugins.slick.StateBasedSlickGriffonApplication;
import groovy.lang.Closure;
import groovy.lang.GroovySystem;
import groovy.lang.MetaClass;
import org.codehaus.griffon.runtime.core.AbstractGriffonArtifact;
import org.codehaus.griffon.runtime.util.GriffonApplicationHelper;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import static java.util.Arrays.asList;

/**
 * @author Andres Almiray
 */
public abstract class AbstractGriffonGameState extends BasicGameState implements GriffonGameState {
    private GriffonApplication app;
    private final Logger log;

    public AbstractGriffonGameState() {
        log = LoggerFactory.getLogger("griffon.app.state." + getClass().getName());
    }

    public GriffonApplication getApp() {
        return app;
    }

    public void setApp(GriffonApplication app) {
        this.app = app;
    }

    public StateBasedSlickGriffonApplication getStateBasedApp() {
        return (StateBasedSlickGriffonApplication) app;
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
        GriffonApplicationHelper.withMVCGroup(getApp(), mvcType, mvcName, args, handler);
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
        GriffonApplicationHelper.withMVCGroup(getApp(), mvcType, mvcName, args, handler);
    }

    public final void init(GameContainer gc, StateBasedGame game) throws SlickException {
        doInit(gc, game);
        getApp().event("SlickStateInit", asList(app, getID(), gc, game));
    }

    public final void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        doUpdate(gc, game, delta);
        getApp().event("SlickStateUpdate", asList(app, getID(), gc, game, delta));
    }

    public final void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        doRender(gc, game, g);
        getApp().event("SlickStateRender", asList(app, getID(), gc, game, g));
    }

    protected void doInit(GameContainer gc, StateBasedGame game) throws SlickException {
        // empty
    }

    protected void doUpdate(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        // empty
    }

    protected void doRender(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        // empty
    }
}
