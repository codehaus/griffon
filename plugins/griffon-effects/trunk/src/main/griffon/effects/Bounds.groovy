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
import java.awt.Rectangle
import org.pushingpixels.trident.Timeline
import org.pushingpixels.trident.TimelinePropertyBuilder.PropertySetter

/**
 * Moves and resizes a component.<p>
 * Parameters:
 * <ul>
 *    <li><b>x</b>: int, in pixels. default: 0i</li>
 *    <li><b>y</b>: int, in pixels. default: 0i</li>
 *    <li><b>w</b>: int, in pixels. default: 0i</li>
 *    <li><b>h</b>: int, in pixels. default: 0i</li>
 *    <li><b>mode</b>: String, if update should be 'relative' or 'absolute'. default: 'relative'</li>
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
 * @author Andres Almiray
 */
class Bounds extends AbstractBasicEffect {
    /**
     * Creates a new Bounds effect.<br/>
     *
     * @param params - set of options
     * @param component - the component to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */ 
    Bounds(Map params = [:], Component component, Closure callback = null) {
        super(EffectUtil.mergeParams(params,[mode: 'relative']), component, callback)
    }
 
    protected void setupTimeline(Timeline timeline) {
        Rectangle origin = component.bounds
        int x = EffectUtil.toInt(params.x, 0i)
        int y = EffectUtil.toInt(params.y, 0i)
        int w = EffectUtil.toInt(params.w, 0i)
        int h = EffectUtil.toInt(params.h, 0i)
        Rectangle dest = new Rectangle(
            params.mode == 'absolute' ? x : EffectUtil.toInt(x + origin.x),
            params.mode == 'absolute' ? y : EffectUtil.toInt(y + origin.y),
            params.mode == 'absolute' ? w : EffectUtil.toInt(w + origin.width),
            params.mode == 'absolute' ? h : EffectUtil.toInt(h + origin.height)
        )

        timeline.addPropertyToInterpolate(Timeline.property('bounds').from(origin).to(dest)
            .setWith(
                new PropertySetter<Rectangle>() {
                    @Override
                    public void set(Object obj, String fieldName, Rectangle value) {
                        obj.setBounds(
                            EffectUtil.toInt(value.x),
                            EffectUtil.toInt(value.y),
                            EffectUtil.toInt(value.width),
                            EffectUtil.toInt(value.height)
                        )
                    }
                }));
    }
}
