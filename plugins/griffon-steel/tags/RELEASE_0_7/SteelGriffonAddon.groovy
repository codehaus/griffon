/*
 * Copyright (c) 2010 Steel - Andres Almiray. All Rights Reserved.
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
 *  o Neither the name of Steel - Andres Almiray nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS 'AS IS'
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

import eu.hansolo.steelseries.gauges.*
import eu.hansolo.steelseries.extras.*
import eu.hansolo.steelseries.tools.*
import griffon.steel.factory.PoiFactory
import griffon.core.GriffonApplication

/**
 * @author Andres Almiray
 */
class SteelGriffonAddon {
    def addonInit(GriffonApplication app) {
        Radar.metaClass.add = { Poi poi ->
            delegate.addPoi(poi)
        }
        Radar.metaClass.leftShift = { Poi poi ->
            delegate.addPoi(poi)
        }
    }

    def factories = [
        airCompass: AirCompass,
        altimeter: Altimeter,
        clock: Clock,
        compass: Compass,
        digitalRadialGauge: DigitalRadial,
        displayCircular: DisplayCircular,
        displayMulti: DisplayMulti,
        displayRectangular: DisplayRectangular,
        displaySingle: DisplaySingle,
        horizon: Horizon,
        led: Led,
        level: Level,
        linearGauge: Linear,
        linearBargraph: LinearBargraph,
        radar: Radar,
        radialGauge: Radial,
        radial1SquareGauge: Radial1Square,
        radial1VerticalGauge: Radial1Vertical,
        radial2TopGauge: Radial2Top,
        radialCounterGauge: RadialCounter,
        radialBargraph: RadialBargraph,
        radialSquareSmall: RadialSquareSmall,
        sparkLine: SparkLine,
        poi: new PoiFactory(),
        stopWatch: StopWatch,
        windDirection: WindDirection
    ]
}
