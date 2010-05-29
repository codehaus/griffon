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
import java.awt.Dimension
import java.awt.Point
import java.awt.Toolkit
import java.awt.GraphicsEnvironment
import org.pushingpixels.trident.Timeline

/**
 * Fades in and moves a window.<p>
 * Parameters:
 * <ul>
 *    <li><b>anchor</b>: Anchor, anchoring point. default: Anchor.TOP</li>
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
class DropIn extends ParallelEffect {
    /**
     * Creates a new DropIn effect.<br/>
     *
     * @param params - set of options
     * @param component - the component to animate
     * @param callback - an optional callback to be executed at the end of the animation
     */ 
    DropIn(Map params = [:], Window window, Closure callback = null) {
        super(EffectUtil.mergeParams(params, [anchor: Anchor.TOP]), window, callback)
        def ps = paramsInternal()
        ps.anchor = Anchor.resolve(ps.anchor)
    }
 
    List<BasicEffect> makeEffects() { 
        def ps = paramsInternal()
        Point origin = component.location
        Dimension size = component.getSize()
        Point center = GraphicsEnvironment.localGraphicsEnvironment.centerPoint
        Dimension screen = Toolkit.defaultToolkit.screenSize

        if(ps.anchor == Anchor.CENTER) {
            
            ps.from = 0f
            ps.to = 100f
            return [
                new Appear(ps, component),
                new Scale(ps, component)
            ] 
        }

        EffectUtil.setWindowOpacity(component, EffectUtil.toFloat(0.0f))
        ps.mode = 'absolute'
        ps.x = (screen.width/2) - (size.width/2)
        ps.y = (screen.height/2) - (size.height/2)
        switch(ps.anchor) {
            case Anchor.TOP:
                component.setLocation(new Point(
                    EffectUtil.toInt(origin.x),
                    EffectUtil.toInt(-size.height)
                )) 
                break
            case Anchor.TOP_LEFT:
                component.setLocation(new Point(
                    EffectUtil.toInt(-size.width),
                    EffectUtil.toInt(-size.height)
                )) 
                break
            case Anchor.LEFT:
                component.setLocation(new Point(
                    EffectUtil.toInt(-size.width),
                    EffectUtil.toInt(origin.y)
                )) 
                break
            case Anchor.BOTTOM_LEFT:
                component.setLocation(new Point(
                    EffectUtil.toInt(-size.width),
                    EffectUtil.toInt(-size.height)
                )) 
                break
            case Anchor.BOTTOM:
                component.setLocation(new Point(
                    EffectUtil.toInt(origin.x),
                    EffectUtil.toInt(screen.height)
                )) 
                break
            case Anchor.BOTTOM_RIGHT:
                component.setLocation(new Point(
                    EffectUtil.toInt(screen.width),
                    EffectUtil.toInt(screen.height)
                )) 
                break
            case Anchor.RIGHT:
                component.setLocation(new Point(
                    EffectUtil.toInt(screen.width),
                    EffectUtil.toInt(origin.y)
                )) 
                break
            case Anchor.TOP_RIGHT:
                component.setLocation(new Point(
                    EffectUtil.toInt(screen.width),
                    EffectUtil.toInt(-size.height)
                )) 
                break
        }

        return [
            new Appear(ps, component),
            new Move(ps, component)
        ]
    }
}
