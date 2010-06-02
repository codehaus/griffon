/*
 * Copyright 2010 the original author or authors.
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

import javax.swing.SwingUtilities
import griffon.lookandfeel.LookAndFeelInfo
import griffon.lookandfeel.LookAndFeelProvider
import griffon.lookandfeel.LookAndFeelManager

/**
 * @author Andres Almiray
 */
class LookAndFeelSelectorController {
    def model
    def view

    def preview = { evt = null ->
        if(!model.lafSelection) return
        LookAndFeelManager.instance.preview(model.lafSelection, view.desktop)
    }

    def reset = { evt = null ->
        LookAndFeelProvider provider = LookAndFeelManager.instance.getLookAndFeelProvider('System')
        LookAndFeelInfo info = LookAndFeelManager.instance.getLookAndFeelInfo(provider, 'System')
        execAsync {
            LookAndFeelManager.instance.installLookAndFeel(info.lookAndFeel, app)
            setCurrentLookAndFeel(provider)
        }
    }

    private void setCurrentLookAndFeel(LookAndFeelProvider provider) {
        if(!provider) return
        model.providerSelection = provider

        for(p in model.providerModel) {
            if(p == provider) {
                model.providerModel.selectedItem = provider
                model.providerSelection = provider
                break
            }
        }

        selectCurrentLookAndFeel(provider)
    }

    private void selectCurrentLookAndFeel(LookAndFeelProvider provider) {
        LookAndFeelInfo currentLookAndFeel
        for(lookAndFeelInfo in provider.supportedLookAndFeels) {
            if(lookAndFeelInfo.isCurrentLookAndFeel()) {
                currentLookAndFeel = lookAndFeelInfo
                break
            }
        }

        for(l in model.lafInfoModel) {
            if(l == currentLookAndFeel) {
                model.lafInfoModel.selectedItem = currentLookAndFeel
                model.lafSelection = currentLookAndFeel
                break
            }
        }
    }
}
