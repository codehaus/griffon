/*
 * Copyright 2010 the original author or authors.
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

import griffon.core.GriffonApplication
import net.sourceforge.gvalidation.ValidationEnhancer
import net.sourceforge.gvalidation.artifact.ConstraintArtifactHandler
import net.sourceforge.gvalidation.ConstraintRepository
import griffon.util.ApplicationHolder
import net.sourceforge.gvalidation.renderer.ErrorRendererAttributeDelegator

/**
 * @author Nick Zhu
 */
class ValidationGriffonAddon {
    def addonInit(GriffonApplication app) {
        app.artifactManager.registerArtifactHandler(new ConstraintArtifactHandler(app))
        ConstraintRepository.instance.initialize(app)
    }

    def attributeDelegates = [
            {builder, node, attributes ->
                def attributeDelegator = new ErrorRendererAttributeDelegator()

                if(attributeDelegator.isAttributeSet(attributes)){
                    attributeDelegator.delegate(ApplicationHolder.application, builder, node, attributes)
                }
            }
    ]

}
