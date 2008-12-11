/*
 * Copyright 2008 the original author or authors.
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

package griffon.builder.fx.factory

import javafx.scene.Scene
import com.sun.scenario.scenegraph.JSGPanel
import com.sun.javafx.scene.JSGPanelSceneImpl

/**
 * @author Andres Almiray <aalmiray@users.sourceforge.com>
 */
class FxSceneFactory extends FxBeanFactory {
    private static final String SCENE_HOST = "_FX_SCENE_HOST_"

    FxSceneFactory() {
        super( Scene, false )
    }

    public void setParent( FactoryBuilderSupport builder, Object parent, Object child ) {
        def host = new JSGPanelSceneImpl()
        host.scene = child
        def panel = new JSGPanel()
        panel.scene = host.root
        parent.add(panel)
        builder.context.SCENE_HOST = host
    }

    public void onNodeCompleted( FactoryBuilderSupport builder, Object parent, Object node ) {
        super.onNodeCompleted( builder, parent, node )
        //builder.context.SCENE_HOST."initialize\$"()
    }
}