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
import java.util.concurrent.CountDownLatch

/**
 * Base class for all Effects.<p>
 * The following parameters are shared across all effects unless otherwise specified
 * <ul>
 *    <li><b>duration</b>: long, how long should the animation take. default: 500l</li>
 *    <li><b>delay</b>: long, wait time before the animation starts. default: 0l</li>
 *    <li><b>ease</b>: TimelineEase. default: Linear</li>
 *    <li><b>wait</b>: boolean. Force the caller thread to wait until the effects finishes. default: false</li>
 * </ul>
 *
 * @author Andres Almiray
 */
abstract class AbstractEffect implements Effect {
    private final Map params
    /** effect component */
    final Component component
    /** end-of-animation callback */
    final Closure callback
    /** callback to be executed before the effect starts */
    Closure beforeCallback
    /** callback to be executed after the effect ends */
    Closure afterCallback
    /** wait flag */
    final boolean waitForCompletion

    /**
     * Creates a new effect.<br/>
     *
     * @param params - set of options
     * @param component - the component to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */
    AbstractEffect(Map params = [:], Component component, Closure callback = null) {
        this.waitForCompletion = params.remove('wait') ?: false
        this.params = params
        this.component = component
        this.callback = callback ?: EffectUtil.NO_CALLBACK
    }

    /**
     * Returns an unmodifiable view of the effect's arguments
     *
     * @return a Map with the effect's parameters including 
     * default settings.
     */
    Map getParams() {
        Collections.unmodifiableMap(this.params)
    }

    /**
     * Mutable view of the effect's parameters.<p>
     * <b>For internal use only.</b>
     *
     * @return a Map with the effect's parameters including 
     * default settings.
     */
    protected Map paramsInternal() {
        this.params
    }

    String toString() {
        EffectUtil.toString(this)
    }

    /**
     * Runs the timeline.<p>
     * Creates and setups the timeline and callbacks, then plays the timeline.<p>
     *
     * <p>If a callback was supplied it will be called at the end of the animation,
     * with the component and supplied parameters as arguments.</p>
     */
    void run() {
        Timeline timeline = EffectUtil.newTimeline(this)
        if(beforeCallback) beforeCallback()
        setupTimeline(timeline)
        EffectUtil.setupEffectCallback(this, timeline)
        EffectUtil.setupAfterCallback(this, timeline)
        CountDownLatch latch = EffectUtil.setupWaitForCompletion(this, timeline)
        doBeforePlay()
        timeline.play()
        if(latch) latch.await()
    }

    /**
     * Configures the Timeline for this effect<p>
     *
     * @param timeline - a timeline on which properties to interpolate can be set
     */
    protected abstract void setupTimeline(Timeline timeline)


    /**
     * Last opportunity to execute custom code before the Timeline starts 
     */
    protected void doBeforePlay() {}
}
