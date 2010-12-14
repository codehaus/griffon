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

package griffon.plugins.slick.artifact;

import static java.util.Arrays.asList;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

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
import griffon.plugins.slick.StateBasedSlickGriffonApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * @author Andres Almiray
 */
public abstract class AbstractGriffonGameState extends BasicGameState implements GriffonGameState {
    private GriffonApplication app;
    private final Logger log;
    
    public AbstractGriffonGameState() {
        log = LoggerFactory.getLogger("griffon.app.state."+getClass().getName());
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
