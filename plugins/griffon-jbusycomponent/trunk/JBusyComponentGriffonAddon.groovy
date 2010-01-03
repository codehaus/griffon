/*
 * Copyright 2009-2010 the original author or authors.
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
 * limitations under the License.
 */

import org.divxdede.swing.busy.*
import groovy.swing.factory.BeanFactory
import griffon.jbusycomponent.factory.*
import org.jdesktop.jxlayer.plaf.ext.*

/**
 * @author Andres Almiray
 */
class JBusyComponentGriffonAddon {
   def factories = [
       busyComponent: new JBusyComponentFactory(),
       busyModel: new BeanFactory(DefaultBusyModel, true),
       futureBusyModel: new BeanFactory(FutureBusyModel, true),
       jxlayer: new JXLayerFactory(),
       buttonPanelUI: new BeanFactory(ButtonPanelUI, true),
       debugRepaintingUI: new BeanFactory(DebugRepaintingUI, true),
       mouseScrollableUI: new BeanFactory(MouseScrollableUI, true),
       spotLightUI: new SpotLightUIFactory(),
       lockableUI: new LockableUIFactory(),
       bufferedImageOpEffect: new BufferedImageOpEffectFactory(),
   ]
}
