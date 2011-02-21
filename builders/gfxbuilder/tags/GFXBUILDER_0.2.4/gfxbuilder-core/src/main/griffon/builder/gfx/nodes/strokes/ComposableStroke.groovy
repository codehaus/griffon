/*
 * Copyright 2007-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 */

package griffon.builder.gfx.nodes.strokes

import java.awt.Stroke
import griffon.builder.gfx.StrokeProvider

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.net>
 */
public interface ComposableStroke /* extends StrokeProvider */ {
    void addStroke( Stroke stroke )

    void addStroke( StrokeProvider stroke )

    ComposableStroke leftShift( Stroke stroke )

    ComposableStroke leftShift( StrokeProvider stroke )
}