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

import org.newdawn.slick.BasicGame
import org.newdawn.slick.GameContainer
import org.newdawn.slick.SlickException
import org.newdawn.slick.Graphics
import org.newdawn.slick.Input

/**
 * 
 * @author Andres Almiray
 */
class BasicGameDelegate extends BasicGame {
    final BasicSlickGriffonApplication app
    
    Closure onInit
    Closure onRender
    Closure onUpdate
    Closure acceptingInput
    Closure onCloseRequested
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
    
    BasicGameDelegate(BasicSlickGriffonApplication app) {
        super('')
        this.app = app
    }

    void init(GameContainer gc) throws SlickException {
        onInit?.call(gc)
        app.event('SlickInit', [app, gc])
    }

    void update(GameContainer gc, int delta) throws SlickException {
        onUpdate?.call(gc, delta)
        app.event('SlickUpdate', [app, gc, delta])   
    }

    void render(GameContainer gc, Graphics g) throws SlickException {
        onRender?.call(gc, g)
        app.event('SlickRender', [app, gc, g])
    }
    
    boolean isAcceptingInput() {
       acceptingInput?.call() ?: true 
    }

    boolean closeRequested() {
       onCloseRequested?.call() ?: true
    }

    void controllerButtonPressed(int controller, int button) {
        onControllerButtonPressed?.call(controller, button)
    }

    void controllerButtonReleased(int controller, int button) {
        onControllerButtonReleased?.call(controller, button)
    }

    void controllerDownPressed(int controller) {
        onControllerDownPressed?.call(controller)
    }

    void controllerDownReleased(int controller) {
        onControllerDownReleased?.call(controller)
    }

    void controllerLeftPressed(int controller) {
        onControllerLeftPressed?.call(controller)
    }

    void controllerLeftReleased(int controller) {
        onControllerLeftReleased?.call(controller)
    }

    void controllerRightPressed(int controller) {
        onControllerRightPressed?.call(controller)
    }

    void controllerRightReleased(int controller) {
        onControllerRightReleased?.call(controller)
    }

    void controllerUpPressed(int controller) {
        onControllerUpPressed?.call(controller)
    }

    void controllerUpReleased(int controller) {
        onControllerUpReleased?.call(controller)
    }

    void inputEnded() {
        onInputEnded?.call()
    }

    void inputStarted() {
        onInputStarted?.call()
    }

    void keyPressed(int key, char c) {
        onKeyPressed?.call(key, c)
    }

    void keyReleased(int key, char c) {
        onKeyReleased?.call(key, c)
    }

    void mouseClicked(int button, int x, int y, int clickCount) {
        onMouseClicked?.call(button, x, y, clickCount)
    }

    void mouseDragged(int oldx, int oldy, int newx, int newy) {
        onMouseDragged?.call(oldx, oldy, newx, newy)
    }

    void mouseMoved(int oldx, int oldy, int newx, int newy) {
        onMouseMoved?.call(oldx, oldy, newx, newy)
    }

    void mousePressed(int button, int x, int y) {
        onMousePressed?.call(button, x, y)
    }

    void mouseReleased(int button, int x, int y) {
        onMouseReleased?.call(button, x, y)
    }

    void mouseWheelMoved(int change) {
        onMouseWheelMoved?.call(change)
    }
    
    void setInput(Input input) {
        onSetInput?.call(input)
    }
}
