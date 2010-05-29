/*
 * Copyright (c) 2010 Effects - Andres Almiray. All Rights Reserved.
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
 *  o Neither the name of Effects - Andres Almiray nor the names of
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

package griffon.effects

import java.awt.Component
import java.awt.Window
import org.pushingpixels.trident.Timeline

/**
 * Effects.<p>
 * The following parameters are shared across all effects unless otherwise specified
 * <ul>
 *    <li><b>duration</b>: long, how long should the animation take. default: 500l</li>
 *    <li><b>delay</b>: long, wait time before the animation starts. default: 0l</li>
 *    <li><b>ease</b>: TimelineEase. default: Linear</li>
 * </ul>
 *
 * <p>If a callback is supplied it will be called at the end of the animation,
 * with the component and supplied parameters as arguments.</p>
 *
 * @author Andres Almiray
 */
class Effects {
    /**
     * Shakes a component.<br/>
     * Parameters:
     * <ul>
     *    <li><b>distance</b>: int, in pixels. default: 20i</li>
     * </ul>
     *
     * @param params - set of options
     * @param component - the component to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */ 
    static void shake(Map params = [:], Component component, Closure callback = null) {
        new Shake(params, component, callback).run()
    }

    /**
     * Moves a component.<br/>
     * Parameters:
     * <ul>
     *    <li><b>x</b>: int, in pixels. default: 0i</li>
     *    <li><b>y</b>: int, in pixels. default: 0i</li>
     *    <li><b>mode</b>: String, if movement should be 'relative' or 'absolute'. default: 'relative'</li>
     * </ul>
     *
     * @param params - set of options
     * @param component - the component to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */ 
    static void move(Map params = [:], Component component, Closure callback = null) {
        new Move(params, component, callback).run()
    }

    /**
     * Resizes a component.<br/>
     * Parameters:
     * <ul>
     *    <li><b>w</b>: int, in pixels. default: 0i</li>
     *    <li><b>h</b>: int, in pixels. default: 0i</li>
     *    <li><b>mode</b>: String, if the update should be 'relative' or 'absolute'. default: 'relative'</li>
     * </ul>
     *
     * @param params - set of options
     * @param component - the component to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */ 
    static void resize(Map params = [:], Component component, Closure callback = null) {
        new Resize(params, component, callback).run()
    }

    /**
     * Moves and resizes a component.<br/>
     * Parameters:
     * <ul>
     *    <li><b>x</b>: int, in pixels. default: 0i</li>
     *    <li><b>y</b>: int, in pixels. default: 0i</li>
     *    <li><b>w</b>: int, in pixels. default: 0i</li>
     *    <li><b>h</b>: int, in pixels. default: 0i</li>
     *    <li><b>mode</b>: String, if movement should be 'relative' or 'absolute'. default: 'relative'</li>
     * </ul>
     *
     * @param params - set of options
     * @param component - the component to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */ 
    static void bounds(Map params = [:], Component component, Closure callback = null) {
        new Bounds(params, component, callback).run()
    }

    /**
     * Animates window opacity.<br/>
     * Parameters:
     * <ul>
     *    <li><b>from</b>: float, starting value. default: 0.0f</li>
     *    <li><b>to</b>: float, ending value. default: 1.0f</li>
     * </ul>
     *
     * @param params - set of options
     * @param window - the window to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */ 
    static void opacity(Map params = [:], Window window, Closure callback = null) {
        new Opacity(params, window, callback).run()
    }

    /**
     * Fades a window.<br/>
     * Animates the window's opacity from its current value (or 1.0f) to 0.0f.
     *
     * @param params - set of options
     * @param window - the window to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */
    static void fade(Map params = [:], Window window, Closure callback = null) {
        new Fade(params, window, callback).run()
    }

    /**
     * Makes a window appear.<br/>
     * Animates the window's opacity from its current value (or 0.0f) to 1.0f.
     *
     * @param params - set of options
     * @param window - the window to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */
    static void appear(Map params = [:], Window window, Closure callback = null) {
        new Appear(params, window, callback).run()
    }

    /**
     * Scales a component on x/y coordinates over an specific anchor.<p>
     * Parameters:
     *    <li><b>scaleX</b>: boolean, if the x coorinate should scale. default: true</li>
     *    <li><b>scaleY</b>: boolean, if the y coorinate should scale. default: true</li>
     *    <li><b>from</b>: float, starting value in percentage. default: 100.0f</li>
     *    <li><b>to</b>: float, ending value in percentage. default: 0.0f</li>
     *    <li><b>anchor</b>: Anchor, anchoring point. default: Anchor.CENTER</li>
     * </ul>
     *
     * @param params - set of options
     * @param window - the window to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */ 
    static void scale(Map params = [:], Component component, Closure callback = null) {
        new Scale(params, component, callback).run()
    }

    /**
     * Fades and blows up a window.<p>
     *
     * @param params - set of options
     * @param window - the window to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */ 
    static void puff(Map params = [:], Window window, Closure callback = null) {
        new Puff(params, window, callback).run()
    }

    /**
     * Fades and moves a window.<p>
     * <ul>
     *    <li><b>anchor</b>: Anchor, anchoring point. default: Anchor.BOTTOM</li>
     * </ul>
     *
     * @param params - set of options
     * @param window - the window to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */ 
    static void dropOut(Map params = [:], Window window, Closure callback = null) {
        new DropOut(params, window, callback).run()
    }

    /**
     * Fades in and centers a window.<p>
     * <ul>
     *    <li><b>anchor</b>: Anchor, anchoring point. default: Anchor.TOP</li>
     * </ul>
     *
     * @param params - set of options
     * @param window - the window to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */ 
    static void dropIn(Map params = [:], Window window, Closure callback = null) {
        new DropIn(params, window, callback).run()
    }

    /**
     * Runs a series of effects one after another.<br/>
     *
     * @param effects - a list of effects to be run sequentially
     * @param callback - an optional callback to be executed at the end of the chain
     */ 
    static void chain(List<Effect> effects, Closure callback = null) {
        if(!effects) return
        
        List copy = new ArrayList(effects)
        int count = copy.size()

        def runEffect = { Effect effect -> effect.run() }

        copy.eachWithIndex { effect, index ->
            if(index < count - 1) {
                def oldAfterCallback = effect.afterCallback
                def runEffectCls = runEffect.curry(copy[index + 1])
                effect.afterCallback = {
                    if(oldAfterCallback) oldAfterCallback()
                    runEffectCls()
                }
            }
        }
        def lastEffect = copy[-1]
        def oldAfterCallback = lastEffect.afterCallback
        lastEffect.afterCallback = {
            if(oldAfterCallback) oldAfterCallback()
            if(callback) callback(lastEffect.component, lastEffect.params)
        }
        copy[0].run()
    }
}
