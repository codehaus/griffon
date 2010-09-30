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
import eu.hansolo.steelseries.tools.Poi
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
        altimeter: Altimeter,
        clock: Clock,
        compass: Compass,
        digitalRadialGauge: DigitalRadial,
        digitalRadialLcdGauge: DigitalRadialLcd,
        displayCircular: DisplayCircular,
        displayMulti: DisplayMulti,
        displayRectangular: DisplayRectangular,
        displaySingle: DisplaySingle,
        led: Led,
        level: Level,
        linearGauge: Linear,
        linearLcdGauge: LinearLcd,
        linearBargraph: LinearBargraph,
        linearBargraphLcd: LinearBargraphLcd,
        radar: Radar,
        radial1Gauge: Radial1,
        radial1LcdGauge: Radial1Lcd,
        radial1SquareGauge: Radial1Square,
        radial1VerticalGauge: Radial1Vertical,
        radial2Gauge: Radial2,
        radial2LcdGauge: Radial2Lcd,
        radial2TopGauge: Radial2Top,
        radial3Gauge: Radial3,
        radial3LcdGauge: Radial3Lcd,
        radial4Gauge: Radial4,
        radial4LcdGauge: Radial4Lcd,
        radialCounterGauge: RadialCounter,
        radialBargraph1: RadialBargraph1,
        radialBargraph1Lcd: RadialBargraph1Lcd,
        radialBargraph2: RadialBargraph2,
        radialBargraph2Lcd: RadialBargraph2Lcd,
        radialBargraph3: RadialBargraph3,
        radialBargraph3Lcd: RadialBargraph3Lcd,
        radialBargraph4: RadialBargraph4,
        radialBargraph4Lcd: RadialBargraph4Lcd,

        poi: new PoiFactory()
    ]
}
