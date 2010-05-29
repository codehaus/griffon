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

import java.awt.Window
import org.pushingpixels.trident.Timeline
import org.pushingpixels.trident.Timeline.TimelineState
import org.pushingpixels.trident.TimelinePropertyBuilder
import org.pushingpixels.trident.TimelinePropertyBuilder.PropertySetter

import static griffon.util.GriffonApplicationUtils.isJdk16
import static griffon.util.GriffonApplicationUtils.isJdk17

/**
 * Animates window opacity.<p>
 * Parameters:
 * <ul>
 *    <li><b>from</b>: float, starting value. default: 0.0f</li>
 *    <li><b>to</b>: float, ending value. default: 1.0f</li>
 * </ul>
 * <p>Shared parameters:
 * <ul>
 *    <li><b>duration</b>: long, how long should the animation take. default: 500l</li>
 *    <li><b>delay</b>: long, wait time before the animation starts. default: 0l</li>
 *    <li><b>ease</b>: TimelineEase. default: Linear</li>
 * </ul>
 *
 * <p>If a callback is supplied it will be called at the end of the animation,
 * with the component and supplied parameters as arguments.</p>
 *
 * <p>Contains code based on Kirill Grouchnikov's work on Onyx, licensed under BSD<br/>
 * see<ul>
 *  <li>http://kenai.com/projects/onyx/</li>
 *  <li>http://pushing-pixels.org</li>
 * </ul>
 *
 * @author Andres Almiray
 */
class Opacity extends AbstractBasicEffect {
    /**
     * Creates a new Opacity effect.<br/>
     *
     * @param params - set of options
     * @param component - the component to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */ 
    Opacity(Map params = [:], Window window, Closure callback = null) {
        super(EffectUtil.mergeParams(params,[from: 0.0f, to: 1.0f]), window, callback)
    }
 
    protected void setupTimeline(Timeline timeline) {
        TimelinePropertyBuilder p = Timeline.property('opacity')
           .from(EffectUtil.toFloat(params.from, 0.0f))
           .to(EffectUtil.toFloat(params.to, 1.0f))

        if(isJdk17) {
            timeline.addPropertyToInterpolate(p)
        } else if(isJdk16) {
            Class awtUtilities = Class.forName('com.sun.awt.AWTUtilities')
   
            timeline.addPropertyToInterpolate(p.setWith(
                new PropertySetter<Float>() {
                    @Override
                    public void set(Object obj, String fieldName, Float value) {
                        awtUtilities.setWindowOpacity(obj, value)
                    }
                }));
        }
    }
}
