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
import org.pushingpixels.trident.Timeline

/**
 * Base class for composite, sequential Effects.<p>
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
abstract class ChainedEffect extends AbstractEffect implements CompositeEffect {
    /**
     * Creates a new chained effect.<br/>
     *
     * @param params - set of options
     * @param component - the component to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */
    ChainedEffect(Map params = [:], Component component, Closure callback = null) {
        super(params, component, callback)
    }

    /**
     * Runs the timeline.<p>
     * Executes the supplied callback (if any) at the end of the animation.
     */
    void run() {
        List<BasicEffect> effects = makeEffects()
        int count = effects.size()

        def runEffect = { BasicEffect effect -> effect.run() }

        effects.eachWithIndex { BasicEffect effect, index ->
            if(index < count - 1) {
                effect.chainCallback = runEffect.curry(effects[index + 1])
            }
        }
        def oldChainCallback = effects[-1].chainCallback
        effects[-1].chainCallback = {
            if(oldChainCallback) oldChainCallback()
            if(callback) callback(component, params)
            if(chainCallback) chainCallback()
        }
        effects[0].run()
    }

    /**
     * Configures the Timeline for this effect<p>
     *
     * @param timeline - a timeline on which properties to interpolate can be set
     */
    protected void setupTimeline(Timeline timeline) {
        // empty
    }
}
