/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.jboss.weld.environment.se.Weld
import org.jboss.weld.environment.se.WeldContainer
import org.jboss.weld.environment.se.events.ContainerInitialized

import griffon.core.GriffonApplication
import griffon.plugins.weld.util.NonContextual
import griffon.plugins.weld.util.NonContextual.Instance

/**
 * @author Andres Almiray
 */
class WeldGriffonAddon {
    private Weld weld
    private WeldContainer weldContainer

    void addonInit(GriffonApplication app) {
        weld = new Weld()
        weldContainer = weld.initialize()

        app.metaClass.weld = weld
        app.metaClass.weldContainer = weldContainer
    }

    def events = [
            LoadAddonsEnd: { app, addons ->
                app.weldContainer.event().select(ContainerInitialized).fire(new ContainerInitialized())
            },
            NewInstance: { klass, type, instance ->
                NonContextual.of(klass, app.weldContainer.beanManager).existingInstance(instance).with {
                    inject()
                    postConstruct()
                }
            },
            ShutdownStart: { app ->
                app.weld.shutdown()
            }
    ]
}
