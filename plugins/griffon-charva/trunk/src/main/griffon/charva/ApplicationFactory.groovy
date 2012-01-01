/*
 * Copyright 2010-2011 the original author or authors.
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
 * See the License for the specific language govnerning permissions and
 * limitations under the License.
 */

package griffon.charva

import griffon.charva.factory.FrameFactory

/**
 * @author Andres Almiray
 */
class ApplicationFactory extends FrameFactory {
    public Object newInstance(FactoryBuilderSupport builder, Object name, Object value, Map attributes) {
        println " === $attributes === "
        def frame = builder.app.createApplicationContainer()
        println ">>> created frame $frame"
        handleRootPaneTasks(builder, frame, attributes)
        return frame
    }
}
