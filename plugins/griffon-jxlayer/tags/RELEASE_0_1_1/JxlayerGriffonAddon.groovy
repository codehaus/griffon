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

import griffon.jxlayer.factory.*

import groovy.swing.factory.BeanFactory
import org.jdesktop.jxlayer.plaf.ext.*
import static griffon.util.GriffonApplicationUtils.isJdk17

/**
 * @author Andres Almiray
 */
class JxlayerGriffonAddon {
   JxlayerGriffonAddon() {
       if(!isJdk17) {
           factories.buttonPanelUI = new BeanFactory(ButtonPanelUI, true)
           factories.debugRepaintingUI = new BeanFactory(DebugRepaintingUI, true)
           factories.mouseScrollableUI = new BeanFactory(MouseScrollableUI, true)
           factories.spotLightUI = new SpotLightUIFactory()
           factories.lockableUI = new LockableUIFactory()
           factories.bufferedImageOpEffect = new BufferedImageOpEffectFactory()
       }
   }

   def factories = [
       jxlayer: new JXLayerFactory()
   ]
}
