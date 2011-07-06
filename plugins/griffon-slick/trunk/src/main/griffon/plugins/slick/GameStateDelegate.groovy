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

package griffon.plugins.slick

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Input
import org.newdawn.slick.SlickException
import org.newdawn.slick.state.BasicGameState
import org.newdawn.slick.state.StateBasedGame

/**
 * 
 * @author Andres Almiray
 */
class GameStateDelegate extends BasicGameState {
    final StateBasedSlickGriffonApplication app
    final int ID
    
    Closure onInit
    Closure onRender
    Closure onUpdate
    Closure acceptingInput
    Closure onControllerButtonPressed
    Closure onControllerButtonReleased
    Closure onControllerDownPressed
    Closure onControllerDownReleased
    Closure onControllerLeftPressed
    Closure onControllerLeftReleased
    Closure onControllerRightPressed
    Closure onControllerRightReleased
    Closure onControllerUpPressed
    Closure onControllerUpReleased
    Closure onInputEnded
    Closure onInputStarted
    Closure onKeyPressed
    Closure onKeyReleased
    Closure onMouseClicked
    Closure onMouseDragged
    Closure onMouseMoved
    Closure onMousePressed
    Closure onMouseReleased
    Closure onMouseWheelMoved
    Closure onSetInput
    
    GameStateDelegate(StateBasedSlickGriffonApplication app, int id) {
        this.app = app
        this.ID = id
    }

    void init(GameContainer gc, StateBasedGame game) throws SlickException {
        callClosure(onInit, gc, game)
        app.event('SlickInit', [app, gc, game])
    }

    void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException {
        callClosure(onUpdate, gc, game, delta)
        app.event('SlickUpdate', [app, gc, game, delta])   
    }

    void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
        callClosure(onRender, gc, game, g)
        app.event('SlickRender', [app, gc, game, g])
    }
    
    boolean isAcceptingInput() {
       def val = callClosure(acceptingInput)
       val != null ? val : true 
    }

    void controllerButtonPressed(int controller, int button) {
        callClosure(onControllerButtonPressed, controller, button)
    }

    void controllerButtonReleased(int controller, int button) {
        callClosure(onControllerButtonReleased, controller, button)
    }

    void controllerDownPressed(int controller) {
        callClosure(onControllerDownPressed, controller)
    }

    void controllerDownReleased(int controller) {
        callClosure(onControllerDownReleased, controller)
    }

    void controllerLeftPressed(int controller) {
        callClosure(onControllerLeftPressed, controller)
    }

    void controllerLeftReleased(int controller) {
        callClosure(onControllerLeftReleased, controller)
    }

    void controllerRightPressed(int controller) {
        callClosure(onControllerRightPressed, controller)
    }

    void controllerRightReleased(int controller) {
        callClosure(onControllerRightReleased, controller)
    }

    void controllerUpPressed(int controller) {
        callClosure(onControllerUpPressed, controller)
    }

    void controllerUpReleased(int controller) {
        callClosure(onControllerUpReleased, controller)
    }

    void inputEnded() {
        callClosure(onInputEnded, )
    }

    void inputStarted() {
        callClosure(onInputStarted, )
    }

    void keyPressed(int key, char c) {
        callClosure(onKeyPressed, key, c)
    }

    void keyReleased(int key, char c) {
        callClosure(onKeyReleased, key, c)
    }

    void mouseClicked(int button, int x, int y, int clickCount) {
        callClosure(onMouseClicked, button, x, y, clickCount)
    }

    void mouseDragged(int oldx, int oldy, int newx, int newy) {
        callClosure(onMouseDragged, oldx, oldy, newx, newy)
    }

    void mouseMoved(int oldx, int oldy, int newx, int newy) {
        callClosure(onMouseMoved, oldx, oldy, newx, newy)
    }

    void mousePressed(int button, int x, int y) {
        callClosure(onMousePressed, button, x, y)
    }

    void mouseReleased(int button, int x, int y) {
        callClosure(onMouseReleased, button, x, y)
    }

    void mouseWheelMoved(int change) {
        callClosure(onMouseWheelMoved, change)
    }

    void setInput(Input input) {
        callClosure(onSetInput, input)
    }
    
    void enter(GameContainer container, StateBasedGame game) {
        callClosure(onEnter, container, game)
    }
    
    void leave(GameContainer container, StateBasedGame game) {
        callClosure(onLeave, container, game)
    }
    
    private callClosure(Closure cls, Object[] args) {
        if(cls) {
            cls.delegate = this
            cls.resolveStrategy = Closure.DELEGATE_FIRST
            return cls(*args)
        }
        return null
    }
}
