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
import griffon.swing.SwingUtils
import griffon.util.UIThreadHelper
import org.pushingpixels.trident.Timeline

/**
 * Fades a window.<p>
 * Animates the window's opacity from its current value (or 1.0f) to 0.0f.<p>
 * <p>Shared parameters:
 * <ul>
 *    <li><b>duration</b>: long, how long should the animation take. default: 500l</li>
 *    <li><b>delay</b>: long, wait time before the animation starts. default: 0l</li>
 *    <li><b>ease</b>: TimelineEase. default: Linear</li>
 *    <li><b>wait</b>: boolean. Force the caller thread to wait until the effects finishes. default: false</li>
 * </ul>
 *
 * <p>If a callback is supplied it will be called at the end of the animation,
 * with the component and supplied parameters as arguments.</p>
 *
 * @author Andres Almiray
 */
class Fade extends Opacity {
    /**
     * Creates a new Fade effect.<br/>
     *
     * @param params - set of options
     * @param window - the window to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */ 
    Fade(Map params = [:], Window window, Closure callback = null) {
        super(EffectUtil.mergeParams(params), window, callback)
        def ps = paramsInternal()
        ps.from = SwingUtils.getWindowOpacity(window) ?: 1.0f
        ps.to = 0f
    }

    protected void setupTimeline(Timeline timeline) {
        super.setupTimeline(timeline)
        EffectUtil.setupCallback(timeline) {
            component.visible = false
        }
    }

    protected void doBeforePlay() {
        // make sure the window is visible
        UIThreadHelper.instance.executeSync {
            component.visible = true
            SwingUtils.setWindowOpacity(component, params.from)
        }
    }
}
