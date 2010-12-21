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

package griffon.plugins.slick;

import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

import griffon.core.GriffonMvcArtifact;

/**
 * 
 * @author Andres Almiray
 */
public interface GriffonGameState extends GriffonMvcArtifact {
    StateBasedSlickGriffonApplication getStateBasedApp();
    
    int getID();

    void init(GameContainer gc, StateBasedGame game) throws SlickException;

    void update(GameContainer gc, StateBasedGame game, int delta) throws SlickException;

    void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException;
    
    boolean isAcceptingInput();
    
    void controllerButtonPressed(int controller, int button);

    void controllerButtonReleased(int controller, int button);

    void controllerDownPressed(int controller);

    void controllerDownReleased(int controller);

    void controllerLeftPressed(int controller);

    void controllerLeftReleased(int controller);

    void controllerRightPressed(int controller);

    void controllerRightReleased(int controller);

    void controllerUpPressed(int controller);

    void controllerUpReleased(int controller);

    void inputEnded();

    void inputStarted();

    void keyPressed(int key, char c);

    void keyReleased(int key, char c);

    void mouseClicked(int button, int x, int y, int clickCount);

    void mouseDragged(int oldx, int oldy, int newx, int newy);

    void mouseMoved(int oldx, int oldy, int newx, int newy);

    void mousePressed(int button, int x, int y);

    void mouseReleased(int button, int x, int y);

    void mouseWheelMoved(int change);

    void setInput(Input input);
    
    void enter(GameContainer container, StateBasedGame game) throws SlickException;
    
    void leave(GameContainer container, StateBasedGame game) throws SlickException;
}
