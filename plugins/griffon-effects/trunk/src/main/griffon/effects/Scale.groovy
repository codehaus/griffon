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
import org.pushingpixels.trident.Timeline.TimelineState
import org.pushingpixels.trident.callback.UIThreadTimelineCallbackAdapter
import org.pushingpixels.trident.TimelinePropertyBuilder.PropertySetter
import static griffon.effects.EffectUtil.*

/**
 * Scales a component on x/y coordinates over an specific anchor.<p>
 * Parameters:
 * <ul>
 *    <li><b>scaleX</b>: boolean, if the x coorinate should scale. default: true</li>
 *    <li><b>scaleY</b>: boolean, if the y coorinate should scale. default: true</li>
 *    <li><b>from</b>: float, starting value in percentage. default: 100.0f</li>
 *    <li><b>to</b>: float, ending value in percentage. default: 0.0f</li>
 *    <li><b>anchor</b>: Anchor, anchoring point. default: Anchor.CENTER</li>
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
class Scale extends AbstractBasicEffect {
    private static final Map DEFAULT_PARAMS = [
        scaleX: true,
        scaleY: true,
        from: 100i,
        to: 0i,
        anchor: Anchor.CENTER
    ]

    /**
     * Creates a new Scale effect.<br/>
     *
     * @param params - set of options
     * @param component - the component to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */ 
    Scale(Map params = [:], Component component, Closure callback = null) {
        super(mergeParams(params, DEFAULT_PARAMS), component, callback)
        def ps = paramsInternal()
        ps.anchor = Anchor.resolve(ps.anchor) 
    }
 
    protected void setupTimeline(Timeline timeline) {
        timeline.addCallback(new Scaler())
    }

    private class Scaler extends UIThreadTimelineCallbackAdapter {
        private final float factor
        private final Rectangle origin

        Scaler() {
            def ps = paramsInternal()
            float to = toFloat(ps.to)
            float from = toFloat(ps.from)
            factor = toFloat((to - from)/100)
            origin = getComponent().bounds
        }

	@Override
	public void onTimelinePulse(float durationFraction, float timelinePosition) {
            scale(timelinePosition)
        }

	public void onTimelineStateChanged(TimelineState oldState,
			TimelineState newState, float durationFraction,
			float timelinePosition) {
            if (newState == TimelineState.DONE) {
                scale(1.0f)
            }
        }

        private void scale(float position) {
            Map ps = getParams()
            float to = toFloat(ps.to)
            float from = toFloat(ps.from)
            float currentScale = toFloat((from/100) + (factor * position))
            float w = ps.scaleX ? origin.width * currentScale : origin.width
            float h = ps.scaleY ? origin.height * currentScale : origin.height

            switch(ps.anchor) {
                case Anchor.TOP_LEFT:
                   newBounds(origin.x, origin.y, w, h) 
                   break
                case Anchor.TOP:
                   newBounds(centerX(w), origin.y, w, h) 
                   break
                case Anchor.TOP_RIGHT:
                   newBounds(rightX(w), origin.y, w, h) 
                   break
                case Anchor.LEFT:
                   newBounds(origin.x, centerY(h), w, h) 
                   break
                case Anchor.CENTER:
                   newBounds(centerX(w), centerY(h), w, h) 
                   break
                case Anchor.RIGHT:
                   newBounds(rightX(w), centerY(h), w, h) 
                   break
                case Anchor.BOTTOM_LEFT:
                   newBounds(origin.x, bottomY(h), w, h) 
                   break
                case Anchor.BOTTOM:
                   newBounds(centerX(w), bottomY(h), w, h) 
                   break
                case Anchor.BOTTOM_RIGHT:
                   newBounds(rightX(w), bottomY(h), w, h) 
                   break
            }
	}

        private centerX(w) { origin.x + (origin.width - w)/2 }
        private rightX(w) { origin.x + origin.width - w }
        private centerY(h) { origin.y + (origin.height - h)/2 }
        private bottomY(h) { origin.y + origin.height - h }

        private void newBounds(x, y, w, h) {
            getComponent().setBounds(
                toInt(x),
                toInt(y),
                toInt(w),
                toInt(h)
            )
        }
    }
}
