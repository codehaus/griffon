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

import org.pushingpixels.trident.Timeline
import org.pushingpixels.trident.Timeline.TimelineState
import org.pushingpixels.trident.ease.Linear
import org.pushingpixels.trident.callback.UIThreadTimelineCallbackAdapter

import java.awt.Window
import static griffon.util.GriffonApplicationUtils.isJdk16
import static griffon.util.GriffonApplicationUtils.isJdk17

/**
 * @author Andres Almiray
 */
final class EffectUtil {
    private static final Map DEFAULT_PARAMS = [
        duration: 500l,
        delay: 0l,
        ease: new Linear(),
        from: 0.0f,
        to: 1.0f,
        x: 0i,
        y: 0i,
        w: 0i,
        h: 0i
    ]

    protected static final Closure NO_CALLBACK = { t, p -> }

    static String toString(Effect effect) {
        [effect.class.simpleName,'[params:',effect.params,']'].join('')
    }

    static void setupCallback(final Effect effect, Timeline timeline) {
        if(effect.callback && effect.callback != NO_CALLBACK) {
            timeline.addCallback(new UIThreadTimelineCallbackAdapter() {
                @Override
                public void onTimelineStateChanged(TimelineState oldState,
                      TimelineState newState, float durationFraction, float timelinePosition) {
                    if (newState == TimelineState.DONE) {
                        effect.callback(effect.component, effect.params)
                    }
                }
            });
        }
    }

    static void setupAfterCallback(final Effect effect, Timeline timeline) {
        if(effect.afterCallback) {
            timeline.addCallback(new UIThreadTimelineCallbackAdapter() {
                @Override
                public void onTimelineStateChanged(TimelineState oldState,
                      TimelineState newState, float durationFraction, float timelinePosition) {
                    if (newState == TimelineState.DONE) {
                        effect.afterCallback()
                    }
                }
            });
        }
    }

    /**
     * Creates a new Timeline using the effect's parameters
     *
     * @param effect - the effect that contains timeline parameters
     * @return a newly created Timeline
     */
    static Timeline newTimeline(Effect effect) {
        Timeline timeline = new Timeline(effect.component)
        timeline.duration = toLong(effect.params.duration, 500l)
        timeline.initialDelay = toLong(effect.params.delay, 0l)
        timeline.ease = effect.params.ease
        return timeline
    }

    static Map mergeParams(Map params, Map overrides = [:]) {
        return [*:DEFAULT_PARAMS, *:overrides, *:params]
    }

    static float toDouble(value, double defaultValue = 0.0d) {
        if(value instanceof Number) {
            return value.doubleValue()
        } else {
            try {
                return Double.parseDouble(String.valueOf(value))
            } catch(NumberFormatException nfe) {
                return defaultValue
            }
        }
    }

    static float toFloat(value, float defaultValue = 0.0f) {
        if(value instanceof Number) {
            return value.floatValue()
        } else {
            try {
                return Float.parseFloat(String.valueOf(value))
            } catch(NumberFormatException nfe) {
                return defaultValue
            }
        }
    }

    static int toInt(value, int defaultValue = 0i) {
        if(value instanceof Number) {
            return value.intValue()
        } else {
            try {
                return Integer.parseInt(String.valueOf(value))
            } catch(NumberFormatException nfe) {
                return defaultValue
            }
        }
    }

    static int toLong(value, long defaultValue = 0l) {
        if(value instanceof Number) {
            return value.longValue()
        } else {
            try {
                return Long.parseLong(String.valueOf(value))
            } catch(NumberFormatException nfe) {
                return defaultValue
            }
        }
    }

    static float getWindowOpacity(Window window) {
        if(isJdk17) {
            return toFloat(window.getOpacity())
        } else if(isJdk16) {
            Class awtUtilities = Class.forName('com.sun.awt.AWTUtilities')
            return toFloat(awtUtilities.getWindowOpacity(window))
        }
        return 1.0f
    }

    static void setWindowOpacity(Window window, float opacity) {
        if(isJdk17) {
            window.setOpacity(opacity)
        } else if(isJdk16) {
            Class awtUtilities = Class.forName('com.sun.awt.AWTUtilities')
            awtUtilities.setWindowOpacity(window, opacity)
        }
    }
}
