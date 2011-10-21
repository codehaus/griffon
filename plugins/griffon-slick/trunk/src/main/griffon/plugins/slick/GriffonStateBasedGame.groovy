/*
 * Copyright (c) 2010-2011 Griffon Slick - Andres Almiray. All Rights Reserved.
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
import org.newdawn.slick.SlickException
import org.newdawn.slick.state.StateBasedGame

/**
 *
 * @author Andres Almiray
 */
class GriffonStateBasedGame extends StateBasedGame {
    final StateBasedSlickGriffonApplication app

    Closure onPreRenderState
    Closure onPostRenderState
    Closure onPreUpdateState
    Closure onPostUpdateState

    GriffonStateBasedGame(StateBasedSlickGriffonApplication app) {
        super('')
        this.app = app
    }

    void initStatesList(GameContainer container) {
        app.event('SlickInitStates', [this])
    }

    protected void preRenderState(GameContainer container, Graphics g) throws SlickException {
        if (onPreRenderState) {
            onPreRenderState.delegate = this
            onPreRenderState.resolveStrategy = Closure.DELEGATE_FIRST
            onPreRenderState(container, g)
        }
        app.event('SlickPreRenderState', [container, g])
    }

    protected void postRenderState(GameContainer container, Graphics g) throws SlickException {
        if (onPostRenderState) {
            onPostRenderState.delegate = this
            onPostRenderState.resolveStrategy = Closure.DELEGATE_FIRST
            onPostRenderState(container, g)
        }
        app.event('SlickPostRenderState', [container, g])
    }

    protected void preUpdateState(GameContainer container, int delta) throws SlickException {
        if (onPreUpdateState) {
            onPreUpdateState.delegate = this
            onPreUpdateState.resolveStrategy = Closure.DELEGATE_FIRST
            onPreUpdateState(container, delta)
        }
        app.event('SlickPreUpdateState', [container, delta])
    }

    protected void postUpdateState(GameContainer container, int delta) throws SlickException {
        if (onPostUpdateState) {
            onPostUpdateState.delegate = this
            onPostUpdateState.resolveStrategy = Closure.DELEGATE_FIRST
            onPostUpdateState(container, delta)
        }
        app.event('SlickPostUpdateState', [container, delta])
    }
}
