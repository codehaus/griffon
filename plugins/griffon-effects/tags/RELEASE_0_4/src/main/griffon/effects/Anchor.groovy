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

/**
 * @author Andres Almiray
 */
enum Anchor {
    TOP, CENTER, BOTTOM, LEFT, RIGHT, TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT;

    /**
     * Resolves the object to a valid Anchor value.
     *
     * @param value an object. May be null.
     * @return Anchor.CENTER always
     */
    static Anchor resolve(Object value) {
        CENTER
    }

    /**
     * Resolves the object to a valid Anchor value.
     *
     * @param value an Anchor instance.
     * @return the same input
     */
    static Anchor resolve(Anchor value) {
        value
    }

    /**
     * Resolves the string to a valid Anchor value.<p>
     * Attempts conversion by matching common names to Anchor
     * constants. Accepts aliases. Input may be in lower, upper
     * and/or mixed case. Spaces are transformed inbto _.<p>
     * Valid values are:<ul>
     * <li>CENTER =&gt; Anchor.CENTER</li>
     * <li>NORTH, TOP =&gt; Anchor.TOP</li>
     * <li>SOUTH, BOTTOM =&gt; Anchor.BOTTOM</li>
     * <li>WEST, LEFT =&gt; Anchor.LEFT</li>
     * <li>EAST, RIGHT =&gt; Anchor.RIGHT</li>
     * <li>NORTH_WEST, NORTH WEST, TOP_LEFT, TOP LEFT =&gt; Anchor.TOP_LEFT</li>
     * <li>NORTH_EAST, NORTH EAST, TOP_RIGHT, TOP RIGHT =&gt; Anchor.TOP_RIGHT</li>
     * <li>SOUTH_WEST, SOUTH WEST, BOTTOM_LEFT, BOTTOM LEFT =&gt; Anchor.BOTTOM_LEFT</li>
     * <li>SOUTH_EAST, SOUTH EAST, BOTTOM_RIGHT, BOTTOM RIGHT =&gt; Anchor.BOTTOM_RIGHT</li>
     * </ul>
     *
     * @param value a GString
     * @return the converted value given the aforementioned rules.
     */
    static Anchor resolve(GString value) {
        resolve(value.toString())
    }

    /**
     * Resolves the object to a valid Anchor value.
     *
     * @return Anchor.CENTER always
     */
    static Anchor resolve(String value) {
        value = value.toUpperCase().replace(' ','_')
        switch(value) {
            case 'NORTH': return TOP
            case 'SOUTH': return BOTTOM
            case 'WEST': return LEFT
            case 'EAST': return RIGHT
            case 'NORTH_WEST': return TOP_LEFT
            case 'NORTH_EAST': return TOP_RIGHT
            case 'SOUTH_WEST': return BOTTOM_LEFT
            case 'SOUTH_EAST': return BOTTOM_RIGHT
            default: return valueOf(Anchor, value)
        }
    }

    boolean isTop() {
        this == TOP || this == TOP_LEFT || this == TOP_RIGHT
    }

    boolean isBottom() {
        this == BOTTOM || this == BOTTOM_LEFT || this == BOTTOM_RIGHT
    }

    boolean isLeft() {
        this == LEFT || this == TOP_LEFT || this == BOTTOM_LEFT
    }

    boolean isRight() {
        this == RIGHT || this == TOP_RIGHT || this == BOTTOM_RIGHT
    }
}
